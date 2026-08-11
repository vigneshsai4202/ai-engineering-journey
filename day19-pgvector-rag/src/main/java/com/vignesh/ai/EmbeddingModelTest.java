package com.vignesh.ai;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.huggingface.HuggingFaceEmbeddingModel;
import io.github.cdimascio.dotenv.Dotenv;

public class EmbeddingModelTest {

    public static void main(String[] args) {

        Dotenv dotenv = Dotenv.load();

        EmbeddingModel embeddingModel =
                HuggingFaceEmbeddingModel.builder()
                        .accessToken(dotenv.get("HF_API_KEY"))
                        .modelId("BAAI/bge-m3")
                        .build();

        String text = "Java is an object-oriented programming language.";

        Embedding embedding = embeddingModel.embed(text).content();

        System.out.println("✅ Embedding generated successfully!");
        System.out.println("Dimension: " + embedding.dimension());
    }
}