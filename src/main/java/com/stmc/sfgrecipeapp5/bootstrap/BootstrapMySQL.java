package com.stmc.sfgrecipeapp5.bootstrap;

import com.stmc.sfgrecipeapp5.model.Category;
import com.stmc.sfgrecipeapp5.model.UnitOfMeasure;
import com.stmc.sfgrecipeapp5.repositories.CategoryRepository;
import com.stmc.sfgrecipeapp5.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile({"dev", "prod"})
public class BootstrapMySQL implements ApplicationListener<ContextRefreshedEvent> {
    private final CategoryRepository categoryRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public BootstrapMySQL(CategoryRepository categoryRepository, UnitOfMeasureRepository unitOfMeasureRepository) {
        this.categoryRepository = categoryRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (categoryRepository.count() == 0L) {
            log.debug("Loading Categories");
            loadCategories();
        }

        if (unitOfMeasureRepository.count() == 0L) {
            log.debug("Loading Units of Measure");
            loadUnitsOfMeasure();
        }
    }

    private void loadCategories() {
        Category category1 = new Category();
        category1.setDescription("American");
        categoryRepository.save(category1);

        Category category2 = new Category();
        category2.setDescription("Italian");
        categoryRepository.save(category2);

        Category category3 = new Category();
        category3.setDescription("Mexican");
        categoryRepository.save(category3);

        Category category4 = new Category();
        category4.setDescription("Fast Food");
        categoryRepository.save(category4);

        Category category5 = new Category();
        category5.setDescription("Dinners");
        categoryRepository.save(category5);

        Category category6 = new Category();
        category6.setDescription("Snacks and Appetizers");
        categoryRepository.save(category6);
    }

    private void loadUnitsOfMeasure() {
        UnitOfMeasure uom1 = new UnitOfMeasure();
        uom1.setDescription("Each");
        unitOfMeasureRepository.save(uom1);

        UnitOfMeasure uom2 = new UnitOfMeasure();
        uom2.setDescription("Tablespoon");
        unitOfMeasureRepository.save(uom2);

        UnitOfMeasure uom3 = new UnitOfMeasure();
        uom3.setDescription("Teaspoon");
        unitOfMeasureRepository.save(uom3);

        UnitOfMeasure uom4 = new UnitOfMeasure();
        uom4.setDescription("Dash");
        unitOfMeasureRepository.save(uom4);

        UnitOfMeasure uom5 = new UnitOfMeasure();
        uom5.setDescription("Pint");
        unitOfMeasureRepository.save(uom5);

        UnitOfMeasure uom6 = new UnitOfMeasure();
        uom6.setDescription("Cup");
        unitOfMeasureRepository.save(uom6);

        UnitOfMeasure uom7 = new UnitOfMeasure();
        uom7.setDescription("Pinch");
        unitOfMeasureRepository.save(uom7);

        UnitOfMeasure uom8 = new UnitOfMeasure();
        uom8.setDescription("Ounce");
        unitOfMeasureRepository.save(uom8);

        UnitOfMeasure uom9 = new UnitOfMeasure();
        uom9.setDescription("Pound");
        unitOfMeasureRepository.save(uom9);
    }
}
