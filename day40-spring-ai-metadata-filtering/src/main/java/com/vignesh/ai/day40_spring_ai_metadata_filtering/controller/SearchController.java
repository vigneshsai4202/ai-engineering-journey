package com.vignesh.ai.day40_spring_ai_metadata_filtering.controller;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vignesh.ai.day40_spring_ai_metadata_filtering.service.MetadataSearchService;

@RestController
public class SearchController {

    private final MetadataSearchService searchService;

    public SearchController(MetadataSearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/search")
    public List<Document> search(
            @RequestParam String question) {

        return searchService.search(question);
    }
}