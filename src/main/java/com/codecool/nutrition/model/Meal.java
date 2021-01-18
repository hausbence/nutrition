package com.codecool.nutrition.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class Meal {

    @Column(columnDefinition = "text")
    private Long foodID;

    @Column(columnDefinition = "text")
    private String foodTitle;

    @Column(columnDefinition = "text")
    private Integer readyInMinutes;

    @Column(columnDefinition = "text")
    private String sourceUrl;

    @Column(columnDefinition = "text")
    private Integer servings;

    @Column
    @ElementCollection
    @Singular
    List<Nutrient> nutrients = new ArrayList<>();
}
