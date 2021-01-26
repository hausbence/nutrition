package com.codecool.nutrition.entity;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@Entity
@Table(name = "meal")
public class MealEntity {

    @Id
    @GeneratedValue
    private Long meal_id;

    @Column(columnDefinition = "text")
    private Long id;

    @Column(columnDefinition = "text")
    private String title;

    @Column(columnDefinition = "text")
    private Integer readyInMinutes;

    @Column(columnDefinition = "text")
    private String sourceUrl;

    @Column(columnDefinition = "text")
    private Integer servings;
}
