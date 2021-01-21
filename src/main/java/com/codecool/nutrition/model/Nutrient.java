package com.codecool.nutrition.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Nutrient {

    private Double calories;

    private Double protein;

    private Double fat;

    private Double carbohydrates;

}
