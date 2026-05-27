package com.portfolio.recipe_manager.utils;

import java.util.List;
import java.util.Map;

public class TagCatalog {
    // Ingredients map: Tag -> List of triggering words
    public static final Map<String, List<String>> INGREDIENT_TAGS = Map.of(
            "Vegan", List.of("tofu", "tempeh", "avocado", "almond milk", "chickpeas"),
            "Dairy-Free", List.of("oat milk", "coconut oil", "olive oil", "almond milk"),
            "Seafood", List.of("salmon", "tuna", "shrimp", "crab", "cod", "tilapia"),
            "Vegetarian", List.of("tofu", "tempeh", "lentils", "chickpeas", "paneer", "eggplant", "mushrooms", "squash"),
            "Pescatarian", List.of("salmon", "tuna", "shrimp", "cod", "tilapia", "trout", "halibut", "haddock", "scallops")
    );

    // Instructions map: Tag -> List of triggering words
    public static final Map<String, List<String>> INSTRUCTION_TAGS = Map.of(
            "Baking", List.of("bake", "preheat", "oven", "knead", "dough"),
            "One-Pot", List.of("slow cooker", "instant pot", "skillet", "same pot"),
            "Fried & Crispy", List.of("fry", "deep-fry", "sear", "crisp", "air-fry"),
            "No-Cook", List.of("toss", "chill", "blend", "assemble", "raw"),
            "Grilled", List.of("grill", "charcoal", "grates", "griddled", "bbq", "barbecue"),
            "Soup", List.of("soup", "broth", "stock", "simmer", "chowder", "stew", "bouillon", "ladle")
    );
}
