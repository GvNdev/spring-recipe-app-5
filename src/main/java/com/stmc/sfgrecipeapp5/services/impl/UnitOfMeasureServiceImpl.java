package com.stmc.sfgrecipeapp5.services.impl;

import com.stmc.sfgrecipeapp5.commands.UnitOfMeasureCommand;
import com.stmc.sfgrecipeapp5.converters.UnitOfMeasureToUnitOfMeasureCommand;
import com.stmc.sfgrecipeapp5.repositories.UnitOfMeasureRepository;
import com.stmc.sfgrecipeapp5.repositories.reactive.UnitOfMeasureReactiveRepository;
import com.stmc.sfgrecipeapp5.services.UnitOfMeasureService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class UnitOfMeasureServiceImpl implements UnitOfMeasureService {
    private final UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;
    private final UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand;

    public UnitOfMeasureServiceImpl(UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository, UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand) {
        this.unitOfMeasureReactiveRepository = unitOfMeasureReactiveRepository;
        this.unitOfMeasureToUnitOfMeasureCommand = unitOfMeasureToUnitOfMeasureCommand;
    }

    @Override
    public Flux<UnitOfMeasureCommand> listAllUnitOfMeasures() {
        return unitOfMeasureReactiveRepository
                .findAll()
                .map(unitOfMeasureToUnitOfMeasureCommand::convert);
    }
}
