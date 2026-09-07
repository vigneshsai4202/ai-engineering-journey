package com.vignesh.ai.day41_spring_ai_hybrid_rag.controller;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vignesh.ai.day41_spring_ai_hybrid_rag.service.HybridSearchService;

@RestController
public class HybridSearchController {

    private final HybridSearchService searchService;

    public HybridSearchController(HybridSearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/search")
    public List<Document> search(@RequestParam String question) {
        return searchService.search(question);
    }
}