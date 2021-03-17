package com.codecool.nutrition.fetch;

import com.codecool.nutrition.controller.NutritionController;
import com.codecool.nutrition.entity.DailyMealsEntity;
import com.codecool.nutrition.entity.MealEntity;
import com.codecool.nutrition.entity.NutrientEntity;
import com.codecool.nutrition.entity.UserEntity;
//import com.codecool.nutrition.repository.PlannerRepository;
import com.codecool.nutrition.entity.customPlans.CustomDailyMealsEntity;
import com.codecool.nutrition.entity.customPlans.IngredientEntity;
import com.codecool.nutrition.entity.customPlans.RecipeEntity;
import com.codecool.nutrition.model.Day;
import com.codecool.nutrition.model.Meal;
import com.codecool.nutrition.model.customModels.CustomDay;
import com.codecool.nutrition.model.customModels.Ingredient;
import com.codecool.nutrition.model.customModels.Recipe;
import com.codecool.nutrition.repository.*;
import com.codecool.nutrition.request.CustomPlanRequest;
import com.codecool.nutrition.request.WeeklyPlanRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.bind.annotation.RequestBody;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;

public class PlannerFetch {

    @Autowired
    UserRepository userRepository;

    @Autowired
    NutritionController nutritionController;

    @Autowired
    NutrientEntityRepository nutrientEntityRepository;

    @Autowired
    RecipeEntityRepository recipeEntityRepository;

    @Autowired
    IngredientEntityRepository ingredientEntityRepository;

    @Autowired
    CustomDailyMealsEntityRepository customDailyMealsEntityRepository;

    @Autowired
    MealEntityRepository mealEntityRepository;

    @Autowired
    DailyMealsEntityRepository dailyMealsEntityRepository;

    @Value("${plannerconnect.url}")
    private String connectUrl;

    @Value("${plannerbase.url}")
    private String basePlannerUrl;

    private final String apiKey = getApiKey();

    public ArrayList<String> getPlannerApiCredentials(String username) throws UnirestException {
        //Set for storing username & hash from plannerAPI
        ArrayList<String> plannerCredentials = new ArrayList<>();

        //Initializing variables for connecting a User to plannerAPI
        String plannerApiName = "";
        String plannerApiNameHash = "";

        String host = connectUrl;

        //Creating a temporary User object to work with
        UserEntity userEntityObject = userRepository.findByName(username);

        //Checking that the user has no planner connected to his account yet
        String plannerName = userEntityObject.getPlannerUsername();
        if (plannerName == null) {
            //Creating a jsonObject for HttpRequest body
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", username);

            HttpResponse<JsonNode> response = Unirest.post(host + "?apiKey=" + apiKey)
                .body(jsonObject)
                .asJson();

            //Saving the response in a JSONObject
            JsonParser jp = new JsonParser();
            JsonElement je = jp.parse(response.getBody().toString());
            JsonObject responseObject = je.getAsJsonObject();

            System.out.println(responseObject.toString());

            //Getting the username & hashedUsername from the object given by plannerAPI and saving it
            Set<Map.Entry<String, JsonElement>> entrySet = responseObject.entrySet();
            for(Map.Entry<String,JsonElement> entry: entrySet) {
                if (entry.getKey().equals("username")) {
                    plannerApiName = String.valueOf(entry.getValue());
                    System.out.println(plannerApiName);
                }
                if (entry.getKey().equals("hash")) {
                    plannerApiNameHash = String.valueOf(entry.getValue());
                    System.out.println(plannerApiNameHash);
                }
            }
            plannerCredentials.add(plannerApiName);
            plannerCredentials.add(plannerApiNameHash);

            return plannerCredentials;

        } else {
            throw new IllegalArgumentException("This user already has a planner connect to his account.");
        }
    }

    public String getGeneratedMealPlanFromApi(String targetCalories, String diet, List<String> excludes) throws UnirestException {
        System.out.println("========");
        System.out.println(targetCalories);
        System.out.println(diet);
        System.out.println(excludes);
        System.out.println("========");

        String timeFrame = "week";

        String host = getValidatedPlannerGeneratorUrl(timeFrame, diet, targetCalories, excludes);


        HttpResponse<JsonNode> response = Unirest.get(host)
            .asJson();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(response.getBody().toString());

        return gson.toJson(je);
    }

