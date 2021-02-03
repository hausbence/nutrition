package com.codecool.nutrition.entity.customPlans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@Entity
@Table(name = "recipe")
public class RecipeEntity {

    @Id
    @GeneratedValue
    private Long recipe_id;

    @Column(columnDefinition = "text")
    private Long id;

    @Column(columnDefinition = "text")
    private String title;

    @Column(name ="nutrient_id")
    private Long nutrientId;


}
