package com.codecool.nutrition.fetch;

import com.codecool.nutrition.entity.UserEntity;
//import com.codecool.nutrition.repository.PlannerRepository;
import com.codecool.nutrition.repository.UserRepository;
import com.google.gson.*;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;

public class PlannerFetch {

    @Autowired
    UserRepository userRepository;

//    @Autowired
//    PlannerRepository plannerRepository;

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

    public JsonObject getGeneratedMealPlan(String targetCalories, String diet, List<String> excludes) throws UnirestException {
        System.out.println("========");
        System.out.println(targetCalories);
        System.out.println(diet);
        System.out.println(excludes);
        System.out.println("========");

        String timeFrame = "week";

        String host = getValidatedPlannerGeneratorUrl(timeFrame, diet, targetCalories, excludes);


        HttpResponse<JsonNode> response = Unirest.get(host)
            .asJson();

        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(response.getBody().toString());
        JsonObject responseObject = je.getAsJsonObject();

        System.out.println("RESPONSE:   " + responseObject);

        return responseObject;
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
