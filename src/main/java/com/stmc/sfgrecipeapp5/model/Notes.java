package com.stmc.sfgrecipeapp5.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
public class Notes {
    @Id
    private String id;
    private String recipeNotes;
}
