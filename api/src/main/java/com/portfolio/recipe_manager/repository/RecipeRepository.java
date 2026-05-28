package com.portfolio.recipe_manager.repository;

import com.portfolio.recipe_manager.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long>, JpaSpecificationExecutor<Recipe> {
    // Criteria defined in /specification/RecipeSpecifications.java
    Optional<Recipe> findByName(String name);
}
