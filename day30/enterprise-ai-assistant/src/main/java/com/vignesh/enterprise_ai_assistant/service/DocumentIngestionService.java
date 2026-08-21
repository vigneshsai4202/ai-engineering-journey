package com.vignesh.enterprise_ai_assistant.service;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;

@Service
public class DocumentIngestionService implements CommandLineRunner {

    private final DocumentLoaderService documentLoaderService;
    private final DocumentChunkerService documentChunkerService;
    private final EmbeddingService embeddingService;
    private final VectorStoreService vectorStoreService;

    public DocumentIngestionService(
            DocumentLoaderService documentLoaderService,
            DocumentChunkerService documentChunkerService,
            EmbeddingService embeddingService,
            VectorStoreService vectorStoreService) {

        this.documentLoaderService = documentLoaderService;
        this.documentChunkerService = documentChunkerService;
        this.embeddingService = embeddingService;
        this.vectorStoreService = vectorStoreService;
    }

    @Override
    public void run(String... args) {

        System.out.println("Starting document ingestion...");

        Document document = documentLoaderService.loadDocument();

        List<TextSegment> segments =
                documentChunkerService.chunk(document);

        List<Embedding> embeddings =
                embeddingService.generateEmbeddings(segments);

        vectorStoreService.store(embeddings, segments);

        System.out.println(
                "Document ingestion completed. Chunks: "
                + segments.size());
    }
}