package com.stmc.sfgrecipeapp5.services;

import com.stmc.sfgrecipeapp5.commands.RecipeCommand;
import com.stmc.sfgrecipeapp5.model.Recipe;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RecipeService {
    Flux<Recipe> getRecipes();
    Mono<Recipe> findById(String id);
    Mono<RecipeCommand> findRecipeCommandById(String id);
    Mono<RecipeCommand> saveRecipeCommand(RecipeCommand recipeCommand);
    void deleteById(String idToDelete);
}
