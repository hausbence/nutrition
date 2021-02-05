package com.codecool.nutrition.repository;

import com.codecool.nutrition.entity.customPlans.RecipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeEntityRepository extends JpaRepository<RecipeEntity, Long> {



}
