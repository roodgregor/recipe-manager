package com.portfolio.recipe_manager.exception;

public class ExistingRecipeException extends RuntimeException {
    public ExistingRecipeException(String message) {
        super(message);
    }
}
