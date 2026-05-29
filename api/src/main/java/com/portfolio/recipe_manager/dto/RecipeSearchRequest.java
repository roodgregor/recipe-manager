package com.portfolio.recipe_manager.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeSearchRequest {
    @Size(max = 100, message = "Search term is too long!")
    String name;
    @Min(value = 1, message = "Serving size must be at least 1.")
    @Max(value = 99999, message = "Serving size too large! Are you feeding giants??")
    Integer servingSize;
    List<String> tags;
    List<String> instructions;
    List<String> includeIngredients;
    List<String> excludeIngredients;
}
