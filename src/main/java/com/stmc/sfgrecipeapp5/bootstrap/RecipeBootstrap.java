package com.stmc.sfgrecipeapp5.bootstrap;

import com.stmc.sfgrecipeapp5.model.*;
import com.stmc.sfgrecipeapp5.repositories.CategoryRepository;
import com.stmc.sfgrecipeapp5.repositories.RecipeRepository;
import com.stmc.sfgrecipeapp5.repositories.UnitOfMeasureRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class RecipeBootstrap implements ApplicationListener<ContextRefreshedEvent> {
    private final CategoryRepository categoryRepository;
    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public RecipeBootstrap(CategoryRepository categoryRepository, RecipeRepository recipeRepository, UnitOfMeasureRepository unitOfMeasureRepository) {
        this.categoryRepository = categoryRepository;
        this.recipeRepository = recipeRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        recipeRepository.saveAll(getRecipes());
    }

    private List<Recipe> getRecipes() {
        List<Recipe> recipes = new ArrayList<>(2);

        Optional<UnitOfMeasure> eachUomOptional = unitOfMeasureRepository.findByDescription("Each");
        if (!eachUomOptional.isPresent()) {
            throw new RuntimeException("'Each' UOM Not found");
        }

        Optional<UnitOfMeasure> tableSpoonUomOptional = unitOfMeasureRepository.findByDescription("Tablespoon");
        if (!tableSpoonUomOptional.isPresent()) {
            throw new RuntimeException("'Tablespoon' UOM Not found");
        }

        Optional<UnitOfMeasure> teaSpoonUomOptional = unitOfMeasureRepository.findByDescription("Teaspoon");
        if (!teaSpoonUomOptional.isPresent()) {
            throw new RuntimeException("'Teaspoon' UOM Not found");
        }

        Optional<UnitOfMeasure> poundUomOptional = unitOfMeasureRepository.findByDescription("Pound");
        if (!poundUomOptional.isPresent()) {
            throw new RuntimeException("'Dash' UOM Not found");
        }

        Optional<UnitOfMeasure> pinchUomOptional = unitOfMeasureRepository.findByDescription("Pinch");
        if (!pinchUomOptional.isPresent()) {
            throw new RuntimeException("'Pint' UOM Not found");
        }

        Optional<UnitOfMeasure> cupUomOptional = unitOfMeasureRepository.findByDescription("Cup");
        if (!cupUomOptional.isPresent()) {
            throw new RuntimeException("'Cup' UOM Not found");
        }

        UnitOfMeasure eachUom = eachUomOptional.get();
        UnitOfMeasure tableSpoonUom = tableSpoonUomOptional.get();
        UnitOfMeasure teaSpoonUom = teaSpoonUomOptional.get();
        UnitOfMeasure poundUom = poundUomOptional.get();
        UnitOfMeasure pinchUom = pinchUomOptional.get();
        UnitOfMeasure cupUom = cupUomOptional.get();

        Optional<Category> mexicanCategoryOptional = categoryRepository.findByDescription("Mexican");
        if (!mexicanCategoryOptional.isPresent()) {
            throw new RuntimeException("'Mexican' Category Not found");
        }

        Optional<Category> dinnersCategoryOptional = categoryRepository.findByDescription("Dinners");
        if (!dinnersCategoryOptional.isPresent()) {
            throw new RuntimeException("'Dinners' Category not found");
        }

        Optional<Category> snacksAndAppetizersCategoryOptional = categoryRepository.findByDescription("Snacks and Appetizers");
        if (!snacksAndAppetizersCategoryOptional.isPresent()) {
            throw new RuntimeException("'Snacks and Appetizers' Category not found");
        }

        Category mexicanCategory = mexicanCategoryOptional.get();
        Category dinnersCategory = dinnersCategoryOptional.get();
        Category snacksAndAppetizersCategory = snacksAndAppetizersCategoryOptional.get();

        Recipe guacamoleRecipe = new Recipe();
        guacamoleRecipe.setDescription("How to Make the Best Guacamole");
        guacamoleRecipe.setPrepTime(10);
        guacamoleRecipe.setCookTime(0);
        guacamoleRecipe.setDifficulty(Difficulty.EASY);
        guacamoleRecipe.setDirections("1. Cut the avocados: Cut the avocados in half. Remove the pit. Score the inside of the avocado with a blunt knife and scoop out the flesh with a spoon. Place in a bowl." +
                "\n" +
                "2. Mash the avocado flesh: Using a fork, roughly mash the avocado. Don't overdo it! The guacamole should be a little chunky." +
                "\n" +
                "3. Add the remaining ingredients to taste: Sprinkle with salt and lime (or lemon) juice. The acid in the lime juice will provide some balance to the richness of the avocado and will help delay the avocados from turning brown. Add the chopped onion, cilantro, black pepper, and chilis. Chili peppers vary individually in their spiciness. So, start with a half of one chili pepper and add more to the guacamole to your desired degree of heat. Remember that much of this is done to taste because of the variability in the fresh ingredients. Start with this recipe and adjust to your taste." +
                "\n" +
                "4. Serve immediately: If making a few hours ahead, place plastic wrap on the surface of the guacamole and press down to cover it to prevent air reaching it. (The oxygen in the air causes oxidation which will turn the guacamole brown.) Garnish with slices of red radish or jicama strips. Serve with your choice of store-bought tortilla chips or make your own homemade tortilla chips. Refrigerate leftover guacamole up to 3 days." +
                "\n" +
                "\n" +
                "Read more: https://www.simplyrecipes.com/recipes/perfect_guacamole/");

        Notes guacamoleNotes = new Notes();
        guacamoleNotes.setRecipeNotes("Chilling tomatoes dulls their flavor. So, if you want to add chopped tomato to your guacamole, add just before serving." +
                "\n" +
                "\n" +
                "Read more: https://www.simplyrecipes.com/recipes/perfect_guacamole/");
        guacamoleRecipe.setNotes(guacamoleNotes);

        guacamoleRecipe.addIngredient(new Ingredient("ripe avocados", new BigDecimal(2), eachUom));
        guacamoleRecipe.addIngredient(new Ingredient("kosher salt, plus more to taste", new BigDecimal(1/4), teaSpoonUom));
        guacamoleRecipe.addIngredient(new Ingredient("fresh lime or lemon juice", new BigDecimal(1), tableSpoonUom));
        guacamoleRecipe.addIngredient(new Ingredient("minced red onion or thinly sliced green onion", new BigDecimal(2), tableSpoonUom));
        guacamoleRecipe.addIngredient(new Ingredient("serrano (or jalapeño) chilis, stems and seeds removed, minced", new BigDecimal(1), eachUom));
        guacamoleRecipe.addIngredient(new Ingredient("cilantro (leaves and tender stems), finely chopped", new BigDecimal(2), tableSpoonUom));
        guacamoleRecipe.addIngredient(new Ingredient("freshly ground black pepper", new BigDecimal(1), pinchUom));
        guacamoleRecipe.addIngredient(new Ingredient("ripe tomato, chopped (optional)", new BigDecimal(1/2), eachUom));
        guacamoleRecipe.addIngredient(new Ingredient("red radish or jicama slices for garnish (optional)", new BigDecimal(1), eachUom));
        guacamoleRecipe.addIngredient(new Ingredient("tortilla chips, to serve", new BigDecimal(1), eachUom));

        guacamoleRecipe.getCategories().add(snacksAndAppetizersCategory);
        guacamoleRecipe.getCategories().add(mexicanCategory);

        recipes.add(guacamoleRecipe);

        Recipe tacosRecipe = new Recipe();
        tacosRecipe.setDescription("Tacos al Pastor");
        tacosRecipe.setPrepTime(30);
        tacosRecipe.setCookTime(60);
        tacosRecipe.setDifficulty(Difficulty.MODERATE);
        tacosRecipe.setDirections("1. Rehydrate the chiles: In a kettle or small saucepan, boil 3 cups water. Meanwhile, heat a large non-stick pan over medium heat. Add the cascabel, ancho, and pasilla chiles, and toast for about 2 minutes on each side until aromatic and slightly softened. Carefully pour the boiled water over the chiles right in the pan and place a large plate on top to make sure the chiles are completely submerged. Let them soak for 20 minutes, until they turn soft." +
                "\n" +
                "2. Prepare the pineapple: Wash the outside of your pineapple with running water. Place the pineapple on a large cutting board. Use a sharp chef’s knife to slice the crown- and root-end off and discard them. Stand the pineapple upright on its flat base so that it doesn’t roll around. Starting from the top, slice off the skin and discard it. With the pineapple still upright, cut off the edible part, working around the core. Discard the core. Cut about one third of the pineapple into large chunks. You’ll add these to the marinade. Cut the remaining pineapple into about 2 x 1/2-inch pieces. Place these in a bowl, cover with plastic wrap, and refrigerate until ready to use. When you’re ready to serve the tacos, you’ll roast these." +
                "\n" +
                "3. Make the adobada marinade: In a blender, transfer the rehydrated chiles—discard the soaking water—and add the pineapple chunks, garlic, achiote paste, oregano, cumin, black peppercorns, vinegar, olive oil, orange juice, and salt. Blend until very smooth." +
                "\n" +
                "4. Marinate the pork: In a large bowl, add the pork shoulder steaks and pour the adobada marinade on top. Use your hands to fully coat the meat with the marinade. Wash your hands and cover the bowl with plastic wrap. Let it marinate in the fridge for at least 3 hours or up to overnight." +
                "\n" +
                "5. Preheat the oven to 400°F." +
                "\n" +
                "6. Sear the pork: Set a large pan with 1 tablespoon olive oil over high heat. You will sear the pork in batches. Once the pan is hot, place 3 steaks in the pan, making sure they don’t overlap. Sprinkle a pinch of salt over each steak. Sear for 2 minutes per side until golden brown and lightly charred. Transfer the meat onto a large baking dish. Sear the remaining pork." +
                "\n" +
                "7. Bake the pork: Pour the adobada marinade used to marinate the pork on top of the seared pork. Cover the baking dish with foil and bake for 20 minutes, until the pork is cooked through. Set it aside to rest while you roast the pineapple." +
                "\n" +
                "8. Roast the pineapple: Transfer the reserved pineapple onto a baking sheet and spread them out into an even layer. Drizzle on 1 tablespoon olive oil and sprinkle with ground ancho chiles and salt. Roast for 15 minutes until the edges are lightly charred." +
                "\n" +
                "9. Meanwhile, warm the tortillas: Warm up your corn tortillas over a hot comal or in a pan in batches, and keep them warm in a tortillero or by wrapping them in a clean tea towel." +
                "\n" +
                "10. Slice the pork: Slice the pork thinly with a very sharp knife and place it on a serving platter. Scrape the marinade on top of the sliced pork." +
                "\n" +
                "11. Assemble the tacos: Bring the sliced pork, roasted pineapples, warm tortillas, chopped onions, cilantro, and lime wedges to the table so that everyone can build their own tacos. For the perfect al pastor taco, add a spoonful of pork into a tortilla, top with a slice or two of pineapple, garnish with onions and cilantro, and then a generous squeeze a lime. Enjoy!" +
                "\n" +
                "\n" +
                "Read more: https://www.simplyrecipes.com/tacos-al-pastor-recipe-5496915");

        Notes tacosNotes =  new Notes();
        tacosNotes.setRecipeNotes("Leftover pork can refrigerate tightly covered for up to 3 days. To reheat, I simply transfer it into a baking dish, cover with foil, and reheat in a 400°F oven for 15 to 20 minutes." +
                "\n" +
                "\n" +
                "Read more: https://www.simplyrecipes.com/tacos-al-pastor-recipe-5496915");
        tacosRecipe.setNotes(tacosNotes);

        tacosRecipe.addIngredient(new Ingredient("water", new BigDecimal(3), cupUom));
        tacosRecipe.addIngredient(new Ingredient("dried cascabel or guajillo chiles, stemmed and deseeded", new BigDecimal(4), eachUom));
        tacosRecipe.addIngredient(new Ingredient("dried ancho chiles, stemmed and deseeded", new BigDecimal(2), eachUom));
        tacosRecipe.addIngredient(new Ingredient("dried pasilla chile, stemmed and deseeded", new BigDecimal(1), eachUom));
        tacosRecipe.addIngredient(new Ingredient("large ripe pineapple", new BigDecimal(1), eachUom));
        tacosRecipe.addIngredient(new Ingredient("cloves garlic", new BigDecimal(3), eachUom));
        tacosRecipe.addIngredient(new Ingredient("achiote paste", new BigDecimal(1), tableSpoonUom));
        tacosRecipe.addIngredient(new Ingredient("Mexican oregano", new BigDecimal(1), tableSpoonUom));
        tacosRecipe.addIngredient(new Ingredient("ground cumin", new BigDecimal(1), teaSpoonUom));
        tacosRecipe.addIngredient(new Ingredient("black peppercorns", new BigDecimal(1), teaSpoonUom));
        tacosRecipe.addIngredient(new Ingredient("apple cider vinegar", new BigDecimal(1/3), cupUom));
        tacosRecipe.addIngredient(new Ingredient("olive oil", new BigDecimal(3), tableSpoonUom));
        tacosRecipe.addIngredient(new Ingredient("orange juice", new BigDecimal(3), tableSpoonUom));
        tacosRecipe.addIngredient(new Ingredient("kosher salt, plus more for cooking the pork", new BigDecimal(1), teaSpoonUom));
        tacosRecipe.addIngredient(new Ingredient("pork shoulder steaks", new BigDecimal(3), poundUom));
        tacosRecipe.addIngredient(new Ingredient("olive oil, divided", new BigDecimal(4), tableSpoonUom));
        tacosRecipe.addIngredient(new Ingredient("ground ancho or guajillo chiles", new BigDecimal(1), tableSpoonUom));
        tacosRecipe.addIngredient(new Ingredient("kosher salt", new BigDecimal(1), teaSpoonUom));
        tacosRecipe.addIngredient(new Ingredient("medium white onion, finely diced", new BigDecimal(1), eachUom));
        tacosRecipe.addIngredient(new Ingredient("small bunch cilantro, finely chopped", new BigDecimal(1), eachUom));
        tacosRecipe.addIngredient(new Ingredient("limes, cut into wedges", new BigDecimal(3), eachUom));
        tacosRecipe.addIngredient(new Ingredient("tortilla corn tortillas", new BigDecimal(4), eachUom));

        tacosRecipe.getCategories().add(dinnersCategory);
        tacosRecipe.getCategories().add(mexicanCategory);

        recipes.add(tacosRecipe);
        return recipes;
    }
}
