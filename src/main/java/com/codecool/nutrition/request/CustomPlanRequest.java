package com.codecool.nutrition.request;

import com.codecool.nutrition.model.customModels.CustomDay;
import lombok.Data;

import java.util.List;

@Data
public class CustomPlanRequest {

    private List<CustomDay> days;

    private String plannerUserName;

}
