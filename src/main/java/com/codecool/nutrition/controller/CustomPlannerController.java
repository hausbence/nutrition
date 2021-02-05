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
import com.codecool.nutrition.repository.NutrientEntityRepository;
import com.codecool.nutrition.repository.RecipeEntityRepository;
import com.codecool.nutrition.repository.UserRepository;
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

        List<CustomDay> days = customPlanRequest.getDays();
        Date date = new Date();
        List<CustomDailyMealsEntity> customDailyMealsEntities = new ArrayList<>();

        for (CustomDay day : days) {
            CustomDailyMealsEntity customDailyMealsEntity = new CustomDailyMealsEntity();

            if (!day.getRecipes().isEmpty()) {
                List<RecipeEntity> oneDayRecipeEntities = new ArrayList<>();
                for (Recipe recipe : day.getRecipes()) {
                    RecipeEntity recipeEntity = new RecipeEntity();
                    NutrientEntity nutrientEntity = new NutrientEntity();
                    recipeEntity.setId(recipe.getId());
                    recipeEntity.setTitle(recipe.getTitle());
                    String recipeNutrient = nutritionController.getRecipeNutrientById(String.valueOf(recipe.getId()));
                    Map<String, String> nutrientMap = new ObjectMapper().readValue(recipeNutrient, Map.class);
                    nutrientEntity.setProtein(Double.parseDouble(nutrientMap.get("protein").replace("g", "")));
                    nutrientEntity.setFat(Double.parseDouble(nutrientMap.get("fat").replace("g", "")));
                    nutrientEntity.setCarbohydrates(Double.parseDouble(nutrientMap.get("carbs").replace("g", "")));
                    nutrientEntity.setCalories(Double.parseDouble(nutrientMap.get("calories")));
                    oneDayRecipeEntities.add(recipeEntity);
                    nutrientEntityRepository.save(nutrientEntity);
                    recipeEntity.setNutrientId(nutrientEntity.getId());
                    recipeEntityRepository.save(recipeEntity);
                }
                customDailyMealsEntity.setRecipeEntities(oneDayRecipeEntities);
            }
            if (!day.getIngredients().isEmpty()) {
                //!!!TODO
                for(Ingredient ingredient : day.getIngredients()) {
                    IngredientEntity ingredientEntity = new IngredientEntity();
                    NutrientEntity nutrientEntity = new NutrientEntity();
                    ingredientEntity.setId(ingredient.getId());
                    ingredientEntity.setName(ingredient.getName());
                    String ingredientNutrient = nutritionController.getIngredientById(String.valueOf(ingredient.getId()));
                    getIngredientNutrientsMap(ingredientNutrient);
                    //valahogy elkéne tárolni ezt a fost, kirakom külön methodba és ott lehet iterálgatni rajta végső esetben
                    System.out.println("wait");
                }
                List<IngredientEntity> oneDayIngredientEntities = new ArrayList<>();
                customDailyMealsEntity.setIngredientEntities(oneDayIngredientEntities);
            }
            customDailyMealsEntities.add(customDailyMealsEntity);
            userEntityObject.setCustomDailyMeals(customDailyMealsEntities);
        }

        return ResponseEntity
            .badRequest()
            .body(new MessageResponse("Not ready yet"));
    }

    private Map<String, String> getIngredientNutrientsMap(String ingredientNutrient) throws JsonProcessingException, IllegalAccessException {
        ObjectMapper oMapper = new ObjectMapper();

        //This contains the nutrients needed for nutrientEntity!! !!!TODO need to iterate on it, and get only the Double type values out from it, not the objects
        ArrayList<Map<String, Object>> finalNutrientsMap = new ArrayList<>();

        Map<String, String> ingredientInfo = new ObjectMapper().readValue(ingredientNutrient, Map.class);
        String nutritionInfo = new ObjectMapper().writeValueAsString(ingredientInfo.get("nutrition"));
        Map<String, ArrayList> nutritionMap = new ObjectMapper().readValue(nutritionInfo, Map.class);
        for (Map.Entry<String, ArrayList> entry : nutritionMap.entrySet()) {
            if (entry.getKey().equals("nutrients")) {
                ArrayList<?> values = entry.getValue();
                for (Object object : values) {
                    Map<String, Object> nutrientMap = oMapper.convertValue(object, Map.class);
                    for (Map.Entry<String, Object> nutrientEntry : nutrientMap.entrySet()) {
                        if (nutrientEntry.getKey().equals("name")) {
                            if (nutrientEntry.getValue().equals("Calories")) {
                                finalNutrientsMap.add(nutrientMap);
                            }
                            if (nutrientEntry.getValue().equals("Protein")) {
                                finalNutrientsMap.add(nutrientMap);
                            }
                            if (nutrientEntry.getValue().equals("Fat")) {
                                finalNutrientsMap.add(nutrientMap);
                            }
                            if (nutrientEntry.getValue().equals("Carbohydrates")) {
                                finalNutrientsMap.add(nutrientMap);
                            }
                        }
                        System.out.println(nutrientEntry);
                    }
                }
            }
        }

        return ingredientInfo;
    }

}
