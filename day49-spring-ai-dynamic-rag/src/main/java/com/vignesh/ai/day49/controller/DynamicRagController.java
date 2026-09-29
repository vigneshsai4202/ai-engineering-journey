package com.vignesh.ai.day49.controller;

import com.vignesh.ai.day49.service.DynamicRagService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DynamicRagController {

    private final DynamicRagService ragService;

    public DynamicRagController(DynamicRagService ragService) {
        this.ragService = ragService;
    }

    @GetMapping("/rag/ask")
    public String ask(@RequestParam String question) {
        return ragService.ask(question);
    }
}