package com.vignesh.ai.day49.service;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DynamicRetrievalService {

    private final VectorStore vectorStore;

    public DynamicRetrievalService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public List<Document> retrieve(
            String question,
            List<String> sourceIds) {

        List<Document> results = new ArrayList<>();

        for (String sourceId : sourceIds) {

            SearchRequest request = SearchRequest.builder()
                    .query(question)
                    .topK(3)
                    .similarityThreshold(0.5)
                    .filterExpression(
                            "sourceId == '" + sourceId + "'"
                    )
                    .build();

            List<Document> documents =
                    vectorStore.similaritySearch(request);

            results.addAll(documents);
        }

        return results;
    }
}