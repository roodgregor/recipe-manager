package com.portfolio.recipe_manager.repository.specification;

import com.portfolio.recipe_manager.entity.Recipe;
import com.portfolio.recipe_manager.entity.RecipeIngredient;
import com.portfolio.recipe_manager.entity.RecipeStep;
import com.portfolio.recipe_manager.entity.RecipeTag;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class RecipeSpecifications {
    // Find recipe by name
    public static Specification<Recipe> nameContains(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), '%' + name.toLowerCase() + '%');
    }

    public static Specification<Recipe> descriptionContains(String keyword) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), '%' + keyword.toLowerCase() + '%');
    }

    // Find by exact serving size
    public static Specification<Recipe> hasServingSize(Integer servingSize) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("servingSize"), servingSize);
    }

    // Filter by dynamic tagging (join to RecipeTag through tags list)
    public static Specification<Recipe> hasTag(String tagName) {
        return (root, query, criteriaBuilder) -> {
            Join<Recipe, RecipeTag> tagJoin = root.join("tags");
            return criteriaBuilder.equal(criteriaBuilder.lower(tagJoin.get("tag")), tagName.toLowerCase());
        };
    }

    // Contains instruction step
    public static Specification<Recipe> instructionContains(String keyword) {
        return (root, query, criteriaBuilder) -> {
            Join<Recipe, RecipeStep> stepJoin = root.join("steps");
            return criteriaBuilder.like(criteriaBuilder.lower(stepJoin.get("instruction")), '%' + keyword.toLowerCase() + '%');
        };
    }

    // Includes ingredient
    public static Specification<Recipe> includesIngredient(String ingredient) {
        return (root, query, criteriaBuilder) -> {
            Join<Recipe, RecipeIngredient> ingredientJoin = root.join("ingredients");
            return criteriaBuilder.like(criteriaBuilder.lower(ingredientJoin.get("name")), '%' + ingredient.toLowerCase() + '%');
        };
    }

    // Exclude ingredient
    public static Specification<Recipe> excludesIngredient(String ingredient) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true); //DISTINCT for non-duplicate
            var subquery = query.subquery(Long.class);
            var subRoot = subquery.from(Recipe.class);
            Join<Recipe, RecipeIngredient> subIngredientJoin = subRoot.join("ingredients");

            //subquery to fetch matches ingredient, like prev method
            subquery.select(subRoot.get("id")).where(
                    criteriaBuilder.like(criteriaBuilder.lower(subIngredientJoin.get("name")), '%' + ingredient.toLowerCase() + '%')
            );

            return criteriaBuilder.not(root.get("id").in(subquery));
        };
    }

}
