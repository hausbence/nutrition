package com.codecool.nutrition.model.customModels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomDay {

    private Timestamp timestamp;

    private List<Ingredient> ingredients;

    private List<Recipe> recipes;

}
