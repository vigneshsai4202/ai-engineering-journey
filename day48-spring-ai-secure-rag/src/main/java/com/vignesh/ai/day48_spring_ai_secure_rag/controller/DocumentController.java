package com.vignesh.ai.day48_spring_ai_secure_rag.controller;

import com.vignesh.ai.day48_spring_ai_secure_rag.service.DocumentIngestionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    private final DocumentIngestionService ingestionService;

    public DocumentController(
            DocumentIngestionService ingestionService
    ) {
        this.ingestionService = ingestionService;
    }

    @PostMapping
    public String addDocument(
            @RequestBody DocumentRequest request
    ) {

        ingestionService.addDocument(
                request.userId(),
                request.content(),
                request.category()
        );

        return "Document added successfully";
    }

    public record DocumentRequest(
            String userId,
            String content,
            String category
    ) {}
}