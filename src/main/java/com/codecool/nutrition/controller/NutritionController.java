package com.codecool.nutrition.controller;

import com.codecool.nutrition.fetch.NutritionFetch;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

@RestController
@CrossOrigin
public class NutritionController {

    @Autowired
    NutritionFetch nutritionFetch;

    @GetMapping("/recipes/random")
    public String getRandomRecipes() throws UnirestException, UnsupportedEncodingException {
        return nutritionFetch.getRandomRecipes();
    }

    @GetMapping("/recipes/{search}")
    public String getRecipeBySearch(@PathVariable("search") String search) throws UnirestException {
        return nutritionFetch.searchForRecipe(search);
    }

    @GetMapping("/recipe/{id}")
    public String getRecipeById(@PathVariable("id") String id) throws UnirestException {
        return nutritionFetch.getRecipeById(id);
    }

    @GetMapping("/recipe/nutrition/{id}")
    public String getRecipeNutrientById(@PathVariable("id") String id) throws UnirestException {
        return nutritionFetch.getRecipeNutrientById(id);
    }

    @GetMapping("/recipesBulk/{id}")
    public String getRecipesByIds(@PathVariable("id") String id) throws UnirestException {
        return nutritionFetch.getRecipesByIds(id);
    }

    @GetMapping("/ingredients/{search}")
    public String getIngredientBySearch(@PathVariable("search") String search) throws UnirestException {
        return nutritionFetch.searchForIngredient(search);
    }

    @GetMapping("/ingredient/{id}")
    public String getIngredientById(@PathVariable("id") String id) throws UnirestException {
        return nutritionFetch.getIngredientById(id);
    }

}
