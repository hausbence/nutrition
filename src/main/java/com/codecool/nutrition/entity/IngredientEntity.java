package com.codecool.nutrition.entity;

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
    private String originalName;

    @Column(name ="nutrient_id")
    private Long nutrientId;

}
