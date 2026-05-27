package com.portfolio.recipe_manager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeSearchRequest {
    String name;
    Integer servingSize;
    String tag;
    String instruction;
    String includeIngredient;
    String excludeIngredient;
}
