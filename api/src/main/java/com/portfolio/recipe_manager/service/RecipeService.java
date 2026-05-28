package com.portfolio.recipe_manager.service;

import com.portfolio.recipe_manager.dto.RecipeRequest;
import com.portfolio.recipe_manager.dto.RecipeSearchRequest;
import com.portfolio.recipe_manager.dto.RecipeSearchResult;
import com.portfolio.recipe_manager.entity.*;
import com.portfolio.recipe_manager.exception.DuplicateStepException;
import com.portfolio.recipe_manager.exception.RecipeNotFoundException;
import com.portfolio.recipe_manager.repository.RecipeRepository;
import com.portfolio.recipe_manager.repository.specification.RecipeSpecifications;
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

    @Transactional(readOnly = true)
    public Page<RecipeSearchResult> searchRecipes(
            RecipeSearchRequest request,
            Pageable pageable) {
        Specification<Recipe> spec = Specification.unrestricted();

        if (request.getName() != null && !request.getName().isBlank()) {
            Specification<Recipe> nameAndDescriptionSpec = RecipeSpecifications.nameContains(request.getName())
                    .or(RecipeSpecifications.descriptionContains(request.getName()));
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

    @Transactional(readOnly = true)
    public Recipe getRecipeById(Long recipeId) {
        return recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RecipeNotFoundException("No recipe found by this id: " + recipeId));
    }

    private Recipe populateRecipeFields(Recipe recipe, RecipeRequest request) {
        Set<String> matchedTags = new HashSet<>();

        if (request.getIngredients() != null) {
            request.getIngredients().forEach(req -> {
                RecipeIngredient ingredient = new RecipeIngredient();
                ingredient.setName(req.getName());
                ingredient.setUnit(req.getUnit());
                ingredient.setQuantity(req.getQuantity());
                recipe.addIngredient(ingredient);

                matchedTags.addAll(taggingService.processEntry(
                        ingredient.getName(), TaggingService.CatalogType.INGREDIENT));
            });
        }

        if (request.getSteps() != null) {
            Set<Integer> stepCountSet = new HashSet<>();
            request.getSteps().forEach(req -> {
                Integer stepCount = req.getStepCount();
                if (stepCountSet.contains(stepCount)) {
                    throw new DuplicateStepException("This recipe already has a step " + stepCount + "." +
                            "Ensure instruction steps are sequential.");
                }
                stepCountSet.add(stepCount);
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

    @Transactional
    public Recipe createRecipe(RecipeRequest request) {
        Recipe recipe = Recipe.builder()
                .name(request.getName())
                .servingSize(request.getServingSize())
                .cookingTimeInMinutes(request.getCookingTimeInMinutes())
                .description(request.getDescription())
                .build();
        return populateRecipeFields(recipe, request);
    }

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

        return populateRecipeFields(originalRecipe, request);
    }

    @Transactional
    public void deleteRecipe(Long recipeId) {
        recipeRepository.deleteById(recipeId);
        // can also be a way to implement soft deletion.
        // Requires an audit table and a archive/inactive column in the Recipes table in the DB
    }
}
