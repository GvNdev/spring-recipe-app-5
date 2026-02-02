package com.stmc.sfgrecipeapp5.services;

import com.stmc.sfgrecipeapp5.commands.IngredientCommand;

public interface IngredientService {
    IngredientCommand findByRecipeIdAndIngredientId(String recipeId, String ingredientId);
    IngredientCommand saveIngredientCommand(IngredientCommand ingredientCommand);
    void deleteById(String recipeId, String idToDelete);
}
