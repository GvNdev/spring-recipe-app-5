package com.stmc.sfgrecipeapp5.services.impl;

import com.stmc.sfgrecipeapp5.commands.IngredientCommand;
import com.stmc.sfgrecipeapp5.converters.IngredientCommandToIngredient;
import com.stmc.sfgrecipeapp5.converters.IngredientToIngredientCommand;
import com.stmc.sfgrecipeapp5.model.Ingredient;
import com.stmc.sfgrecipeapp5.model.Recipe;
import com.stmc.sfgrecipeapp5.model.UnitOfMeasure;
import com.stmc.sfgrecipeapp5.repositories.RecipeRepository;
import com.stmc.sfgrecipeapp5.repositories.UnitOfMeasureRepository;
import com.stmc.sfgrecipeapp5.repositories.reactive.RecipeReactiveRepository;
import com.stmc.sfgrecipeapp5.repositories.reactive.UnitOfMeasureReactiveRepository;
import com.stmc.sfgrecipeapp5.services.IngredientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {
    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;
    private final RecipeReactiveRepository recipeReactiveRepository;
    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;

    public IngredientServiceImpl(IngredientToIngredientCommand ingredientToIngredientCommand,
                                 IngredientCommandToIngredient ingredientCommandToIngredient,
                                 RecipeReactiveRepository recipeReactiveRepository,
                                 RecipeRepository recipeRepository,
                                 UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository) {
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
        this.recipeReactiveRepository = recipeReactiveRepository;
        this.recipeRepository = recipeRepository;
        this.unitOfMeasureReactiveRepository = unitOfMeasureReactiveRepository;
    }

    @Override
    public Mono<IngredientCommand> findByRecipeIdAndIngredientId(String recipeId, String ingredientId) {
        return recipeReactiveRepository.findById(recipeId)
                .map(recipe -> recipe.getIngredients()
                        .stream()
                        .filter(ingredient -> Objects.equals(ingredient.getId(), ingredientId))
                        .findFirst())
                .filter(Optional::isPresent)
                .map(ingredient -> {
                    IngredientCommand ingredientCommand = ingredientToIngredientCommand.convert(ingredient.get());
                    ingredientCommand.setRecipeId(recipeId);
                    return ingredientCommand;
                });
    }

    @Override
    public Mono<IngredientCommand> saveIngredientCommand(IngredientCommand ingredientCommand) {
        return Mono.defer(() -> {
            if (ingredientCommand == null || !StringUtils.hasText(ingredientCommand.getRecipeId())) {
                return Mono.error(new IllegalArgumentException("Recipe ID is required"));
            }

            Recipe recipe = recipeRepository.findById(ingredientCommand.getRecipeId())
                    .orElseThrow(() -> new IllegalArgumentException("Recipe not found for id: " + ingredientCommand.getRecipeId()));

            if (!StringUtils.hasText(ingredientCommand.getId())) {
                ingredientCommand.setId(UUID.randomUUID().toString());
            }

            Optional<Ingredient> ingredientOptional = recipe
                    .getIngredients()
                    .stream()
                    .filter(ingredient -> Objects.equals(ingredient.getId(), ingredientCommand.getId()))
                    .findFirst();

            UnitOfMeasure unitOfMeasure = null;
            if (ingredientCommand.getUnitOfMeasure() != null
                    && StringUtils.hasText(ingredientCommand.getUnitOfMeasure().getId())) {
                unitOfMeasure = unitOfMeasureReactiveRepository
                        .findById(ingredientCommand.getUnitOfMeasure().getId())
                        .blockOptional()
                        .orElseThrow(() -> new RuntimeException("Unit of measure not found: " +  ingredientCommand.getUnitOfMeasure().getId()));
            }

            if (ingredientOptional.isPresent()) {
                Ingredient ingredientFound = ingredientOptional.get();
                ingredientFound.setDescription(ingredientCommand.getDescription());
                ingredientFound.setAmount(ingredientCommand.getAmount());
                ingredientFound.setUom(unitOfMeasure);
            } else {
                Ingredient ingredient = ingredientCommandToIngredient.convert(ingredientCommand);

                if (!StringUtils.hasText(ingredient.getId())) {
                    ingredient.setId(ingredientCommand.getId());
                }

                ingredient.setUom(unitOfMeasure);
                recipe.addIngredient(ingredient);
            }

            Recipe savedRecipe = recipeRepository.save(recipe);

            Ingredient savedIngredient = savedRecipe.getIngredients().stream()
                    .filter(ingredient -> Objects.equals(ingredient.getId(), ingredientCommand.getId()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Saved ingredient not found. Id: " + ingredientCommand.getId()));

            IngredientCommand savedIngredientCommand = ingredientToIngredientCommand.convert(savedIngredient);
            savedIngredientCommand.setRecipeId(savedRecipe.getId());

            return Mono.just(savedIngredientCommand);
        });
    }

    @Override
    public Mono<Void> deleteById(String recipeId, String idToDelete) {
        log.debug("Deleting ingredient: " + recipeId + ": " + idToDelete);

        Recipe recipe = recipeRepository.findById(recipeId).get();

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
                recipeRepository.save(recipe);
            }
        } else {
            log.debug("Recipe Id not found. Id: " + recipeId);
        }
        return Mono.empty();
    }
}
