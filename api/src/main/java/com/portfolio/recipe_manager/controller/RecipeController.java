package com.portfolio.recipe_manager.controller;

import com.portfolio.recipe_manager.dto.RecipeSearchRequest;
import com.portfolio.recipe_manager.entity.RecipeSearchResult;
import com.portfolio.recipe_manager.service.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
