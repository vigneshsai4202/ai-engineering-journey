package com.vignesh.ai.day44_spring_ai_rag_observability.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RagCacheService {

    private final Map<String, String> cache =
            new ConcurrentHashMap<>();

    public String get(String question) {

        return cache.get(normalize(question));
    }

    public void put(String question, String answer) {

        cache.put(
                normalize(question),
                answer
        );
    }

    public boolean contains(String question) {

        return cache.containsKey(
                normalize(question)
        );
    }

    private String normalize(String question) {

        return question
                .trim()
                .toLowerCase()
                .toLowerCase()
                .replaceAll("[^a-z0-9\\s]", "")
                .replaceAll("\\s+", " ");
    }
}