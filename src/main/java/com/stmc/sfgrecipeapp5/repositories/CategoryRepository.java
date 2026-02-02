package com.stmc.sfgrecipeapp5.repositories;

import com.stmc.sfgrecipeapp5.model.Category;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CategoryRepository extends CrudRepository<Category, String> {
    Optional<Category> findByDescription(String description);
}
