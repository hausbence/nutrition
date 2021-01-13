package com.codecool.nutrition.fetch;

import com.codecool.nutrition.model.User;
//import com.codecool.nutrition.repository.PlannerRepository;
import com.codecool.nutrition.repository.RoleRepository;
import com.codecool.nutrition.repository.UserRepository;
import com.google.gson.*;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.util.*;

public class PlannerFetch {

    @Autowired
    UserRepository userRepository;

//    @Autowired
//    PlannerRepository plannerRepository;

    @Value("${plannerconnect.url}")
    private String connectUrl;

    private final String apiKey = getApiKey();

    public ArrayList<String> getPlannerApiCredentials(String username) throws UnirestException {
        //Set for storing username & hash from plannerAPI
        ArrayList<String> plannerCredentials = new ArrayList<>();

        //Initializing variables for connecting a User to plannerAPI
        String plannerApiName = "";
        String plannerApiNameHash = "";

        String host = connectUrl;

        //Creating a temporary User object to work with
        User userObject = userRepository.findByName(username);

        //Checking that the user has no planner connected to his account yet
        String plannerName = userObject.getPlannerUsername();
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

    private String getJson(HttpResponse<JsonNode> response) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(response.getBody().toString());

        return gson.toJson(je);
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
