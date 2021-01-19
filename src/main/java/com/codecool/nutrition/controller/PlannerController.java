package com.codecool.nutrition.controller;

import com.codecool.nutrition.fetch.PlannerFetch;
import com.codecool.nutrition.model.Meal;
import com.codecool.nutrition.model.Nutrient;
import com.codecool.nutrition.model.User;
import com.codecool.nutrition.repository.UserRepository;
import com.codecool.nutrition.request.PlannerConnectRequest;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
        User userObject = userRepository.findByName(username);
        ArrayList<String> plannerApiCredentials = plannerFetch.getPlannerApiCredentials(username);


        userObject.setMealPlanner(Boolean.TRUE);
        userObject.setPlannerUsername(plannerApiCredentials.get(0));
        userObject.setPlannerUserHash(plannerApiCredentials.get(1));

        userRepository.save(userObject);
    }

    @GetMapping("/planner/plan/generate/{plannerUsername}")
    public void getGeneratedMealPlan(@RequestParam(defaultValue = "empty") String targetCalories,
                                     @RequestParam(defaultValue = "empty") String diet,
                                     @RequestParam(defaultValue = "empty") List<String> excludes,
                                     @PathVariable String plannerUsername) throws UnirestException {

        JsonObject generatedMealPlan = plannerFetch.getGeneratedMealPlan(targetCalories, diet, excludes);
        JsonObject allWeekPlans = (JsonObject) generatedMealPlan.get("week");
        Map<String, ArrayList<Object>> allWeekPlansJsonObject = new HashMap<>();
        User userObject = userRepository.findByName(plannerUsername);
        System.out.println(userObject.toString());

        Set<Map.Entry<String, JsonElement>> objectEntrySet = allWeekPlans.entrySet();
        for(Map.Entry<String, JsonElement> entry : objectEntrySet) {
            System.out.println("FIRSTKEY:  "+entry.getKey());
            System.out.println("FIRSTVALUE:    "+entry.getValue());
            JsonObject values = (JsonObject) entry.getValue();
            for(Map.Entry<String, JsonElement> element : values.entrySet()) {
                System.out.println("SECONDKEY:    "+element.getKey());
                if(element.getKey().equals("meals")) {
                    JsonArray mealValues = (JsonArray) element.getValue();
                    ArrayList<Object> mealsPerDay = new ArrayList<>();
                    for (JsonElement mealValuesEntry : mealValues) {
                        JsonObject mealJsonObject = mealValuesEntry.getAsJsonObject();
                        System.out.println("MEALSOBJECT:    "+mealJsonObject);
                        Meal meal = new Meal();
                        for (Map.Entry<String, JsonElement> mealElement : mealJsonObject.entrySet()) {
                            switch (mealElement.getKey()) {
                                case "id":
                                    JsonElement foodID = mealElement.getValue();
                                    meal.setFoodID(foodID.getAsLong());
                                    break;
                                case "title":
                                    JsonElement foodTitle = mealElement.getValue();
                                    meal.setFoodTitle(foodTitle.getAsString());
                                    break;
                                case "servings":
                                    JsonElement foodServings = mealElement.getValue();
                                    meal.setServings(foodServings.getAsInt());
                                    break;
                                case "sourceUrl":
                                    JsonElement foodSourceUrl = mealElement.getValue();
                                    meal.setSourceUrl(foodSourceUrl.getAsString());
                                    break;
                                case "readyInMinutes":
                                    JsonElement foodReadyInMinutes = mealElement.getValue();
                                    meal.setReadyInMinutes(foodReadyInMinutes.getAsInt());
                                    break;
                            }
                        }
                        mealsPerDay.add(meal);
                    }
                    allWeekPlansJsonObject.put(entry.getKey(), mealsPerDay);
                } else if (element.getKey().equals("nutrients")) {
                    JsonObject nutrientValues = (JsonObject) element.getValue();
                    Nutrient nutrientObject = new Nutrient();
                    ArrayList<Object> nutrientsPerDay = new ArrayList<>();

                    nutrientObject.setCalories(nutrientValues.get("calories").getAsDouble());
                    nutrientObject.setCarbohydrates(nutrientValues.get("carbohydrates").getAsDouble());
                    nutrientObject.setProtein(nutrientValues.get("protein").getAsDouble());
                    nutrientObject.setFat(nutrientValues.get("fat").getAsDouble());

                    nutrientsPerDay.add(nutrientObject);

                    allWeekPlansJsonObject.put(entry.getKey()+"Nutrients", nutrientsPerDay);
                }
            }
        }

        userObject.setMealPlans(allWeekPlansJsonObject);
        userRepository.save(userObject);
    }

}
