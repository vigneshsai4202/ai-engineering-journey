package com.vignesh.ai.day41_spring_ai_hybrid_rag.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

@Service
public class HybridSearchService {

    private final VectorStore vectorStore;
    private final KeywordSearchService keywordSearchService;

    public HybridSearchService(
            VectorStore vectorStore,
            KeywordSearchService keywordSearchService) {

        this.vectorStore = vectorStore;
        this.keywordSearchService = keywordSearchService;
    }

    public List<Document> search(String question) {

        // 1. Semantic / Vector Search
        SearchRequest request = SearchRequest.builder()
                .query(question)
                .topK(3)
                .similarityThreshold(0.2)
                .build();

        List<Document> vectorResults =
                vectorStore.similaritySearch(request);

        // 2. Keyword Search
        List<Document> keywordResults =
                keywordSearchService.search(question);

        // 3. Combine both result sets
        List<Document> combined = new ArrayList<>();

        combined.addAll(vectorResults);
        combined.addAll(keywordResults);

        // 4. Remove duplicates
        Map<String, Document> uniqueDocuments = new LinkedHashMap<>();

        for (Document document : combined) {

            String id = document.getMetadata()
                    .getOrDefault("id", document.getId())
                    .toString();

            uniqueDocuments.putIfAbsent(id, document);
        }

        return new ArrayList<>(uniqueDocuments.values());
    }
}