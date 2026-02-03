package com.stmc.sfgrecipeapp5.services.impl;

import com.stmc.sfgrecipeapp5.commands.UnitOfMeasureCommand;
import com.stmc.sfgrecipeapp5.converters.UnitOfMeasureToUnitOfMeasureCommand;
import com.stmc.sfgrecipeapp5.model.UnitOfMeasure;
import com.stmc.sfgrecipeapp5.repositories.reactive.UnitOfMeasureReactiveRepository;
import com.stmc.sfgrecipeapp5.services.UnitOfMeasureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UnitOfMeasureServiceImplTest {
    UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand = new UnitOfMeasureToUnitOfMeasureCommand();
    UnitOfMeasureService unitOfMeasureService;

    @Mock
    UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        unitOfMeasureService = new UnitOfMeasureServiceImpl(unitOfMeasureReactiveRepository, unitOfMeasureToUnitOfMeasureCommand);
    }

    @Test
    void listAllUnitOfMeasures() {
        // Given
        Set<UnitOfMeasure> unitOfMeasures = new HashSet<>();
        UnitOfMeasure unitOfMeasure1 = new UnitOfMeasure();
        unitOfMeasure1.setId("1");
        unitOfMeasures.add(unitOfMeasure1);

        UnitOfMeasure unitOfMeasure2 = new UnitOfMeasure();
        unitOfMeasure2.setId("2");
        unitOfMeasures.add(unitOfMeasure2);

        when(unitOfMeasureReactiveRepository.findAll()).thenReturn(Flux.just(unitOfMeasure1, unitOfMeasure2));

        // When
        List<UnitOfMeasureCommand> unitOfMeasureCommands = unitOfMeasureService.listAllUnitOfMeasures().collectList().block();

        // Then
        assertEquals(2, unitOfMeasureCommands.size());
        verify(unitOfMeasureReactiveRepository, times(1)).findAll();
    }
}