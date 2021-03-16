package com.codecool.nutrition.controller;

import com.codecool.nutrition.entity.NutrientEntity;
import com.codecool.nutrition.entity.UserEntity;
import com.codecool.nutrition.entity.customPlans.CustomDailyMealsEntity;
import com.codecool.nutrition.entity.customPlans.IngredientEntity;
import com.codecool.nutrition.entity.customPlans.RecipeEntity;
import com.codecool.nutrition.fetch.PlannerFetch;
import com.codecool.nutrition.model.customModels.CustomDay;
import com.codecool.nutrition.model.customModels.Ingredient;
import com.codecool.nutrition.model.customModels.Recipe;
import com.codecool.nutrition.repository.*;
import com.codecool.nutrition.request.CustomPlanRequest;
import com.codecool.nutrition.response.MessageResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.*;

@RestController
public class CustomPlannerController {

    @Autowired
    PlannerFetch plannerFetch;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RecipeEntityRepository recipeEntityRepository;

    @Autowired
    NutritionController nutritionController;

    @Autowired
    NutrientEntityRepository nutrientEntityRepository;

    @Autowired
    IngredientEntityRepository ingredientEntityRepository;

    @Autowired
    CustomDailyMealsEntityRepository customDailyMealsEntityRepository;

    @PostMapping("/planner/custom/plan/save")
    public ResponseEntity<?> saveCustomMealPlan(@RequestBody CustomPlanRequest customPlanRequest) throws UnirestException, JsonProcessingException, IllegalAccessException {
        System.out.println(customPlanRequest.getPlannerUsername());
        UserEntity userEntityObject;

        if (userRepository.existsByPlannerUsername(customPlanRequest.getPlannerUsername())) {
            userEntityObject = userRepository.findByPlannerUsername(customPlanRequest.getPlannerUsername());
        } else {
            return ResponseEntity
                .badRequest()
                .body(new MessageResponse("Error: User not connected to plannerApi!"));
        }

        plannerFetch.saveCustomPlan(customPlanRequest, userEntityObject);

        return ResponseEntity
            .accepted()
            .body(new MessageResponse("Custom meal plan saved for user!"));
    }

}
