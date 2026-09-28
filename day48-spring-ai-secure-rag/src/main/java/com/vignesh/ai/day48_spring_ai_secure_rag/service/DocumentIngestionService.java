package com.vignesh.ai.day48_spring_ai_secure_rag.service;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DocumentIngestionService {

    private final VectorStore vectorStore;

    public DocumentIngestionService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public void addDocument(
            String userId,
            String content,
            String category
    ) {

        Map<String, Object> metadata = new HashMap<>();

        metadata.put("userId", userId);
        metadata.put("category", category);

        Document document = new Document(
                content,
                metadata
        );

        vectorStore.add(List.of(document));

        System.out.println("Document added successfully.");
        System.out.println("User ID: " + userId);
        System.out.println("Category: " + category);
    }
}