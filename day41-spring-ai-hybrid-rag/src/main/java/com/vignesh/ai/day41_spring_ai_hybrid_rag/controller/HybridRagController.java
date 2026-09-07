package com.vignesh.ai.day41_spring_ai_hybrid_rag.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vignesh.ai.day41_spring_ai_hybrid_rag.service.HybridRagService;

@RestController
public class HybridRagController {

    private final HybridRagService hybridRagService;

    public HybridRagController(HybridRagService hybridRagService) {
        this.hybridRagService = hybridRagService;
    }

    @GetMapping("/rag/ask")
    public String ask(@RequestParam String question) {
        return hybridRagService.ask(question);
    }
}