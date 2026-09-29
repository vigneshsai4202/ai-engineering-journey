package com.vignesh.ai.day49.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import com.vignesh.ai.day49.model.DocumentRequest;

@Service
public class DocumentIngestionService {

    private final VectorStore vectorStore;
    private final KnowledgeSourceRegistry registry;

    public DocumentIngestionService(
            VectorStore vectorStore,
            KnowledgeSourceRegistry registry) {

        this.vectorStore = vectorStore;
        this.registry = registry;
    }

    public void addDocument(DocumentRequest request) {

        boolean sourceExists = registry.getSources()
                .stream()
                .anyMatch(source ->
                        source.id().equals(request.sourceId()));

        if (!sourceExists) {
            throw new IllegalArgumentException(
                    "Unknown knowledge source: "
                            + request.sourceId()
            );
        }

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("sourceId", request.sourceId());

        Document document = new Document(
                request.content(),
                metadata
        );

        vectorStore.add(List.of(document));
    }
}