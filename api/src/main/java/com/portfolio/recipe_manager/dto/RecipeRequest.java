package com.portfolio.recipe_manager.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeRequest {
    @NotBlank(message = "Recipe name cannot be blank.")
    @Size(max = 255, message = "Text exceeds maximum limit")
    private String name;
    @Min(value = 1, message = "Serving size must be at least 1.")
    @Max(value = 99999, message = "Serving size too large! Are you feeding giants??")
    private Integer servingSize = 1;
    @Min(value = 1, message = "Cooking time in minutes must be at least 1.")
    @Max(value = 99999, message = "Cooking time takes too long! Everyone's hungry!")
    private Integer cookingTimeInMinutes = 1;
    @Size(max = 5000, message = "Recipe description must not exceed 5,000 characters")
    private String description;
    @Size(max = 2500, message = "URL must not exceed 2,500 characters")
    @Pattern(
            regexp = "^(https?://)[^\\s/$.?#].[^\\s]*\\.(jpg|jpeg|png|gif|webp|svg)(\\?.*)?$",
            flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "Recipe Image must be a valid absolute URL (http/https) ending in a standard image format."
    )
    private String recipeImageUrl;
    @NotEmpty(message = "Recipe must have at least one ingredient.")
    private List<IngredientRequest> ingredients;
    @NotEmpty(message = "Recipe must have at least one step.")
    private List<StepRequest> steps;
    private List<TagRequest> tags;
}
