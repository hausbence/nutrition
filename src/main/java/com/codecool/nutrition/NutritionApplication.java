package com.codecool.nutrition;

import com.codecool.nutrition.fetch.NutritionFetch;
import com.codecool.nutrition.fetch.PlannerFetch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NutritionApplication {

    @Autowired
    private NutritionFetch nutritionFetchCreator;


    @Autowired
    private PlannerFetch plannerFetchCreator;

    public static void main(String[] args) {
        SpringApplication.run(NutritionApplication.class, args);
    }

}
