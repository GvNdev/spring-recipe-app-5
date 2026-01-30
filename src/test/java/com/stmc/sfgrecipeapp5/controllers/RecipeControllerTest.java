package com.stmc.sfgrecipeapp5.controllers;

import com.stmc.sfgrecipeapp5.commands.RecipeCommand;
import com.stmc.sfgrecipeapp5.exceptions.NotFoundException;
import com.stmc.sfgrecipeapp5.model.Recipe;
import com.stmc.sfgrecipeapp5.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RecipeControllerTest {
    @Mock
    RecipeService recipeService;

    RecipeController controller;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        controller = new RecipeController(recipeService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testGetRecipe() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId(1L);

        when(recipeService.findById(anyLong())).thenReturn(recipe);

        mockMvc.perform(get("/recipe/1/show"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/show"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    void getRecipeNotFound() throws Exception {
        when(recipeService.findById(anyLong())).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/recipe/1/show"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("404error"));
    }

    @Test
    void getRecipeNumberFormatException() throws Exception {
        mockMvc.perform(get("/recipe/asdf/show"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("400error"));
    }

    @Test
    public void testGetNewRecipeForm() throws Exception {
        RecipeCommand recipeCommand = new RecipeCommand();

        mockMvc.perform(get("/recipe/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/recipeform"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    void testPostNewRecipeForm() throws Exception {
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(2L);

        when(recipeService.saveRecipeCommand(any())).thenReturn(recipeCommand);

        mockMvc.perform(post("/recipe")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/2/show"));
    }

    @Test
    void testGetUpdateView() throws Exception {
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(2L);

        when(recipeService.findRecipeCommandById(anyLong())).thenReturn(recipeCommand);

        mockMvc.perform(get("/recipe/1/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/recipeform"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    void testDeleteAction() throws Exception {
        mockMvc.perform(get("/recipe/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));

        verify(recipeService, times(1)).deleteById(anyLong());
    }
}