package com.vignesh.ai.day48_spring_ai_secure_rag.controller;

import com.vignesh.ai.day48_spring_ai_secure_rag.service.SecureRagService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rag")
public class RagController {

    private final SecureRagService ragService;

    public RagController(SecureRagService ragService) {
        this.ragService = ragService;
    }

    @GetMapping("/ask")
    public String ask(
            @RequestParam String userId,
            @RequestParam String question
    ) {

        return ragService.ask(
                userId,
                question
        );
    }
}