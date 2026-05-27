package com.portfolio.recipe_manager.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RecipeRequest {
    @NotBlank(message = "Recipe name cannot be blank.")
    private String name;
    @Min(value = 1, message = "Serving size must be at least 1.")
    private Integer servingSize = 1;
    @Min(value = 1, message = "Cooking time in minutes must be at least 1.")
    private Integer cookingTimeInMinutes = 1;
    private String description;
    @NotEmpty(message = "Recipe must have at least one ingredient.")
    private List<IngredientRequest> ingredients;
    @NotEmpty(message = "Recipe must have at least one step.")
    private List<StepRequest> steps;
    private List<TagRequest> tags;
}
