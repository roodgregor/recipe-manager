package com.portfolio.recipe_manager.controller;

import com.portfolio.recipe_manager.dto.RecipeCreateRequest;
import com.portfolio.recipe_manager.dto.RecipeSearchRequest;
import com.portfolio.recipe_manager.entity.Recipe;
import com.portfolio.recipe_manager.entity.RecipeSearchResult;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

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
    @Operation(summary = "Search recipes with dynamic, aggregate filters based on user input",
        description = "Returns a paginated list of recipe summaries matching the optional criteria defined.")
    public ResponseEntity<Page<RecipeSearchResult>> searchRecipes(
            @ParameterObject @ModelAttribute RecipeSearchRequest request,
            @PageableDefault(size = 25, sort = "id") Pageable pageable) {
        Page<RecipeSearchResult> results = recipeService.searchRecipes(request, pageable);
        return ResponseEntity.ok(results);
    }

    // GET /api/v1/recipes/1
    @GetMapping("/{id}")
    @Operation(summary = "Fetches the full details of a specific recipe",
        description = "Returns a single recipe based on the specific ID provided, including steps, ingredients, and" +
                "dynamically added tags.")
    public ResponseEntity<Recipe> fetchById(@PathVariable Long id) {
        Recipe recipe = recipeService.getRecipeById(id);
        return ResponseEntity.ok(recipe);
    }

    //POST /api/v1/recipes
    @PostMapping
    @Operation(summary = "Creates a new recipe",
        description = "Converts the JSON payload into a new recipe, along with its steps, ingredients, and dynamic tags.")
    public ResponseEntity<Recipe> createRecipe(@Valid @RequestBody RecipeCreateRequest recipeCreateRequest) {
        Recipe newRecipe = recipeService.createRecipe(recipeCreateRequest);
        return new ResponseEntity<>(newRecipe, HttpStatus.CREATED);
    }
}
