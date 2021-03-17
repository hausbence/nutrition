package com.codecool.nutrition.request;

import com.codecool.nutrition.model.Day;
import lombok.Data;

import java.util.List;

@Data
public class WeeklyPlanRequest {

    private List<Day> days;

    private String username;

}
