package com.stmc.sfgrecipeapp5.repositories.reactive;

import com.stmc.sfgrecipeapp5.model.Recipe;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface RecipeReactiveRepository extends ReactiveMongoRepository<Recipe, String> {
}
