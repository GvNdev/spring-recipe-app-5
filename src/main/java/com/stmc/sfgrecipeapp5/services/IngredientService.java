package com.stmc.sfgrecipeapp5.services;

import com.stmc.sfgrecipeapp5.commands.IngredientCommand;

public interface IngredientService {
    IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId);
}
