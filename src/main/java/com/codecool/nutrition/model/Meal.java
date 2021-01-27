package com.codecool.nutrition.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Meal {

    private Long id;

    private String title;

    private Integer readyInMinutes;

    private String sourceUrl;

    private Integer servings;
}
