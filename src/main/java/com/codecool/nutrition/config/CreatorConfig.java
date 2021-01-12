package com.codecool.nutrition.config;

import com.codecool.nutrition.fetch.FetchData;
import com.codecool.nutrition.fetch.PlannerFetch;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CreatorConfig {

    @Bean
    public FetchData FetchDatCreator() {
        return new FetchData();
    }

    @Bean
    public PlannerFetch PlannerFetchCreator() {
        return new PlannerFetch();
    }

}
