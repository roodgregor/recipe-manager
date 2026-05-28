package com.portfolio.recipe_manager.exception;

public class IncompleteRequiredFieldsException extends RuntimeException {
    public IncompleteRequiredFieldsException(String message) {
        super(message);
    }
}
