package com.portfolio.recipe_manager.service;

import com.portfolio.recipe_manager.dto.*;
import com.portfolio.recipe_manager.entity.Recipe;
import com.portfolio.recipe_manager.entity.RecipeIngredient;
import com.portfolio.recipe_manager.entity.RecipeStep;
import com.portfolio.recipe_manager.entity.RecipeTag;
import com.portfolio.recipe_manager.exception.RecipeNotFoundException;
import com.portfolio.recipe_manager.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecipeServiceTest {
    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private TaggingService taggingService;

    @InjectMocks
    private RecipeService recipeService;

    private Recipe sampleRecipe;
    private RecipeRequest sampleRequest;

    @BeforeEach
    void setUp() {
        sampleRecipe = new Recipe();
        sampleRecipe.setId(1L);
        sampleRecipe.setName("Chicken Inasal");
        sampleRecipe.setIngredients(new ArrayList<>());
        sampleRecipe.setSteps(new ArrayList<>());
        sampleRecipe.setTags(new ArrayList<>());

        // Reasonable sample request
        sampleRequest = RecipeRequest.builder()
                .name("Chicken Inasal")
                .servingSize(4)
                .cookingTimeInMinutes(20)
                .description("Yummy Chicken Inasal")
                .recipeImageUrl("http://image.url")
                .ingredients(List.of(new IngredientRequest(new BigDecimal("500.0"), "grams", "Beef")))
                .steps(List.of(new StepRequest(1, "Cook the beef")))
                .build();
    }

    // GET Recipe Tests
    @Nested
    @DisplayName("Get Recipe Methods")
    class GetRecipeTests {

        @Test
        @DisplayName("Should return recipe when found by ID")
        void getRecipeById_Success() {
            when(recipeRepository.findById(1L)).thenReturn(Optional.of(sampleRecipe));

            Recipe result = recipeService.getRecipeById(1L);

            assertNotNull(result);
            assertEquals(1L, result.getId());
            verify(recipeRepository, times(1)).findById(1L);
        }

        @Test
        @DisplayName("Should throw RecipeNotFoundException when ID does not exist")
        void getRecipeById_NotFound() {
            when(recipeRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(RecipeNotFoundException.class, () -> recipeService.getRecipeById(1L));
            verify(recipeRepository, times(1)).findById(1L);
        }

        @Test
        @DisplayName("Should return recipe when found by Name")
        void getRecipeByName_Success() {
            when(recipeRepository.findByName("Chicken Inasal")).thenReturn(Optional.of(sampleRecipe));

            Recipe result = recipeService.getRecipeByName("Chicken Inasal");

            assertNotNull(result);
            assertEquals("Chicken Inasal", result.getName());
        }

        @Test
        @DisplayName("Should return null when recipe name does not exist")
        void getRecipeByName_NotFound() {
            when(recipeRepository.findByName("Unknown")).thenReturn(Optional.empty());

            Recipe result = recipeService.getRecipeByName("Unknown");

            assertNull(result);
        }
    }

    // CRUD Tests (except for READ/GET methods covered above)
    @Nested
    @DisplayName("CRUD Methods")
    class MutationTests {

        @Test
        @DisplayName("Should successfully map and save a brand new recipe")
        void createRecipe_Success() {
            when(taggingService.processEntry("Beef", TaggingService.CatalogType.INGREDIENT))
                    .thenReturn(Set.of("Meat"));
            when(taggingService.processEntry("Cook the beef", TaggingService.CatalogType.INSTRUCTION))
                    .thenReturn(Set.of("Cooking"));

            when(recipeRepository.save(any(Recipe.class))).thenAnswer(invocation -> invocation.getArgument(0));

            Recipe created = recipeService.createRecipe(sampleRequest);

            assertNotNull(created);
            assertEquals("Chicken Inasal", created.getName());
            assertEquals(4, created.getServingSize());
            assertNotNull(created.getCreatedAt());

            assertEquals(1, created.getIngredients().size());
            assertEquals(1, created.getSteps().size());
            assertEquals(2, created.getTags().size());

            verify(recipeRepository, times(1)).save(any(Recipe.class));
        }

        @Test
        @DisplayName("Should clear previous relations and update fields when modifying existing recipe")
        void updateRecipe_Success() {
            sampleRecipe.getIngredients().add(new RecipeIngredient());
            sampleRecipe.getSteps().add(new RecipeStep());
            sampleRecipe.getSteps().add(new RecipeStep());
            sampleRecipe.getSteps().add(new RecipeStep());
            sampleRecipe.getTags().add(new RecipeTag());
            sampleRecipe.getTags().add(new RecipeTag());

            when(recipeRepository.save(any(Recipe.class))).thenAnswer(invocation -> invocation.getArgument(0));

            Recipe updated = recipeService.updateRecipe(sampleRequest, sampleRecipe);

            // Since sampleRequest did not change, the added entries to steps and tags to the sampleRecipe should not
            // affect the resulting count.
            assertEquals(1, updated.getIngredients().size());
            assertEquals(1, updated.getSteps().size());
            assertEquals(0, updated.getTags().size());
            verify(recipeRepository).save(sampleRecipe);
        }

        @Test
        @DisplayName("Should delete recipe by ID")
        void deleteRecipe_Success() {
            willDoNothing().given(recipeRepository).deleteById(1L);

            recipeService.deleteRecipe(1L);

            verify(recipeRepository, times(1)).deleteById(1L);
        }

        // Pushing unit test for Search method to a future version. Mocking the FluentQuery is being difficult
        // Especially with the DTO interface of RecipeSearchResult used for lightweight fetch
    }
}
