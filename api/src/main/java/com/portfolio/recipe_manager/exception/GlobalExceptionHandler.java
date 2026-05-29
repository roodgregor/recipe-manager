package com.portfolio.recipe_manager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RecipeNotFoundException.class)
    public ResponseEntity<Object> recipeNotFound(RecipeNotFoundException ex) {
        // Custom exception: Used for checking a recipe before Updating and Deleting
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ExistingRecipeException.class)
    public ResponseEntity<Object> duplicateRecipeFound(ExistingRecipeException ex) {
        // Custom exception: Used for checking a recipe with the same name already exists upon creation.
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(IncompleteRequiredFieldsException.class)
    public ResponseEntity<Object> incompleteRequiredFields(IncompleteRequiredFieldsException ex) {
        // Caught by UI validation in latest version, but kept here just in case the controller function is extended.
        // Case: A required field is missing from JSON payload if the API is used not through the UI.
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(InvalidIngredientsLineException.class)
    public ResponseEntity<Object> incompleteRequiredFields(InvalidIngredientsLineException ex) {
        // Custom exception: similar to HttpMessageNotReadableException, but more specific to Ingredients
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(InvalidFieldValueException.class)
    public ResponseEntity<Object> invalidFieldValue(InvalidFieldValueException ex) {
        // Custom exception: Used for invalid field values, namely a negative quantity on ingredients, serving size,
        // or cooking time in minutes.
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageExceptions(HttpMessageNotReadableException ex) {
        // Caught by UI validation in latest version, but kept here just in case.
        // Case: Ingredients field does not follow input pattern and passes "asdf" to when a line is expected
        // to follow the following pattern: "10 unit ingredient"
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
