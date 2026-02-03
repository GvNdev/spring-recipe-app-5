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
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
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
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    public void testGetRecipe() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId("1");

        when(recipeService.findById(anyString())).thenReturn(Mono.just(recipe));

        mockMvc.perform(get("/recipe/1/show"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/show"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    void getRecipeNotFound() throws Exception {
        when(recipeService.findById(anyString())).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/recipe/1/show"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("404error"));
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
        recipeCommand.setId("2");

        when(recipeService.saveRecipeCommand(any())).thenReturn(Mono.just(recipeCommand));

        mockMvc.perform(post("/recipe")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", "")
                        .param("description", "some string")
                        .param("directions", "some directions")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/2/show"));
    }

    @Test
    void testPostNewRecipeFormValidationFail() throws Exception {
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId("2");

        when(recipeService.saveRecipeCommand(any())).thenReturn(Mono.just(recipeCommand));

        mockMvc.perform(post("/recipe")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "")
                        .param("cookTime", "3000")
        )
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"))
                .andExpect(view().name("recipe/recipeform"));
    }

    @Test
    void testGetUpdateView() throws Exception {
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId("2");

        when(recipeService.findRecipeCommandById(anyString())).thenReturn(Mono.just(recipeCommand));

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

        verify(recipeService, times(1)).deleteById(anyString());
    }
}