package com.codecool.nutrition.repository;

import com.codecool.nutrition.entity.NutrientEntity;
import com.codecool.nutrition.model.Nutrient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NutrientEntityRepository extends JpaRepository<NutrientEntity, Long> {

}
