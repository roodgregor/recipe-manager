package com.portfolio.recipe_manager.repository;

import com.portfolio.recipe_manager.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long>, JpaSpecificationExecutor<Recipe> {
    // Criteria defined in /specification/RecipeSpecifications.java
    // Left completely empty and used default .findBy() method with a Specification, Pageable, and conversion to
    // RecipeSearchResult for lightweight results list
}
