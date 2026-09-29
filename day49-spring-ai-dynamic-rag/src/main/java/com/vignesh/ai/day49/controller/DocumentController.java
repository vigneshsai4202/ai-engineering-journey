package com.vignesh.ai.day49.controller;

import com.vignesh.ai.day49.model.DocumentRequest;
import com.vignesh.ai.day49.service.DocumentIngestionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    private final DocumentIngestionService ingestionService;

    public DocumentController(
            DocumentIngestionService ingestionService) {

        this.ingestionService = ingestionService;
    }

    @PostMapping
    public String addDocument(
            @RequestBody DocumentRequest request) {

        ingestionService.addDocument(request);

        return "Document added successfully";
    }
}