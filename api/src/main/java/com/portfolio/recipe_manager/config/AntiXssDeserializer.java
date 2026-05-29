package com.portfolio.recipe_manager.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

import java.io.IOException;

public class AntiXssDeserializer extends JsonDeserializer<String> {
    @Override
    public String deserialize(JsonParser p, DeserializationContext context) throws IOException {
        String value = p.getValueAsString();
        if (value == null) return null;

        String trimmedValue = value.trim();
        if (trimmedValue.isEmpty()) return null;

        // Clean XSS payloads by stripping HTML tags
        return Jsoup.clean(trimmedValue, Safelist.none());
    }
}
