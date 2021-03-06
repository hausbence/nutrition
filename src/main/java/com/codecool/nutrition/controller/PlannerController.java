package com.codecool.nutrition.controller;

import com.codecool.nutrition.entity.DailyMealsEntity;
import com.codecool.nutrition.entity.MealEntity;
import com.codecool.nutrition.entity.NutrientEntity;
import com.codecool.nutrition.fetch.PlannerFetch;
import com.codecool.nutrition.entity.UserEntity;
import com.codecool.nutrition.model.Day;
import com.codecool.nutrition.model.Meal;
import com.codecool.nutrition.repository.DailyMealsEntityRepository;
import com.codecool.nutrition.repository.MealEntityRepository;
import com.codecool.nutrition.repository.NutrientEntityRepository;
import com.codecool.nutrition.repository.UserRepository;
import com.codecool.nutrition.request.PlannerConnectRequest;
import com.codecool.nutrition.request.WeeklyPlanRequest;
import com.codecool.nutrition.response.MessageResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    DailyMealsEntityRepository dailyMealsEntityRepository;

    @Autowired
    MealEntityRepository mealEntityRepository;

    @Autowired
    NutrientEntityRepository nutrientEntityRepository;

    @Autowired
    PasswordEncoder encoder;

    @PostMapping("/planner/connect")
    public ResponseEntity<?> connectUserToPlanner(@Valid @RequestBody PlannerConnectRequest plannerConnectRequest) throws UnirestException {
        String username = plannerConnectRequest.getUsername();
        UserEntity userEntityObject = userRepository.findByName(username);
        ArrayList<String> plannerApiCredentials = plannerFetch.getPlannerApiCredentials(username);
        String plannerUsername = plannerApiCredentials.get(0).substring(1, plannerApiCredentials.get(0).length()-1);
        String plannerUserHash = plannerApiCredentials.get(1).substring(1, plannerApiCredentials.get(1).length()-1);

        if (userRepository.existsByPlannerUsername(plannerUsername)) {
            return ResponseEntity
                .badRequest()
                .body(new MessageResponse("Error! User already connected to plannerApi!"));
        }

        userEntityObject.setPlannerUsername(plannerUsername);
        userEntityObject.setPlannerUserHash(plannerUserHash);

        userRepository.save(userEntityObject);

        return ResponseEntity
            .accepted()
            .body(new MessageResponse("User successfully connected to plannerApi!"));
    }

    @GetMapping("/planner/plan/generate")
    public String getGeneratedMealPlan(@RequestParam(defaultValue = "empty") String targetCalories,
                                           @RequestParam(defaultValue = "empty") String diet,
                                           @RequestParam(defaultValue = "empty") List<String> excludes) throws UnirestException {

        return plannerFetch.getGeneratedMealPlan(targetCalories, diet, excludes);
    }

    @PostMapping("/planner/plan/generated/save")
    public ResponseEntity<?> saveGeneratedMealPlan(@RequestBody WeeklyPlanRequest weeklyPlanRequest) {
        UserEntity userEntityObject;

        if (userRepository.existsByPlannerUsername(weeklyPlanRequest.getPlannerUserName())) {
            userEntityObject = userRepository.findByPlannerUsername(weeklyPlanRequest.getPlannerUserName());
        } else {
            return ResponseEntity
                .badRequest()
                .body(new MessageResponse("Error: User not connected to plannerApi!"));
        }

        savePlan(weeklyPlanRequest, userEntityObject);

        return ResponseEntity
            .accepted()
            .body(new MessageResponse("Meal plan saved for user!"));
    }

    private void savePlan(@RequestBody WeeklyPlanRequest weeklyPlanRequest, UserEntity userEntityObject) {
        List<Day> days = weeklyPlanRequest.getDays();
        Date date = new Date();
        List<DailyMealsEntity> dailyMealsEntities = new ArrayList<>();

        for (Day day: days) {
            DailyMealsEntity dailyMealsEntity = new DailyMealsEntity();
            List<MealEntity> oneDayMealEntities = new ArrayList<>();
            NutrientEntity nutrientEntity = new NutrientEntity();

            for (Meal meal : day.getMeals()) {
                MealEntity mealEntity = new MealEntity();
                mealEntity.setSourceUrl(meal.getSourceUrl());
                mealEntity.setTitle(meal.getTitle());
                mealEntity.setReadyInMinutes(meal.getReadyInMinutes());
                mealEntity.setId(meal.getId());
                mealEntity.setServings(meal.getServings());
                mealEntityRepository.save(mealEntity);
                oneDayMealEntities.add(mealEntity);
            }

            nutrientEntity.setProtein(day.getNutrients().getProtein());
            nutrientEntity.setCalories(day.getNutrients().getCalories());
            nutrientEntity.setCarbohydrates(day.getNutrients().getCarbohydrates());
            nutrientEntity.setFat(day.getNutrients().getFat());
            nutrientEntityRepository.save(nutrientEntity);

            Timestamp timestamp = new Timestamp(date.getTime());

            dailyMealsEntity.setMealEntities(oneDayMealEntities);
            dailyMealsEntity.setTimeStamp(timestamp);
            dailyMealsEntity.setNutrientId(nutrientEntity.getId());
            dailyMealsEntity.setUserId(userEntityObject.getId());
            dailyMealsEntityRepository.save(dailyMealsEntity);
            dailyMealsEntities.add(dailyMealsEntity);
        }

        userEntityObject.setDailyMeals(dailyMealsEntities);
        userRepository.save(userEntityObject);
    }

}
