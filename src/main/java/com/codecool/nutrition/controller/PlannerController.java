package com.codecool.nutrition.controller;

import com.codecool.nutrition.fetch.PlannerFetch;
import com.codecool.nutrition.model.Meal;
import com.codecool.nutrition.model.User;
import com.codecool.nutrition.repository.UserRepository;
import com.codecool.nutrition.request.PlannerConnectRequest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class PlannerController {

    @Autowired
    PlannerFetch plannerFetch;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/planner/connect")
    public void connectUserToPlanner(@Valid @RequestBody PlannerConnectRequest plannerConnectRequest) throws UnirestException {
        String username = plannerConnectRequest.getUsername();
        User userObject = userRepository.findByName(username);
        ArrayList<String> plannerApiCredentials = plannerFetch.getPlannerApiCredentials(username);


        userObject.setMealPlanner(Boolean.TRUE);
        userObject.setPlannerUsername(plannerApiCredentials.get(0));
        userObject.setPlannerUserHash(plannerApiCredentials.get(1));

        userRepository.save(userObject);
    }

    @GetMapping("/planner/plan/generated")
    public void getGeneratedMealPlan(@RequestParam(defaultValue = "empty") String targetCalories,
                                     @RequestParam(defaultValue = "empty") String diet,
                                     @RequestParam(defaultValue = "empty") List<String> excludes) {

        plannerFetch.getGeneratedMealPlan(targetCalories, diet, excludes);

    }

}
