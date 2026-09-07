package com.vignesh.ai.day41_spring_ai_hybrid_rag.controller;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vignesh.ai.day41_spring_ai_hybrid_rag.service.KeywordSearchService;

@RestController
public class KeywordSearchController {

    private final KeywordSearchService keywordSearchService;

    public KeywordSearchController(KeywordSearchService keywordSearchService) {
        this.keywordSearchService = keywordSearchService;
    }

    @GetMapping("/keyword-search")
    public List<Document> search(@RequestParam String question) {
        return keywordSearchService.search(question);
    }
}