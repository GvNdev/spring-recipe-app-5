package com.stmc.sfgrecipeapp5.services.impl;

import com.stmc.sfgrecipeapp5.commands.IngredientCommand;
import com.stmc.sfgrecipeapp5.converters.IngredientCommandToIngredient;
import com.stmc.sfgrecipeapp5.converters.IngredientToIngredientCommand;
import com.stmc.sfgrecipeapp5.model.Ingredient;
import com.stmc.sfgrecipeapp5.model.Recipe;
import com.stmc.sfgrecipeapp5.repositories.reactive.RecipeReactiveRepository;
import com.stmc.sfgrecipeapp5.repositories.reactive.UnitOfMeasureReactiveRepository;
import com.stmc.sfgrecipeapp5.services.IngredientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {
    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;
    private final RecipeReactiveRepository recipeReactiveRepository;
    private final UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;

    public IngredientServiceImpl(IngredientToIngredientCommand ingredientToIngredientCommand,
                                 IngredientCommandToIngredient ingredientCommandToIngredient,
                                 RecipeReactiveRepository recipeReactiveRepository,
                                 UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository) {
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
        this.recipeReactiveRepository = recipeReactiveRepository;
        this.unitOfMeasureReactiveRepository = unitOfMeasureReactiveRepository;
    }

    @Override
    public Mono<IngredientCommand> findByRecipeIdAndIngredientId(String recipeId, String ingredientId) {
        return recipeReactiveRepository.findById(recipeId)
                .flatMapIterable(Recipe::getIngredients)
                .filter(ingredient -> Objects.equals(ingredient.getId(), ingredientId))
                .single()
                .map(ingredient -> {
                    IngredientCommand ingredientCommand = ingredientToIngredientCommand.convert(ingredient);
                    ingredientCommand.setRecipeId(recipeId);
                    return ingredientCommand;
                });
    }

    @Override
    public Mono<IngredientCommand> saveIngredientCommand(IngredientCommand ingredientCommand) {
        Recipe recipe = recipeReactiveRepository.findById(ingredientCommand.getRecipeId()).block();

        if (recipe == null) {
            log.error("Recipe not found for id: " +  ingredientCommand.getRecipeId());
            return Mono.just(new IngredientCommand());
        } else {
            Optional<Ingredient> ingredientOptional = recipe
                    .getIngredients()
                    .stream()
                    .filter(ingredient -> Objects.equals(ingredient.getId(), ingredientCommand.getId()))
                    .findFirst();

            if (ingredientOptional.isPresent()) {
                Ingredient ingredientFound = ingredientOptional.get();
                ingredientFound.setDescription(ingredientCommand.getDescription());
                ingredientFound.setAmount(ingredientCommand.getAmount());
                ingredientFound.setUom(unitOfMeasureReactiveRepository.findById(ingredientCommand.getUnitOfMeasure().getId()).block());

                if (ingredientFound.getUom() == null) {
                    new RuntimeException("Unit of Measure not found");
                }
            } else {
                Ingredient ingredient = ingredientCommandToIngredient.convert(ingredientCommand);
                recipe.addIngredient(ingredient);
            }

            Recipe savedRecipe = recipeReactiveRepository.save(recipe).block();

            Optional<Ingredient> savedIngredientOptional = savedRecipe.getIngredients().stream()
                    .filter(recipeIngredient -> Objects.equals(recipeIngredient.getId(), ingredientCommand.getId()))
                    .findFirst();

            if (!savedIngredientOptional.isPresent()) {
                savedIngredientOptional = savedRecipe.getIngredients().stream()
                        .filter(recipeIngredient -> Objects.equals(recipeIngredient.getDescription(), ingredientCommand.getDescription()))
                        .filter(recipeIngredient -> Objects.equals(recipeIngredient.getAmount(), ingredientCommand.getAmount()))
                        .filter(recipeIngredient -> Objects.equals(recipeIngredient.getUom().getId(), ingredientCommand.getUnitOfMeasure().getId()))
                        .findFirst();
            }

            IngredientCommand savedIngredientCommand = ingredientToIngredientCommand.convert(savedIngredientOptional.get());
            savedIngredientCommand.setRecipeId(recipe.getId());

            return Mono.just(savedIngredientCommand);
        }
    }

    @Override
    public Mono<Void> deleteById(String recipeId, String idToDelete) {
        log.debug("Deleting ingredient: " + recipeId + ": " + idToDelete);

        Recipe recipe = recipeReactiveRepository.findById(recipeId).block();

        if (recipe != null) {
            log.debug("Found recipe");

            Optional<Ingredient> ingredientOptional = recipe
                    .getIngredients()
                    .stream()
                    .filter(ingredient -> Objects.equals(ingredient.getId(), idToDelete))
                    .findFirst();

            if (ingredientOptional.isPresent()) {
                log.debug("Found ingredient");
                recipe.getIngredients().remove(ingredientOptional.get());
                recipeReactiveRepository.save(recipe).block();
            }
        } else {
            log.debug("Recipe Id not found. Id: " + recipeId);
        }
        return Mono.empty();
    }
}
