package com.vignesh.ai.day49.controller;

import com.vignesh.ai.day49.model.QueryPlan;
import com.vignesh.ai.day49.service.QueryPlanner;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlannerController {

    private final QueryPlanner queryPlanner;

    public PlannerController(QueryPlanner queryPlanner) {
        this.queryPlanner = queryPlanner;
    }

    @GetMapping("/plan")
    public QueryPlan plan(@RequestParam String question) {
        return queryPlanner.plan(question);
    }
}