package com.vignesh.ai.day51.controller;

import com.vignesh.ai.day51.service.WorkflowService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/workflow")
public class WorkflowController {

    private final WorkflowService workflowService;

    public WorkflowController(WorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    @GetMapping
    public String run(@RequestParam String request) {
        return workflowService.run(request);
    }
}