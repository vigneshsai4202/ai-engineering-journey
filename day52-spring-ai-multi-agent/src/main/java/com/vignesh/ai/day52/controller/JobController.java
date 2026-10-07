package com.vignesh.ai.day52.controller;

import com.vignesh.ai.day52.agent.ManagerAgent;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/job")
public class JobController {

    private final ManagerAgent managerAgent;

    public JobController(ManagerAgent managerAgent) {
        this.managerAgent = managerAgent;
    }

    @PostMapping("/analyze")
    public String analyze(
            @RequestParam String skills,
            @RequestBody String jobDescription) {

        return managerAgent.analyze(
                jobDescription,
                skills
        );
    }
}