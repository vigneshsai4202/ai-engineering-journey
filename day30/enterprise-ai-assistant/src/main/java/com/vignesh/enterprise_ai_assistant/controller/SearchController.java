package com.vignesh.enterprise_ai_assistant.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vignesh.enterprise_ai_assistant.service.VectorSearchService;

import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.data.segment.TextSegment;

@RestController
public class SearchController {

    private final VectorSearchService vectorSearchService;

    public SearchController(VectorSearchService vectorSearchService) {
        this.vectorSearchService = vectorSearchService;
    }

    @GetMapping("/api/search")
    public List<SearchResult> search(
            @RequestParam String query) {

        return vectorSearchService.search(query)
                .stream()
                .map(match -> new SearchResult(
                        match.score(),
                        match.embedded().text()
                ))
                .toList();
    }

    public record SearchResult(
            double score,
            String text
    ) {}
}