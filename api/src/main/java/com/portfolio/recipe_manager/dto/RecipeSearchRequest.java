package com.portfolio.recipe_manager.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
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
