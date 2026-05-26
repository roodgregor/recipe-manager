package com.portfolio.recipe_manager;

import com.portfolio.recipe_manager.repository.RecipeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RecipeManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecipeManagerApplication.class, args);
	}

//	Mock runner
	@Bean
	public CommandLineRunner testDatabase(RecipeRepository repository) {
		return args -> {
			System.out.println("--FETCHING DATABASE RECIPES--");
			repository.findAll().forEach(recipe -> {
				System.out.println("Recipe found: " + recipe.getName());
				System.out.println("Description: " + recipe.getDescription());
				System.out.println();
			});
			System.out.println("--END DATABASE FETCH--");
		};
	}

}
