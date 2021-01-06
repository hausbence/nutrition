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

    private String limit = "number=10";
    private final String charset = "UTF-8";
    private final String apiKey = getApiKey();

    public String searchForRecipe(String search) throws UnsupportedEncodingException, UnirestException {
        String host = baseUrl + search;
        System.out.println(host);

        String query = String.format(limit,
            URLEncoder.encode(limit,charset));

        System.out.println(host + "&" + query + "&apiKey=" + apiKey);

        return getResponse(host,apiKey, query);
    }

    public String randomRecipesFetch() throws UnsupportedEncodingException, UnirestException {
        //https://api.spoonacular.com/recipes/random?number=10

        String host = baseUrl + "/random";

        String query = String.format(limit,
            URLEncoder.encode(limit,charset));

        HttpResponse<JsonNode> response = Unirest.get(host + "?" + query + "&apiKey=" + apiKey)
            .asJson();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(response.getBody().toString());

        return gson.toJson(je);
    }

    private String getResponse(String host, String apiKey, String query) throws UnirestException {
        System.out.println(host);
        System.out.println(query);
        HttpResponse<JsonNode> response = Unirest.get(host + "&" + query + "&apiKey=" + apiKey)
            .asJson();

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
