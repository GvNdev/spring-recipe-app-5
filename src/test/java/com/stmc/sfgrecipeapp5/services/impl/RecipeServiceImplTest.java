package com.stmc.sfgrecipeapp5.services.impl;

import com.stmc.sfgrecipeapp5.commands.RecipeCommand;
import com.stmc.sfgrecipeapp5.converters.RecipeCommandToRecipe;
import com.stmc.sfgrecipeapp5.converters.RecipeToRecipeCommand;
import com.stmc.sfgrecipeapp5.exceptions.NotFoundException;
import com.stmc.sfgrecipeapp5.model.Recipe;
import com.stmc.sfgrecipeapp5.repositories.reactive.RecipeReactiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecipeServiceImplTest {
    RecipeServiceImpl recipeService;

    @Mock
    RecipeReactiveRepository recipeReactiveRepository;

    @Mock
    RecipeToRecipeCommand recipeToRecipeCommand;

    @Mock
    RecipeCommandToRecipe recipeCommandToRecipe;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        recipeService = new RecipeServiceImpl(recipeReactiveRepository, recipeCommandToRecipe, recipeToRecipeCommand);
    }

    @Test
    void getRecipeById() {
        Recipe recipe = new Recipe();
        recipe.setId("1");

        when(recipeReactiveRepository.findById(anyString())).thenReturn(Mono.just(recipe));

        Recipe recipeReturned = recipeService.findById("1").block();

        assertNotNull(recipeReturned, "Null recipe returned");
        verify(recipeReactiveRepository, times(1)).findById(anyString());
        verify(recipeReactiveRepository, never()).findAll();
    }

    @Test
    void getRecipeCommandById() {
        Recipe recipe = new Recipe();
        recipe.setId("1");

        when(recipeReactiveRepository.findById(anyString())).thenReturn(Mono.just(recipe));

        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId("1");

        when(recipeToRecipeCommand.convert(any())).thenReturn(recipeCommand);

        RecipeCommand recipeCommandById = recipeService.findRecipeCommandById("1").block();

        assertNotNull(recipeCommandById, "Null recipe command returned");
        verify(recipeReactiveRepository, times(1)).findById(anyString());
        verify(recipeReactiveRepository, never()).findAll();
    }

    @Test
    void getRecipes() {
        Recipe recipe = new Recipe();
        HashSet recipesData = new HashSet();
        recipesData.add(recipe);

        when(recipeService.getRecipes()).thenReturn(Flux.just(recipe));

        List<Recipe> recipes = recipeService.getRecipes().collectList().block();

        assertEquals(recipes.size(), 1);
        verify(recipeReactiveRepository, times(1)).findAll();
        verify(recipeReactiveRepository, never()).findById(anyString());
    }

    @Test
    void deleteById() {
        // Given
        String idToDelete = "2";

        when(recipeReactiveRepository.deleteById(anyString())).thenReturn(Mono.empty());

        // When
        recipeService.deleteById(idToDelete);

        // Then
        verify(recipeReactiveRepository, times(1)).deleteById(anyString());
    }
}