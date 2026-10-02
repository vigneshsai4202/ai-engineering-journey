package com.vignesh.ai.day51.service;

import org.springframework.stereotype.Service;

@Service
public class WorkflowService {

    private final PlannerService plannerService;
    private final ExecutorService executorService;

    public WorkflowService(
            PlannerService plannerService,
            ExecutorService executorService) {

        this.plannerService = plannerService;
        this.executorService = executorService;
    }

    public String run(String request) {

        // Step 1: Create plan
        var plan = plannerService.createPlan(request);

        System.out.println("Plan created:");
        plan.steps().forEach(System.out::println);

        // Step 2: Execute plan
        return executorService.execute(
                plan.steps(),
                request
        );
    }
}