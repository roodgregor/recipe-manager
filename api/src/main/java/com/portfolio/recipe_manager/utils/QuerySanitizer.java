package com.portfolio.recipe_manager.utils;

public class QuerySanitizer {
    private QuerySanitizer() {} // Utility
    public static String escapeLike(String input) {
        if (input == null || input.isBlank()) {
            return "";
        }
        // Escape backslash first, then the wildcards
        return input.replace("\\", "\\\\")
                .replace("%", "\\%")
                .replace("_", "\\_");
    }
}
