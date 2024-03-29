package com.codecool.nutrition.entity;

import com.codecool.nutrition.entity.customPlans.CustomDailyMealsEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.*;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(columnDefinition = "text")
    private String name;

    @Column(columnDefinition = "text")
    private String password;

    @Column(columnDefinition = "text")
    private String email;

    @Column(columnDefinition = "text")
    private String plannerUsername;

    @Column(columnDefinition = "text")
    private String plannerUserHash;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_dailymeals",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "dailymeals_id"))
    private List<DailyMealsEntity> dailyMeals = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_custom_dailymeals",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "custom_dailymeals_id"))
    private List<CustomDailyMealsEntity> CustomDailyMeals = new ArrayList<>();

    public UserEntity(String username, String email, String password) {
        this.name = username;
        this.email = email;
        this.password = password;
    }
}
