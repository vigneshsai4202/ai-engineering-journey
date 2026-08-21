package com.vignesh.enterprise_ai_assistant.service;

import java.util.List;

import org.springframework.stereotype.Service;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;

@Service
public class VectorSearchService {

    private final EmbeddingService embeddingService;
    private final VectorStoreService vectorStoreService;

    public VectorSearchService(
            EmbeddingService embeddingService,
            VectorStoreService vectorStoreService) {

        this.embeddingService = embeddingService;
        this.vectorStoreService = vectorStoreService;
    }

    public List<EmbeddingMatch<TextSegment>> search(String query) {

        Embedding queryEmbedding =
                embeddingService.generateEmbedding(query);

        EmbeddingStore<TextSegment> store =
                vectorStoreService.getStore();

        EmbeddingSearchRequest request =
                EmbeddingSearchRequest.builder()
                        .queryEmbedding(queryEmbedding)
                        .maxResults(3)
                        .build();

        return store.search(request).matches();
    }
}