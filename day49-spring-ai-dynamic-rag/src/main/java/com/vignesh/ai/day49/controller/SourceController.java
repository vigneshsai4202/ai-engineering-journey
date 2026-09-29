package com.vignesh.ai.day49.controller;

import com.vignesh.ai.day49.model.KnowledgeSource;
import com.vignesh.ai.day49.service.KnowledgeSourceRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sources")
public class SourceController {

    private final KnowledgeSourceRegistry registry;

    public SourceController(KnowledgeSourceRegistry registry) {
        this.registry = registry;
    }

    @GetMapping
    public List<KnowledgeSource> getSources() {
        return registry.getSources();
    }
}