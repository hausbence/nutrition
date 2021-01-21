package com.codecool.nutrition.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@Entity
@Table(name = "meal")
public class MealEntity {

    @Id
    @GeneratedValue
    private Long Id;

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
}
