package com.vignesh.ai.day37_spring_ai_pgvector.controller;

import com.vignesh.ai.day37_spring_ai_pgvector.service.RagService;
import com.vignesh.ai.day37_spring_ai_pgvector.model.RagResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
@RestController
public class RagController {

    private final RagService ragService;

    public RagController(RagService ragService) {
        this.ragService = ragService;
    }

    @GetMapping("/rag/load")
    public String loadPdf() {
        return ragService.loadPdf();
        
        
    }
    @GetMapping("/rag/ask")
    public RagResponse ask(@RequestParam String question) {
        return ragService.ask(question);
    }
}