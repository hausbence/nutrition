package com.codecool.nutrition.entity;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@Entity
@Table(name = "nutrient")
public class NutrientEntity {

    @Id
    @GeneratedValue
    private Long Id;

    @Column(columnDefinition = "text")
    private Double calories;

    @Column(columnDefinition = "text")
    private Double protein;

    @Column(columnDefinition = "text")
    private Double fat;

    @Column(columnDefinition = "text")
    private Double carbohydrates;

}
