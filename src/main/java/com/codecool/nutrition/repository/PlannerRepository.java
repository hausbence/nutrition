package com.codecool.nutrition.repository;

import com.codecool.nutrition.model.MealPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlannerRepository extends JpaRepository<MealPlan,Long> {



}
