package com.stmc.sfgrecipeapp5.controllers;

import com.stmc.sfgrecipeapp5.commands.IngredientCommand;
import com.stmc.sfgrecipeapp5.commands.RecipeCommand;
import com.stmc.sfgrecipeapp5.commands.UnitOfMeasureCommand;
import com.stmc.sfgrecipeapp5.services.IngredientService;
import com.stmc.sfgrecipeapp5.services.RecipeService;
import com.stmc.sfgrecipeapp5.services.UnitOfMeasureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
public class IngredientController {
    private final IngredientService ingredientService;
    private final RecipeService recipeService;
    private final UnitOfMeasureService unitOfMeasureService;

    public IngredientController(IngredientService ingredientService, RecipeService recipeService, UnitOfMeasureService unitOfMeasureService) {
        this.ingredientService = ingredientService;
        this.recipeService = recipeService;
        this.unitOfMeasureService = unitOfMeasureService;
    }

    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredients")
    public String listIngredients(@PathVariable("recipeId") String recipeId, Model model) {
        log.debug("Getting ingredient list for recipe id: " + recipeId);

        model.addAttribute("recipe", recipeService.findRecipeCommandById(Long.valueOf(recipeId)));
        return "recipe/ingredient/list";
    }

    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredient/{id}/show")
    public String showRecipeIngredient(@PathVariable("recipeId") String recipeId,
                                       @PathVariable("id") String id, Model model) {
        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(Long.valueOf(recipeId), Long.valueOf(id)));
        return "recipe/ingredient/show";
    }

    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredient/new")
    public String newRecipeIngredient(@PathVariable("recipeId") String recipeId, Model model) {
        RecipeCommand  recipeCommand = recipeService.findRecipeCommandById(Long.valueOf(recipeId));

        // todo raise exception if null

        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId(Long.valueOf(recipeId));
        ingredientCommand.setUnitOfMeasure(new UnitOfMeasureCommand());
        model.addAttribute("ingredient", ingredientCommand);
        model.addAttribute("uomList", unitOfMeasureService.listAllUnitOfMeasures());
        return "recipe/ingredient/ingredientform";
    }

    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredient/{id}/update")
    public String updateRecipeIngredient(@PathVariable("recipeId") String recipeId,
                                         @PathVariable("id") String id, Model model) {
        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(Long.valueOf(recipeId), Long.valueOf(id)));
        model.addAttribute("uomList", unitOfMeasureService.listAllUnitOfMeasures());
        return "recipe/ingredient/ingredientform";
    }

    @PostMapping
    @RequestMapping("/recipe/{recipeId}/ingredient")
    public String saveOrUpdate(@ModelAttribute IngredientCommand ingredientCommand) {
        IngredientCommand savedIngredientCommand = ingredientService.saveIngredientCommand(ingredientCommand);
        log.debug("Saved recipe id: " + savedIngredientCommand.getRecipeId());
        log.debug("Saved ingredient id: " + savedIngredientCommand.getId());
        return "redirect:/recipe/" + savedIngredientCommand.getRecipeId() + "/ingredient/" + savedIngredientCommand.getId() + "/show";
    }
}
