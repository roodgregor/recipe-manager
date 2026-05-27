package com.portfolio.recipe_manager.controller;

import com.portfolio.recipe_manager.dto.RecipeRequest;
import com.portfolio.recipe_manager.dto.RecipeSearchRequest;
import com.portfolio.recipe_manager.entity.Recipe;
import com.portfolio.recipe_manager.dto.RecipeSearchResult;
import com.portfolio.recipe_manager.exception.RecipeNotFoundException;
import com.portfolio.recipe_manager.service.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    // Example call:
    // GET /api/v1/recipes?name=pasta&tag=vegetarian&includedIngredients=tomato,garlic
    @GetMapping
    @Operation(summary = "Search recipes with dynamic filters.",
        description = "Retrieves a paginated list of recipe summaries tailored to your search criteria and" +
                " selected ingredient filters.")
    public ResponseEntity<Page<RecipeSearchResult>> searchRecipes(
            @ParameterObject @ModelAttribute RecipeSearchRequest request,
            @PageableDefault(size = 25, sort = "id") Pageable pageable) {
        Page<RecipeSearchResult> results = recipeService.searchRecipes(request, pageable);
        return ResponseEntity.ok(results);
    }

    // GET /api/v1/recipes/1
    @GetMapping("/{id}")
    @Operation(summary = "Retrieve a specific recipe by ID.",
        description = "Fetches complete details for a single recipe, including preparation steps, ingredients, " +
                "and dynamically assigned tags.")
    public ResponseEntity<Recipe> fetchById(@PathVariable Long id) {
        Recipe recipe = recipeService.getRecipeById(id);
        return ResponseEntity.ok(recipe);
    }

    //POST /api/v1/recipes
    @PostMapping
    @Operation(summary = "Creates a new recipe entry.",
        description = "Registers a new recipe in the system, automatically processing its associated steps, " +
                "ingredients, and descriptive tags.")
    public ResponseEntity<Recipe> createRecipe(@Valid @RequestBody RecipeRequest recipeCreateRequest) {
        Recipe newRecipe = recipeService.createRecipe(recipeCreateRequest);
        return new ResponseEntity<>(newRecipe, HttpStatus.CREATED);
    }

    //PUT /api/v1/recipes/1
    @PutMapping("/{id}")
    @Operation(summary = "Updates an existing recipe",
        description = "Modifies the details of a specific recipe, updating its configuration, steps, " +
                "ingredients, and system tags.")
    public ResponseEntity<Recipe> updateRecipe(
            @PathVariable Long id,
            @Valid @RequestBody RecipeRequest recipeUpdateRequest) {
        Recipe oldRecipe = recipeService.getRecipeById(id);
        if (oldRecipe == null) {
            throw new RecipeNotFoundException("Recipe with ID (" + id + ") not found." +
                    "Verify correct Recipe ID to trigger proper update.");
        }
        Recipe updatedRecipe = recipeService.updateRecipe(recipeUpdateRequest, oldRecipe);
        return ResponseEntity.ok(updatedRecipe);
    }

    // DELETE /api/v1/recipes/2
    @DeleteMapping("/{id}")
    @Operation(summary = "Removes a recipe from the system.",
        description = "Permanently deletes a specific recipe profile along with all associated historical data" +
                " and details.")
    public ResponseEntity<Void> deleteRecipe(
            @PathVariable Long id) {
        recipeService.deleteRecipe(id);
        return ResponseEntity.noContent().build();
    }
}
