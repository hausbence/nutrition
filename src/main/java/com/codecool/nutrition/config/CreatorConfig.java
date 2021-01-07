package com.codecool.nutrition.config;

import com.codecool.nutrition.fetch.FetchData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CreatorConfig {

    @Bean
    public FetchData FetchDataCreator() {
        return new FetchData();
    }

}
