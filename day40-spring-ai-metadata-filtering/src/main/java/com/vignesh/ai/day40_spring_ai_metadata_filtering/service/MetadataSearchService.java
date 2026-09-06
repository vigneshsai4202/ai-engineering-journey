package com.vignesh.ai.day40_spring_ai_metadata_filtering.service;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

@Service
public class MetadataSearchService {

    private final VectorStore vectorStore;
    private final CategoryClassifier categoryClassifier;

    public MetadataSearchService(
            VectorStore vectorStore,
            CategoryClassifier categoryClassifier) {

        this.vectorStore = vectorStore;
        this.categoryClassifier = categoryClassifier;
    }

    public List<Document> search(String question) {

        String category = categoryClassifier.classify(question);

        System.out.println("Question: " + question);
        System.out.println("Predicted category: [" + category + "]");

        SearchRequest request = SearchRequest.builder()
                .query(question)
                .topK(3)
                .similarityThreshold(0.2)
                .filterExpression("category == '" + category + "'")
                .build();

        List<Document> results = vectorStore.similaritySearch(request);

        System.out.println("Results found: " + results.size());

        return results;
    }
}