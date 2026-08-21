package com.vignesh.enterprise_ai_assistant.service;

import java.util.List;

import org.springframework.stereotype.Service;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;

@Service
public class EmbeddingService {

    private final EmbeddingModel embeddingModel;

    public EmbeddingService() {
        this.embeddingModel = new AllMiniLmL6V2EmbeddingModel();
    }

    public List<Embedding> generateEmbeddings(List<TextSegment> segments) {

        return embeddingModel.embedAll(segments).content();
    }
    public Embedding generateEmbedding(String text) {

        return embeddingModel.embed(text).content();
    }
}