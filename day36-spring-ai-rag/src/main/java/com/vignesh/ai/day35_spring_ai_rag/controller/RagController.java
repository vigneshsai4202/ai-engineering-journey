package com.vignesh.ai.day35_spring_ai_rag.controller;

import com.vignesh.ai.day35_spring_ai_rag.model.RagResponse;
import com.vignesh.ai.day35_spring_ai_rag.service.RagService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RagController {

    private final RagService ragService;

    public RagController(RagService ragService) {
        this.ragService = ragService;
    }

    @GetMapping("/api/rag")
    public RagResponse ask(@RequestParam String question) {
        return ragService.ask(question);
    }
}