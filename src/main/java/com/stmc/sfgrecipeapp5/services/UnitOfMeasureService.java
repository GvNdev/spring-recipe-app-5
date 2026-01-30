package com.stmc.sfgrecipeapp5.services;

import com.stmc.sfgrecipeapp5.commands.UnitOfMeasureCommand;

import java.util.Set;

public interface UnitOfMeasureService {
    Set<UnitOfMeasureCommand> listAllUnitOfMeasures();
}
