package com.stmc.sfgrecipeapp5.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Notes {
    private String id;
    private Recipe recipe;
    private String recipeNotes;
}
