package com.codecool.nutrition.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

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
    private Set<Role> roles = new HashSet<>();

    @OneToMany
    @JoinTable(name = "user_plans",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "plan_id"))
    private Set<MealPlan> mealPlans = new HashSet<>();

    public User(String username, String email, String password) {
        this.name = username;
        this.email = email;
        this.password = password;
    }
}
