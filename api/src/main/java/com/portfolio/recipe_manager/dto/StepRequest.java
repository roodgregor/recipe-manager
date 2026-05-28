package com.portfolio.recipe_manager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StepRequest {
    @NotNull(message = "Step count must be provided.")
    private Integer stepCount;
    @NotBlank(message = "Instruction step cannot be blank.")
    private String instruction;
}
