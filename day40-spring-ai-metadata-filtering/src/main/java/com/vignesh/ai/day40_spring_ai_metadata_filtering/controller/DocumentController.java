package com.vignesh.ai.day40_spring_ai_metadata_filtering.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vignesh.ai.day40_spring_ai_metadata_filtering.service.DocumentIngestionService;

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