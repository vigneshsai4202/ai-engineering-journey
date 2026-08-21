package com.vignesh.enterprise_ai_assistant.service;

import java.util.List;

import org.springframework.stereotype.Service;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;

@Service
public class VectorStoreService {

    private final EmbeddingStore<TextSegment> embeddingStore;

    public VectorStoreService() {
        this.embeddingStore = new InMemoryEmbeddingStore<>();
    }

    public void store(
            List<Embedding> embeddings,
            List<TextSegment> segments) {

        embeddingStore.addAll(embeddings, segments);
    }

    public EmbeddingStore<TextSegment> getStore() {
        return embeddingStore;
    }
}