    public String getValidatedPlannerGeneratorUrl(String timeFrame, String diet, String targetCalories, List<String> excludes) {
        StringBuilder excludesString = new StringBuilder();
        String url;

        if (!excludes.isEmpty()) {
            for (String exclude : excludes) {
                if (excludes.indexOf(exclude) == excludes.size() - 1) {
                    excludesString.append(exclude);
                } else {
                    excludesString.append(exclude).append(",");
                }
            }
        }


        if (!diet.equals("empty") && !targetCalories.equals("empty") && !String.valueOf(excludesString).equals("empty")) {
            url = basePlannerUrl + "generate" + "?apiKey=" + apiKey +
                "&diet=" + diet +
                "&targetCalories=" + targetCalories +
                "&excludes=" + excludesString +
                "&timeFrame=" + timeFrame;
        }
        else if (diet.equals("empty") && !targetCalories.equals("empty") && String.valueOf(excludesString).equals("empty")) {
            url = basePlannerUrl + "generate" + "?apiKey=" + apiKey + "&targetCalories=" + targetCalories + "&timeFrame=" + timeFrame;
        }
        else if (diet.equals("empty") && !targetCalories.equals("empty")) {
            url = basePlannerUrl + "generate" + "?apiKey=" + apiKey + "&targetCalories=" + targetCalories + "&timeFrame=" + timeFrame + "&excludes=" + excludesString;
        }
        else if (!diet.equals("empty") && !String.valueOf(excludesString).equals("empty")) {
            url = basePlannerUrl + "generate" + "?apiKey=" + apiKey + "&diet=" + diet + "&excludes=" + excludesString + "&timeFrame=" + timeFrame;
        }
        else if (String.valueOf(excludesString).equals("empty") && !diet.equals("empty") && !targetCalories.equals("empty")){
            url = basePlannerUrl + "generate" + "?apiKey=" + apiKey  + "&targetCalories=" + targetCalories + "&diet=" + diet + "&timeFrame=" + timeFrame;
        }
        else if (String.valueOf(excludesString).equals("empty") && !diet.equals("empty")) {
            url = basePlannerUrl + "generate" + "?apiKey=" + apiKey + "&diet=" + diet;
        }
        else {
            url = basePlannerUrl + "generate" + "?apiKey=" + apiKey + "&excludes=" + excludesString + "&timeFrame=" + timeFrame;
        }

        return url;
    }

    public void saveCustomPlan(@RequestBody CustomPlanRequest customPlanRequest, UserEntity userEntityObject) throws UnirestException, JsonProcessingException {
        List<CustomDay> days = customPlanRequest.getDays();
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
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
                    nutrientEntityRepository.save(nutrientEntity);
                    recipeEntity.setNutrientId(nutrientEntity.getId());
                    recipeEntityRepository.save(recipeEntity);
                    oneDayRecipeEntities.add(recipeEntity);
                }
                customDailyMealsEntity.setRecipeEntities(oneDayRecipeEntities);
            }
            if (!day.getIngredients().isEmpty()) {
                List<IngredientEntity> oneDayIngredientEntities = new ArrayList<>();
                for(Ingredient ingredient : day.getIngredients()) {
                    IngredientEntity ingredientEntity = new IngredientEntity();
                    NutrientEntity nutrientEntity;
                    ingredientEntity.setId(ingredient.getId());
                    ingredientEntity.setName(ingredient.getName());
                    String ingredientNutrient = nutritionController.getIngredientById(String.valueOf(ingredient.getId()));
                    nutrientEntity = getNutrientEntityForIngredient(ingredientNutrient);
                    nutrientEntityRepository.save(nutrientEntity);
                    ingredientEntity.setNutrientId(nutrientEntity.getId());
                    ingredientEntityRepository.save(ingredientEntity);
                    oneDayIngredientEntities.add(ingredientEntity);
                }
                customDailyMealsEntity.setIngredientEntities(oneDayIngredientEntities);
            }
            customDailyMealsEntities.add(customDailyMealsEntity);
            customDailyMealsEntity.setTimeStamp(timestamp);
            customDailyMealsEntity.setUserId(userEntityObject.getId());
            customDailyMealsEntityRepository.save(customDailyMealsEntity);
        }
        userEntityObject.setCustomDailyMeals(customDailyMealsEntities);
        userRepository.save(userEntityObject);
    }

    private NutrientEntity getNutrientEntityForIngredient(String ingredientNutrient) throws JsonProcessingException {
        ObjectMapper oMapper = new ObjectMapper();
        NutrientEntity nutrientEntity = new NutrientEntity();

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
                    }
                }
            }
        }

        for(Map map : finalNutrientsMap) {
            if (map.containsValue("Calories")) {
                String caloriesString = String.valueOf(map.get("amount"));
                Double caloriesDouble = Double.parseDouble(caloriesString);
                nutrientEntity.setCalories(caloriesDouble);
            } else if (map.containsValue("Protein")) {
                String proteinString = String.valueOf(map.get("amount"));
                Double proteinDouble = Double.parseDouble(proteinString);
                nutrientEntity.setProtein(proteinDouble);
            } else if (map.containsValue("Carbohydrates")) {
                String carbString = String.valueOf(map.get("amount"));
                Double carbDouble = Double.parseDouble(carbString);
                nutrientEntity.setCarbohydrates(carbDouble);
            } else {
                String fatString = String.valueOf(map.get("amount"));
                Double fatDouble = Double.parseDouble(fatString);
                nutrientEntity.setFat(fatDouble);
            }
        }

        return nutrientEntity;
    }

    public void saveGeneratedPlan(@RequestBody WeeklyPlanRequest weeklyPlanRequest, UserEntity userEntityObject) {
        List<Day> days = weeklyPlanRequest.getDays();
        Date date = new Date();
        List<DailyMealsEntity> dailyMealsEntities = new ArrayList<>();
        int dayIncrement = 0;
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
            dailyMealsEntity.setDate(LocalDate.from(timestamp.toLocalDateTime().plusDays(dayIncrement)));
            dailyMealsEntity.setNutrientId(nutrientEntity.getId());
            dailyMealsEntity.setUserId(userEntityObject.getId());
            dailyMealsEntityRepository.save(dailyMealsEntity);
            dailyMealsEntities.add(dailyMealsEntity);

            dayIncrement++;
        }

        userEntityObject.setDailyMeals(dailyMealsEntities);
        userRepository.save(userEntityObject);
    }

    private String getApiKey() {
        Map<String, String> env = System.getenv();

        for (String envName : env.keySet()) {
            if (envName.equals("API_KEY")) {
                return env.get(envName);
            }
        }

        return "";
    }
}
