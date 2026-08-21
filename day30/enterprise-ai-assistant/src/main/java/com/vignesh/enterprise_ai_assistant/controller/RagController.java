package com.vignesh.enterprise_ai_assistant.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vignesh.enterprise_ai_assistant.service.RagService;

@RestController
public class RagController {

    private final RagService ragService;

    public RagController(RagService ragService) {
        this.ragService = ragService;
    }

    @GetMapping("/api/rag")
    public String ask(@RequestParam String question) {

        return ragService.answer(question);
    }
}