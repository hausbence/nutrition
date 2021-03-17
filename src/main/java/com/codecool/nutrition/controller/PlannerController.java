package com.codecool.nutrition.controller;

import com.codecool.nutrition.fetch.PlannerFetch;
import com.codecool.nutrition.entity.UserEntity;
import com.codecool.nutrition.repository.NutrientEntityRepository;
import com.codecool.nutrition.response.GeneratedMealPlanResponse;
import com.codecool.nutrition.repository.DailyMealsEntityRepository;
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
    NutrientEntityRepository nutrientEntityRepository;

    @Autowired
    PasswordEncoder encoder;

    @PostMapping("/planner/connect")
    @CrossOrigin
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
    public String getGeneratedMealPlanFromApi(@RequestParam(defaultValue = "empty") String targetCalories,
                                              @RequestParam(defaultValue = "empty") String diet,
                                              @RequestParam(defaultValue = "empty") List<String> excludes) throws UnirestException {

        return plannerFetch.getGeneratedMealPlanFromApi(targetCalories, diet, excludes);
    }

    @PostMapping("/planner/plan/generated/save")
    public ResponseEntity<?> saveGeneratedMealPlan(@RequestBody WeeklyPlanRequest weeklyPlanRequest) {
        UserEntity userEntityObject;

        if (userRepository.existsByName(weeklyPlanRequest.getUsername())) {
            userEntityObject = userRepository.findByName(weeklyPlanRequest.getUsername());
        } else {
            return ResponseEntity
                .badRequest()
                .body(new MessageResponse("Error: User not connected to plannerApi!"));
        }

        plannerFetch.saveGeneratedPlan(weeklyPlanRequest, userEntityObject);

        return ResponseEntity
            .accepted()
            .body(new MessageResponse("Meal plan saved for user!"));
    }

    @GetMapping("/plan/generated/{userId}")
    public GeneratedMealPlanResponse getGeneratedMealPlanByUserId(@PathVariable("userId") Long userId) {
        GeneratedMealPlanResponse generatedMealPlanResponse = new GeneratedMealPlanResponse();
        Optional<UserEntity> userEntityObject = userRepository.findById(userId);
        if (userEntityObject.isPresent()) {
            generatedMealPlanResponse.setDays(userEntityObject.get().getDailyMeals());
            generatedMealPlanResponse.setUsername(userEntityObject.get().getName());
        } else {
            throw new IllegalArgumentException("There is no user with this ID!");
        }

        return generatedMealPlanResponse;
    }

    @GetMapping("/plan/dailymeals/nutrient/{nutrientId}")
    public Optional<?> getNutrientForDailyMealsById(@PathVariable("nutrientId") Long nutrientId) {
        Optional<?> nutrientEntity = nutrientEntityRepository.findById(nutrientId);
        if (nutrientEntity.isPresent()) {
            return nutrientEntity;
        }else {
            throw new IllegalArgumentException("There is no nutrient object with that ID!");
        }
    }

}
