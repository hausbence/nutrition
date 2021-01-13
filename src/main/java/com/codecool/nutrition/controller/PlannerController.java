package com.codecool.nutrition.controller;

import com.codecool.nutrition.fetch.PlannerFetch;
import com.codecool.nutrition.model.User;
import com.codecool.nutrition.repository.UserRepository;
import com.codecool.nutrition.request.PlannerConnectRequest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class PlannerController {

    @Autowired
    PlannerFetch plannerFetch;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/planner/connect")
    public String connectUserToPlanner(@Valid @RequestBody PlannerConnectRequest plannerConnectRequest) throws UnirestException {
        String username = plannerConnectRequest.getUsername();
        User userObject = userRepository.findByName(username);
        ArrayList<String> plannerApiCredentials = plannerFetch.getPlannerApiCredentials(username);


        userObject.setMealPlanner(Boolean.TRUE);
        userObject.setPlannerUsername(plannerApiCredentials.get(0));
        userObject.setPlannerUserHash(plannerApiCredentials.get(1));

        userRepository.save(userObject);
        return "";
    }

}
