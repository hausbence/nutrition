package com.codecool.nutrition.request;

import com.codecool.nutrition.model.Day;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class WeeklyPlanRequest {

    private List<Day> days;

    private String username;

}
