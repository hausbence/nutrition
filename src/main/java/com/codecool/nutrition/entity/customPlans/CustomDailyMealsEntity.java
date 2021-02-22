package com.codecool.nutrition.entity.customPlans;

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
@Table(name = "custom_dailymeals")
public class CustomDailyMealsEntity {

    @Id
    @GeneratedValue
    private Long Id;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "custom_dailymeals_ingredient",
        joinColumns = @JoinColumn(name = "dailymeals_id"),
        inverseJoinColumns = @JoinColumn(name = "ingredient_id"))
    private List<IngredientEntity> ingredientEntities = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "custom_dailymeals_recipe",
        joinColumns = @JoinColumn(name = "dailymeals_id"),
        inverseJoinColumns = @JoinColumn(name = "recipe_id"))
    private List<RecipeEntity> recipeEntities = new ArrayList<>();

    @Column(name = "user_id")
    private Long userId;

    @Temporal(TemporalType.DATE)
    @Column(name = "creationDate")
    private Date timeStamp;
}
