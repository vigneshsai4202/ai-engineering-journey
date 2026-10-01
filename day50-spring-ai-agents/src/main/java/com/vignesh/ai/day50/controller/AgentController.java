package com.vignesh.ai.day50.controller;

import com.vignesh.ai.day50.service.AgentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AgentController {

    private final AgentService agentService;

    public AgentController(AgentService agentService) {
        this.agentService = agentService;
    }

    @GetMapping("/agent")
    public String ask(@RequestParam String question) {
        return agentService.ask(question);
    }
}