package com.codecool.nutrition.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Column(name ="nutrient_id")
    private Long nutrientId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "dailymeals_date_day")
    private LocalDate date;

}
