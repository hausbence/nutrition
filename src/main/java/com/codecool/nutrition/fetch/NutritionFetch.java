package com.codecool.nutrition.fetch;

import com.google.gson.*;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Value;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

public class NutritionFetch {
    @Value("${recipe.url}")
    private String recipesBaseUrl;

    @Value("${ingredient.url}")
    private String ingredientsBaseUrl;

    private String recipeLimit = "number=10";
    private final String apiKey = getApiKey();

    public String getRecipeById(String id) throws UnirestException {
        id = id + "/information";
        String host = recipesBaseUrl + id;
        HttpResponse<JsonNode> response = Unirest.get(host + "?" + "apiKey=" + apiKey)
            .asJson();

        return getJson(response);
    }

    public String searchForRecipe(String search) throws UnirestException {
        search = "complexSearch?query=" + search;
        String host = recipesBaseUrl + search;
        System.out.println(host);

        System.out.println(host + "&" + recipeLimit + "&apiKey=" + apiKey);

        return getResponseWithLimit(host);
    }

    public String searchForIngredient(String search) throws UnirestException {
        search = "search?query=" + search;
        String host = ingredientsBaseUrl + search;
        return getResponseWithLimit(host);
    }

    public String getIngredientById(String id) throws UnirestException {
        String host = ingredientsBaseUrl + id + "/information?amount=1";
        return getResponseWithLimit(host);
    }

    public String getRandomRecipes() throws UnirestException, UnsupportedEncodingException {
        String host = recipesBaseUrl + "random";
        System.out.println(host);
        String charset = "UTF-8";

        String query = String.format(recipeLimit,
            URLEncoder.encode(recipeLimit,charset));

        HttpResponse<JsonNode> response = Unirest.get(host + "?" + query + "&apiKey=" + apiKey)
            .asJson();

        return getJson(response);
    }

    private String getResponseWithLimit(String host) throws UnirestException {
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
