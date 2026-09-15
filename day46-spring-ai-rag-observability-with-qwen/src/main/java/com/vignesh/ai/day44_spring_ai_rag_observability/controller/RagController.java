package com.vignesh.ai.day44_spring_ai_rag_observability.controller;

import com.vignesh.ai.day44_spring_ai_rag_observability.service.RagService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rag")
public class RagController {

    private final RagService ragService;

    public RagController(RagService ragService) {
        this.ragService = ragService;
    }

    @GetMapping("/ask")
    public String ask(
            @RequestParam String question) {

        return ragService.ask(question);
    }
}