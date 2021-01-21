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
//        JsonObject allWeekPlans = (JsonObject) generatedMealPlan.get("week");
//        Map<String, ArrayList<Object>> allWeekPlansJsonObject = new HashMap<>();
//        User userObject = userRepository.findByName(plannerUsername);
//        System.out.println(userObject.toString());

//        Set<Map.Entry<String, JsonElement>> objectEntrySet = allWeekPlans.entrySet();
//        for(Map.Entry<String, JsonElement> entry : objectEntrySet) {
//            System.out.println("FIRSTKEY:  "+entry.getKey());
//            System.out.println("FIRSTVALUE:    "+entry.getValue());
//            JsonObject values = (JsonObject) entry.getValue();
//            for(Map.Entry<String, JsonElement> element : values.entrySet()) {
//                System.out.println("SECONDKEY:    "+element.getKey());
//                if(element.getKey().equals("meals")) {
//                    JsonArray mealValues = (JsonArray) element.getValue();
//                    ArrayList<Object> mealsPerDay = new ArrayList<>();
//                    for (JsonElement mealValuesEntry : mealValues) {
//                        JsonObject mealJsonObject = mealValuesEntry.getAsJsonObject();
//                        System.out.println("MEALSOBJECT:    "+mealJsonObject);
//                        Meal meal = new Meal();
//                        for (Map.Entry<String, JsonElement> mealElement : mealJsonObject.entrySet()) {
//                            switch (mealElement.getKey()) {
//                                case "id":
//                                    JsonElement foodID = mealElement.getValue();
//                                    meal.setFoodID(foodID.getAsLong());
//                                    break;
//                                case "title":
//                                    JsonElement foodTitle = mealElement.getValue();
//                                    meal.setFoodTitle(foodTitle.getAsString());
//                                    break;
//                                case "servings":
//                                    JsonElement foodServings = mealElement.getValue();
//                                    meal.setServings(foodServings.getAsInt());
//                                    break;
//                                case "sourceUrl":
//                                    JsonElement foodSourceUrl = mealElement.getValue();
//                                    meal.setSourceUrl(foodSourceUrl.getAsString());
//                                    break;
//                                case "readyInMinutes":
//                                    JsonElement foodReadyInMinutes = mealElement.getValue();
//                                    meal.setReadyInMinutes(foodReadyInMinutes.getAsInt());
//                                    break;
//                            }
//                        }
//                        mealsPerDay.add(meal);
//                    }
//                    allWeekPlansJsonObject.put(entry.getKey(), mealsPerDay);
//                } else if (element.getKey().equals("nutrients")) {
//                    JsonObject nutrientValues = (JsonObject) element.getValue();
//                    Nutrient nutrientObject = new Nutrient();
//                    ArrayList<Object> nutrientsPerDay = new ArrayList<>();
//
//                    nutrientObject.setCalories(nutrientValues.get("calories").getAsDouble());
//                    nutrientObject.setCarbohydrates(nutrientValues.get("carbohydrates").getAsDouble());
//                    nutrientObject.setProtein(nutrientValues.get("protein").getAsDouble());
//                    nutrientObject.setFat(nutrientValues.get("fat").getAsDouble());
//
//                    nutrientsPerDay.add(nutrientObject);
//
//                    allWeekPlansJsonObject.put(entry.getKey()+"Nutrients", nutrientsPerDay);
//                }
//            }
//        }

//        userObject.setMealPlans(allWeekPlansJsonObject);
//        userRepository.save(userObject);
    }

    @PostMapping("/planner/plan/save")
    public void saveGeneratedMealPlan(@RequestBody WeeklyPlanRequest weeklyPlanRequest) {
        UserEntity userEntityObject = userRepository.findByName(weeklyPlanRequest.getUsername());
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
                mealEntity.setFoodTitle(meal.getFoodTitle());
                mealEntity.setReadyInMinutes(meal.getReadyInMinutes());
                mealEntity.setFoodID(meal.getFoodID());
                mealEntity.setServings(meal.getServings());
                oneDayMealEntities.add(mealEntity);
            }

            nutrientEntity.setProtein(day.getNutrient().getProtein());
            nutrientEntity.setCalories(day.getNutrient().getCalories());
            nutrientEntity.setCarbohydrates(day.getNutrient().getCarbohydrates());
            nutrientEntity.setFat(day.getNutrient().getFat());

            Timestamp timestamp = new Timestamp(date.getTime());

            dailyMealsEntity.setMealEntities(oneDayMealEntities);
            dailyMealsEntity.setTimeStamp(timestamp);
            dailyMealsEntity.setNutrientId(nutrientEntity.getId());
            dailyMealsEntities.add(dailyMealsEntity);
        }

        userEntityObject.setDailyMeals(dailyMealsEntities);
        userRepository.save(userEntityObject);
    }

}
