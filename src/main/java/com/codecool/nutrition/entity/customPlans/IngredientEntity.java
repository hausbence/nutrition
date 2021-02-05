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
@Table(name = "ingredient")
public class IngredientEntity {

    @Id
    @GeneratedValue
    private Long ingredient_id;

    @Column(columnDefinition = "text")
    private Long id;

    @Column(columnDefinition = "text")
    private String name;

    @Column(name ="nutrient_id")
    private Long nutrientId;

}
