package com.codecool.nutrition.controller;

import com.codecool.nutrition.fetch.PlannerFetch;
import com.codecool.nutrition.request.PlannerConnectRequest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class PlannerController {

    @Autowired
    PlannerFetch plannerFetch;

    @PostMapping("/planner/connect")
    public String connectUserToPlanner(@Valid @RequestBody PlannerConnectRequest plannerConnectRequest) throws UnirestException {
        String plannerUsername = plannerConnectRequest.getUsername();
        System.out.println(plannerUsername);
        return plannerFetch.connectToPlanner(plannerUsername);
    }

}
