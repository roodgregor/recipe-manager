package com.portfolio.recipe_manager.service;

import com.portfolio.recipe_manager.dto.RecipeSearchRequest;
import com.portfolio.recipe_manager.entity.Recipe;
import com.portfolio.recipe_manager.entity.RecipeSearchResult;
import com.portfolio.recipe_manager.repository.RecipeRepository;
import com.portfolio.recipe_manager.repository.specification.RecipeSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Transactional(readOnly = true)
    public Page<RecipeSearchResult> searchRecipes(
            RecipeSearchRequest request,
            Pageable pageable) {
        Specification<Recipe> spec = Specification.unrestricted();

        if (request.getName() != null && !request.getName().isBlank()) {
            spec = spec.and(RecipeSpecifications.nameContains(request.getName()));
        }
        if (request.getServingSize() != null) {
            spec = spec.and(RecipeSpecifications.hasServingSize(request.getServingSize()));
        }
        if (request.getTag() != null && !request.getTag().isBlank()) {
            spec = spec.and(RecipeSpecifications.hasTag(request.getTag()));
        }
        if (request.getInstruction() != null && !request.getInstruction().isBlank()) {
            spec = spec.and(RecipeSpecifications.instructionContains(request.getInstruction()));
        }
        if (request.getIncludeIngredient() != null && !request.getIncludeIngredient().isBlank()) {
            spec = spec.and(RecipeSpecifications.includesIngredient(request.getIncludeIngredient()));
        }
        if (request.getExcludeIngredient() != null && !request.getExcludeIngredient().isBlank()) {
            spec = spec.and(RecipeSpecifications.excludesIngredient(request.getExcludeIngredient()));
        }

        return recipeRepository.findBy(spec, query -> query
                .as(RecipeSearchResult.class)
                .page(pageable));
    }
}
