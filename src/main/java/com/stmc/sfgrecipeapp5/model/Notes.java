package com.stmc.sfgrecipeapp5.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Notes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Recipe recipe;

    @Lob
    private String recipeNotes;
}
