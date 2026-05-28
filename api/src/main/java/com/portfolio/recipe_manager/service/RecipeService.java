package com.portfolio.recipe_manager.service;

import com.portfolio.recipe_manager.dto.RecipeRequest;
import com.portfolio.recipe_manager.dto.RecipeSearchRequest;
import com.portfolio.recipe_manager.dto.RecipeSearchResult;
import com.portfolio.recipe_manager.entity.*;
import com.portfolio.recipe_manager.exception.InvalidIngredientsLineException;
import com.portfolio.recipe_manager.exception.RecipeNotFoundException;
import com.portfolio.recipe_manager.repository.RecipeRepository;
import com.portfolio.recipe_manager.repository.specification.RecipeSpecifications;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final TaggingService taggingService;

    public RecipeService(RecipeRepository recipeRepository,
                         TaggingService taggingService) {
        this.recipeRepository = recipeRepository;
        this.taggingService = taggingService;
    }

    // Deciding not to cache the result of searchRecipes since it is not only for "all recipes"
    // The lightweight DTO of RecipeSearchResult already makes the payload manageable
    @Transactional(readOnly = true)
    public Page<RecipeSearchResult> searchRecipes(
            RecipeSearchRequest request,
            Pageable pageable) {
        // Replaces Specification.where(null) without using deprecated or missing methods
        // Cannot use Specification.where(null) -> deprecated as of 3.5.0
        // Cannot use Specification.unrestricted() -> introduced in 3.5.2
        Specification<Recipe> spec = (root, query, builder) -> builder.conjunction();

        if (request.getName() != null && !request.getName().isBlank()) {
            Specification<Recipe> nameAndDescriptionSpec = RecipeSpecifications.nameContains(request.getName())
                    .or(RecipeSpecifications.descriptionContains(request.getName()))
                    .or(RecipeSpecifications.includesIngredient(request.getName()))
                    .or(RecipeSpecifications.instructionContains(request.getName()));
            spec = spec.and(nameAndDescriptionSpec);
        }
        if (request.getServingSize() != null) {
            spec = spec.and(RecipeSpecifications.hasServingSize(request.getServingSize()));
        }
        if (request.getTags() != null) {
            for (String tag : request.getTags()) {
                spec = spec.and(RecipeSpecifications.hasTag(tag));
            }
        }
        if (request.getInstructions() != null) {
            for (String instruction : request.getInstructions()) {
                spec = spec.and(RecipeSpecifications.instructionContains(instruction));
            }
        }
        if (request.getIncludeIngredients() != null) {
            for (String ingredient : request.getIncludeIngredients()) {
                spec = spec.and(RecipeSpecifications.includesIngredient(ingredient));
            }
        }
        if (request.getExcludeIngredients() != null) {
            for (String ingredient : request.getExcludeIngredients()) {
                spec = spec.and(RecipeSpecifications.excludesIngredient(ingredient));
            }
        }

        return recipeRepository.findBy(spec, query -> query
                .as(RecipeSearchResult.class)
                .page(pageable));
    }

    @Cacheable(value = "recipe", key = "#recipeId")
    @Transactional(readOnly = true)
    public Recipe getRecipeById(Long recipeId) {
        return recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RecipeNotFoundException("No recipe found by this id: " + recipeId));
    }

    @Cacheable(value = "recipe", key = "#name")
    @Transactional(readOnly = true)
    public Recipe getRecipeByName(String name) {
        return recipeRepository.findByName(name)
                .orElse(null);
    }

    private Recipe populateRecipeFields(Recipe recipe, RecipeRequest request) {
        Set<String> matchedTags = new HashSet<>();

        if (request.getIngredients() != null) {
            request.getIngredients().forEach(req -> {
                try {
                    RecipeIngredient ingredient = new RecipeIngredient();
                    ingredient.setName(req.getName());
                    ingredient.setUnit(req.getUnit());
                    ingredient.setQuantity(req.getQuantity());
                    recipe.addIngredient(ingredient);

                    matchedTags.addAll(taggingService.processEntry(
                            ingredient.getName(), TaggingService.CatalogType.INGREDIENT));
                } catch (Exception e) {
                    throw new InvalidIngredientsLineException("Invalid ingredients.");
                }
            });
        }

        if (request.getSteps() != null) {
            request.getSteps().forEach(req -> {
                RecipeStep step = new RecipeStep();
                step.setStepCount(req.getStepCount());
                step.setInstruction(req.getInstruction());
                recipe.addStep(step);

                matchedTags.addAll(taggingService.processEntry(
                        step.getInstruction(), TaggingService.CatalogType.INSTRUCTION
                ));
            });
        }

        if (!matchedTags.isEmpty()) {
            matchedTags.forEach(tagName -> {
                RecipeTag tag = new RecipeTag();
                tag.setTag(tagName);
                recipe.addTag(tag);
            });
        }

        return recipeRepository.save(recipe);
    }

    @CacheEvict(value = {"recipe"}, allEntries = true)
    @Transactional
    public Recipe createRecipe(RecipeRequest request) {
        Recipe recipe = Recipe.builder()
                .name(request.getName())
                .servingSize(request.getServingSize())
                .cookingTimeInMinutes(request.getCookingTimeInMinutes())
                .description(request.getDescription())
                .recipeImageUrl(request.getRecipeImageUrl())
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();
        return populateRecipeFields(recipe, request);
    }

    @CacheEvict(value = {"recipe"}, allEntries = true)
    @Transactional
    public Recipe updateRecipe(RecipeRequest request, Recipe originalRecipe) {
        // DB deletion enabled by orphanRemoval=true in Recipe.java entity
        originalRecipe.getIngredients().clear();
        originalRecipe.getSteps().clear();
        originalRecipe.getTags().clear();

        originalRecipe.setName(request.getName());
        originalRecipe.setServingSize(request.getServingSize());
        originalRecipe.setCookingTimeInMinutes(request.getCookingTimeInMinutes());
        originalRecipe.setDescription(request.getDescription());
        originalRecipe.setUpdatedAt(OffsetDateTime.now());
        originalRecipe.setRecipeImageUrl(request.getRecipeImageUrl());

        return populateRecipeFields(originalRecipe, request);
    }

    @CacheEvict(value = {"recipe"}, allEntries = true)
    @Transactional
    public void deleteRecipe(Long recipeId) {
        recipeRepository.deleteById(recipeId);
        // can also be a way to implement soft deletion.
        // Requires an audit table and a archive/inactive column in the Recipes table in the DB
    }
}
