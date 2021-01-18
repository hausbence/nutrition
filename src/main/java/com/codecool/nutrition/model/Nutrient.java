package com.codecool.nutrition.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class Nutrient {

    @Column(columnDefinition = "text")
    private Double calories;

    @Column(columnDefinition = "text")
    private Double protein;

    @Column(columnDefinition = "text")
    private Double fat;

    @Column(columnDefinition = "text")
    private Double carbohydrates;

}
