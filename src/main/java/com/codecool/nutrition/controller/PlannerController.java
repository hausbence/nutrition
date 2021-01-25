package com.codecool.nutrition.controller;

import com.codecool.nutrition.entity.DailyMealsEntity;
import com.codecool.nutrition.entity.MealEntity;
import com.codecool.nutrition.entity.NutrientEntity;
import com.codecool.nutrition.fetch.PlannerFetch;
import com.codecool.nutrition.entity.UserEntity;
import com.codecool.nutrition.model.Day;
import com.codecool.nutrition.model.Meal;
import com.codecool.nutrition.repository.UserRepository;
import com.codecool.nutrition.request.PlannerConnectRequest;
import com.codecool.nutrition.request.WeeklyPlanRequest;
import com.google.gson.JsonObject;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.*;

@RestController
public class PlannerController {

    @Autowired
    PlannerFetch plannerFetch;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/planner/connect")
    public void connectUserToPlanner(@Valid @RequestBody PlannerConnectRequest plannerConnectRequest) throws UnirestException {
        String username = plannerConnectRequest.getUsername();
        UserEntity userEntityObject = userRepository.findByName(username);
        ArrayList<String> plannerApiCredentials = plannerFetch.getPlannerApiCredentials(username);


        userEntityObject.setMealPlanner(Boolean.TRUE);
        userEntityObject.setPlannerUsername(plannerApiCredentials.get(0));
        userEntityObject.setPlannerUserHash(plannerApiCredentials.get(1));

        userRepository.save(userEntityObject);
    }

    @GetMapping("/planner/plan/generate")
    public JsonObject getGeneratedMealPlan(@RequestParam(defaultValue = "empty") String targetCalories,
                                           @RequestParam(defaultValue = "empty") String diet,
                                           @RequestParam(defaultValue = "empty") List<String> excludes) throws UnirestException {

        return plannerFetch.getGeneratedMealPlan(targetCalories, diet, excludes);
    }

    @PostMapping("/planner/plan/save")
    public void saveGeneratedMealPlan(@RequestBody WeeklyPlanRequest weeklyPlanRequest) {
        //error kezelés
        UserEntity userEntityObject = userRepository.findByName(weeklyPlanRequest.getName());
        List<Day> days = weeklyPlanRequest.getDays();
        Date date = new Date();
        List<DailyMealsEntity> dailyMealsEntities = new ArrayList<>();

        for (Day day: days) {
            DailyMealsEntity dailyMealsEntity = new DailyMealsEntity();
            List<MealEntity> oneDayMealEntities = new ArrayList<>();
            NutrientEntity nutrientEntity = new NutrientEntity();

            for (Meal meal:day.getMeals()) {
                MealEntity mealEntity = new MealEntity();
                mealEntity.setSourceUrl(meal.getSourceUrl());
                mealEntity.setTitle(meal.getTitle());
                mealEntity.setReadyInMinutes(meal.getReadyInMinutes());
                mealEntity.setId(meal.getId());
                mealEntity.setServings(meal.getServings());
                oneDayMealEntities.add(mealEntity);
            }

            nutrientEntity.setProtein(day.getNutrients().getProtein());
            nutrientEntity.setCalories(day.getNutrients().getCalories());
            nutrientEntity.setCarbohydrates(day.getNutrients().getCarbohydrates());
            nutrientEntity.setFat(day.getNutrients().getFat());

            Timestamp timestamp = new Timestamp(date.getTime());

            dailyMealsEntity.setMealEntities(oneDayMealEntities);
            dailyMealsEntity.setTimeStamp(timestamp);
            dailyMealsEntity.setNutrientId(nutrientEntity.getId());
            dailyMealsEntity.setUserId(userEntityObject.getId());
            dailyMealsEntities.add(dailyMealsEntity);
        }

        userEntityObject.setDailyMeals(dailyMealsEntities);
        userRepository.save(userEntityObject);
    }

}
