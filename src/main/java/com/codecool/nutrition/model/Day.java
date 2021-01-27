package com.codecool.nutrition.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Day {

    private Timestamp timestamp;

    private List<Meal> meals;

    private Nutrient nutrients;
}
