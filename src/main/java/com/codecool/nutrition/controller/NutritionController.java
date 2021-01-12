package com.codecool.nutrition.controller;

import com.codecool.nutrition.fetch.FetchData;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NutritionController {

    @Autowired
    FetchData fetchData;

    @GetMapping("/recipes/random")
    public String getRandomRecipes() throws UnirestException {
        return fetchData.getRandomRecipes();
    }

    @GetMapping("/recipes/{search}")
    public String getRecipeBySearch(@PathVariable("search") String search) throws UnirestException {
        search = "complexSearch?query=" + search;
        return fetchData.searchForRecipe(search);
    }

    @GetMapping("/recipe/{id}")
    public String getRecipeById(@PathVariable("id") String id) throws UnirestException {
        id = id + "/information";
        return fetchData.getRecipeById(id);
    }

}
