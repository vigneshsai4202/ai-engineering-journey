package com.vignesh.ai.day41_spring_ai_hybrid_rag.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vignesh.ai.day41_spring_ai_hybrid_rag.service.DocumentIngestionService;

@RestController
public class DocumentController {

    private final DocumentIngestionService ingestionService;

    public DocumentController(DocumentIngestionService ingestionService) {
        this.ingestionService = ingestionService;
    }

    @GetMapping("/documents/load")
    public String loadDocuments() {
        return ingestionService.loadDocuments();
    }
}