package com.stmc.sfgrecipeapp5.repositories.reactive;

import com.stmc.sfgrecipeapp5.model.UnitOfMeasure;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface UnitOfMeasureReactiveRepository extends ReactiveMongoRepository<UnitOfMeasure, String> {
}
