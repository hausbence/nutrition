package com.codecool.nutrition.fetch;

import com.google.gson.*;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

public class FetchData {
    @Value("${recipe.url}")
    private String baseUrl;

    private String recipeLimit = "number=10";
    private final String apiKey = getApiKey();

    public String getRecipeById(String id) throws UnirestException {
        String host = baseUrl + id;
        return getResponseWithoutLimit(host,apiKey);
    }

    public String searchForRecipe(String search) throws UnirestException {
        String host = baseUrl + search;
        System.out.println(host);

        System.out.println(host + "&" + recipeLimit + "&apiKey=" + apiKey);

        return getResponseWithLimit(host,apiKey, recipeLimit);
    }

    public String getRandomRecipes() throws UnirestException {
        String host = baseUrl + "random";
        System.out.println(host);

        HttpResponse<JsonNode> response = Unirest.get(host + "?" + recipeLimit + "&apiKey=" + apiKey)
            .asJson();

        return getJson(response);
    }

    private String getResponseWithLimit(String host, String apiKey, String recipeLimit) throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.get(host + "&" + recipeLimit + "&apiKey=" + apiKey)
            .asJson();

        return getJson(response);
    }

    private String getResponseWithoutLimit(String host, String apiKey) throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.get(host + "?" + "apiKey=" + apiKey)
            .asJson();

        return getJson(response);
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
