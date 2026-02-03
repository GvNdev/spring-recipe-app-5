package com.stmc.sfgrecipeapp5.services;

import com.stmc.sfgrecipeapp5.commands.UnitOfMeasureCommand;
import reactor.core.publisher.Flux;

public interface UnitOfMeasureService {
    Flux<UnitOfMeasureCommand> listAllUnitOfMeasures();
}
