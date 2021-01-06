package com.codecool.nutrition;

import com.codecool.nutrition.fetch.FetchData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NutritionApplication {

    @Autowired
    private FetchData FetchDataCreator;

    public static void main(String[] args) {
        SpringApplication.run(NutritionApplication.class, args);
    }

}
