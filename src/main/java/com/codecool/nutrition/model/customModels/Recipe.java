package com.codecool.nutrition.model.customModels;

import com.codecool.nutrition.model.Nutrient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Recipe {

    private Long id;

    private String title;

    private Nutrient nutrient;
}
