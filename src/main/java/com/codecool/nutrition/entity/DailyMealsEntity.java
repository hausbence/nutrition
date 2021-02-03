package com.codecool.nutrition.entity;

import com.codecool.nutrition.model.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@Entity
@Table(name = "dailymeals")
public class DailyMealsEntity {

    @Id
    @GeneratedValue
    private Long Id;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "dailymeals_meal",
        joinColumns = @JoinColumn(name = "dailymeals_id"),
        inverseJoinColumns = @JoinColumn(name = "meal_id"))
    private List<MealEntity> mealEntities = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "dailymeals_ingredient",
        joinColumns = @JoinColumn(name = "dailymeals_id"),
        inverseJoinColumns = @JoinColumn(name = "ingredient_id"))
    private List<IngredientEntity> ingredientEntities = new ArrayList<>();

    @Column(name ="nutrient_id")
    private Long nutrientId;

    @Column(name = "user_id")
    private Long userId;

    @Temporal(TemporalType.DATE)
    @Column(name = "creationDate")
    private Date timeStamp;

}
