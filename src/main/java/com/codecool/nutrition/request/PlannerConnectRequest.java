package com.codecool.nutrition.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class PlannerConnectRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;
}
