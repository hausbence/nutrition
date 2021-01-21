package com.codecool.nutrition.entity;

import com.codecool.nutrition.model.Meal;
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
    private Boolean mealPlanner = Boolean.FALSE;

    @Column(columnDefinition = "text")
    private String plannerUsername;

    @Column(columnDefinition = "text")
    private String plannerUserHash;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleEntity> roleEntities = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_dailymeals",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "dailymeals_id"))
    private List<DailyMealsEntity> dailyMeals = new ArrayList<>();

    public UserEntity(String username, String email, String password) {
        this.name = username;
        this.email = email;
        this.password = password;
    }
}
