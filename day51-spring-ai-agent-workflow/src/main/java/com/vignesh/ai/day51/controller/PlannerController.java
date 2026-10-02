package com.vignesh.ai.day51.controller;

import com.vignesh.ai.day51.model.TaskPlan;
import com.vignesh.ai.day51.service.PlannerService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/planner")
public class PlannerController {

    private final PlannerService plannerService;

    public PlannerController(PlannerService plannerService) {
        this.plannerService = plannerService;
    }

    @GetMapping
    public TaskPlan createPlan(@RequestParam String request) {
        return plannerService.createPlan(request);
    }
}