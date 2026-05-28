package com.portfolio.recipe_manager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IngredientRequest {
    @NotNull(message = "Ingredient quantity shouldn't be empty.")
    private BigDecimal quantity;
    @NotBlank(message = "Unit of measure cannot be blank.")
    private String unit;
    @NotBlank(message = "Ingredient name cannot be blank.")
    private String name;
}
