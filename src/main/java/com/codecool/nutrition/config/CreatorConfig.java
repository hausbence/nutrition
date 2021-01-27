package com.codecool.nutrition.config;

import com.codecool.nutrition.fetch.NutritionFetch;
import com.codecool.nutrition.fetch.PlannerFetch;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CreatorConfig {

    @Bean
    public NutritionFetch FetchDatCreator() {
        return new NutritionFetch();
    }

    @Bean
    public PlannerFetch PlannerFetchCreator() {
        return new PlannerFetch();
    }

}
