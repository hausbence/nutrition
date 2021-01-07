package com.codecool.nutrition.fetch;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import com.google.gson.*;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Value;

public class FetchData {

    @Value("${recipe.url}")
    private String baseUrl;

    private String recipeLimit = "number=10";
    private final String apiKey = getApiKey();

    public String getRecipeById(String id) throws UnirestException {
        String host = baseUrl + id;

        HttpResponse<JsonNode> response = Unirest.get(host + "?" + "apiKey=" + apiKey)
            .asJson();

        return getJson(response);
    }

    public String searchForRecipe(String search) throws UnsupportedEncodingException, UnirestException {
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
