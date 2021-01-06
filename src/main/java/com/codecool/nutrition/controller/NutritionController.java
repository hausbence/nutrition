package com.codecool.nutrition.controller;

import com.codecool.nutrition.fetch.FetchData;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

@RestController
public class NutritionController {

    @Autowired
    FetchData fetchData;

    @GetMapping("/recipes/random")
    public String getRandomRecipes() throws UnsupportedEncodingException, UnirestException {
        return fetchData.randomRecipesFetch();
    }

    @GetMapping("/recipes/{search}")
    public String getRecipeBySearch(@PathVariable("search") String search) throws UnsupportedEncodingException, UnirestException {
        search = "/complexSearch?query=" + search;
        return fetchData.searchForRecipe(search);
    }

}
