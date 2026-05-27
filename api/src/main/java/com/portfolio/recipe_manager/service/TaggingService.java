package com.portfolio.recipe_manager.service;

import com.portfolio.recipe_manager.utils.TagCatalog;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TaggingService {
    public enum CatalogType {INGREDIENT, INSTRUCTION}

    // Split into words
    private Set<String> tokenize(String text) {
        return Arrays.stream(text.toLowerCase().split("[\\s\\p{Punct}]+"))
                .collect(Collectors.toSet());
    }

    public Set<String> processEntry(String text, CatalogType type) {
        if (text == null || text.isBlank()) {
            return Collections.emptySet();
        }

        Set<String> tokens = tokenize(text);

        Map<String, List<String>> catalog = (type == CatalogType.INGREDIENT)
                ? TagCatalog.INGREDIENT_TAGS
                : TagCatalog.INSTRUCTION_TAGS;

        Set<String> matchedTags = new HashSet<>();
        catalog.forEach((tag, keywords) -> {
            boolean hasMatch = keywords.stream().anyMatch(tokens::contains);
            if (hasMatch) matchedTags.add(tag);
        });

        return matchedTags;
    }
}
