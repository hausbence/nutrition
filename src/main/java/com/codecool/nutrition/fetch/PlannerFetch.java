package com.codecool.nutrition.fetch;

import com.codecool.nutrition.model.User;
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

import java.util.Map;
import java.util.Optional;

public class PlannerFetch {

    @Autowired
    UserRepository userRepository;

    @Value("${plannerconnect.url}")
    private String connectUrl;

    private final String apiKey = getApiKey();

    public String connectToPlanner(String plannerUsername) throws UnirestException {
        String host = connectUrl;
        Optional<User> userObject = userRepository.findByName(plannerUsername);
        String plannerName = userObject.get().getPlannerUsername();
        System.out.println(userObject.toString());

        if (plannerName == null) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", plannerUsername);

            userObject.get().setMealPlanner(Boolean.TRUE);

            HttpResponse<JsonNode> response = Unirest.post(host + "?apiKey=" + apiKey)
                .body(jsonObject)
                .asJson();

            String responseData =  getJson(response);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonParser jp = new JsonParser();
            JsonElement je = jp.parse(response.getBody().toString());

            JsonObject responseObject = je.getAsJsonObject();
            //We got the response in json, need to iterate on it, get the username from it, and the hash version also

            System.out.println(responseData);

            return "";

        } else {
            return "This user has already connected a planner to his account.";
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
