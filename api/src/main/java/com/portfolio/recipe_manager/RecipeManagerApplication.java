package com.portfolio.recipe_manager;

import com.portfolio.recipe_manager.dto.RecipeSearchRequest;
import com.portfolio.recipe_manager.repository.RecipeRepository;
import com.portfolio.recipe_manager.service.RecipeService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@SpringBootApplication
public class RecipeManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecipeManagerApplication.class, args);
	}

//	Mock runner
	@Bean
	public CommandLineRunner testDatabase(RecipeService recipeService) {
		Pageable pageable = PageRequest.of(0, 25, Sort.by("id").ascending());
		RecipeSearchRequest req = RecipeSearchRequest.builder()
				.name("chIckEn")
				.build();
		return args -> {
			System.out.println("--FETCHING DATABASE RECIPES--");
			System.out.println();
			recipeService.searchRecipes(req, pageable).forEach(recipe -> {
				System.out.println("Recipe ID: " + recipe.getId());
				System.out.println("Name: " + recipe.getName());
				System.out.println("Description: " + recipe.getDescription());
				System.out.println();
			});
			System.out.println("--END DATABASE FETCH--");
		};
	}

}
