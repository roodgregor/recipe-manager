package com.portfolio.recipe_manager.repository;

import com.portfolio.recipe_manager.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    // Servings Filter (DQ)
    List<Recipe> findByServingSize(Integer servingSize);

    // Instruction Content Search (e.g. grill, simmer, boil)
    List<Recipe> findDistinctBySteps_InstructionContainingIgnoreCase(String keyword);

    // Include Ingredient Filter (e.g. chicken, spaghetti)
    List<Recipe> findDistinctByIngredients_NameContainingIgnoreCase(String ingredientName);

    // Exclude Ingredient Filter (JPQL Query)
    @Query("SELECT r FROM Recipe r WHERE r.id NOT IN " +
            "(SELECT i.recipe.id FROM RecipeIngredient i WHERE LOWER(i.name) LIKE LOWER(CONCAT('%', :ingredientName, '%')))")
    List<Recipe> findByIngredients_NameExcluding(@Param("ingredientName") String ingredientName);

    // Tag Filter (e.g. vegetarian, vegan) -- dynamically tagged
    List<Recipe> findByTagsContaining(String tag);
}
