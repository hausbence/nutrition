package com.codecool.nutrition.response;

import com.codecool.nutrition.entity.DailyMealsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeneratedMealPlanResponse {

    private String username;

    private List<DailyMealsEntity> days;

}
