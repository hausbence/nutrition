package com.codecool.nutrition.model;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@Entity
@Table(name = "plans")
public class MealPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(length = 20, name = "planType")
    private String planType;

}
