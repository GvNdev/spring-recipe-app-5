package com.stmc.sfgrecipeapp5.repositories;

import com.stmc.sfgrecipeapp5.model.Recipe;
import org.springframework.data.repository.CrudRepository;

public interface RecipeRepository extends CrudRepository<Recipe, Long> {
}
