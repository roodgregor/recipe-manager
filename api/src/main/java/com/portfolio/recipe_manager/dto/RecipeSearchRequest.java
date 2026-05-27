package com.portfolio.recipe_manager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeSearchRequest {
    String name;
    Integer servingSize;
    List<String> tags;
    List<String> instructions;
    List<String> includeIngredients;
    List<String> excludeIngredients;
}
