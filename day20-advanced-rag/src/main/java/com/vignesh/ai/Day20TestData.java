package com.vignesh.ai;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.huggingface.HuggingFaceEmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.ArrayList;
import java.util.List;

public class Day20TestData {

    public static void main(String[] args) {

        Dotenv dotenv = Dotenv.load();

        // -----------------------------------------
        // 1. BGE-M3
        // -----------------------------------------

        EmbeddingModel embeddingModel =
                HuggingFaceEmbeddingModel.builder()
                        .accessToken(dotenv.get("HF_API_KEY"))
                        .modelId("BAAI/bge-m3")
                        .build();

        System.out.println("✅ BGE-M3 ready!");

        // -----------------------------------------
        // 2. PGVector
        // -----------------------------------------

        EmbeddingStore<TextSegment> embeddingStore =
                PgVectorEmbeddingStore.builder()
                        .host(dotenv.get("NEON_HOST"))
                        .port(Integer.parseInt(
                                dotenv.get("NEON_PORT")))
                        .database(dotenv.get("NEON_DATABASE"))
                        .user(dotenv.get("NEON_USERNAME"))
                        .password(dotenv.get("NEON_PASSWORD"))
                        .table("day20_test_embeddings")
                        .dimension(1024)
                        .build();

        System.out.println("✅ Day 20 PGVector connected!");

        // -----------------------------------------
        // 3. Test documents
        // -----------------------------------------

        List<String> texts = List.of(

                "Spring Boot is a Java framework used to build production-ready applications.",

                "Spring Boot provides features such as dependency injection and auto-configuration.",

                "PostgreSQL is a relational database commonly used in backend applications.",

                "Docker packages applications and their dependencies into containers.",

                "Java is a strongly typed, object-oriented programming language."
        );

        // -----------------------------------------
        // 4. Create embeddings
        // -----------------------------------------

        List<Embedding> embeddings = new ArrayList<>();

        for (String text : texts) {

            Embedding embedding =
                    embeddingModel
                            .embed(text)
                            .content();

            embeddings.add(embedding);
        }

        // -----------------------------------------
        // 5. Store embeddings + metadata
        // -----------------------------------------

        for (int i = 0; i < texts.size(); i++) {

            TextSegment segment =
                    TextSegment.from(texts.get(i));

            segment.metadata().put(
                    "documentName",
                    "day20-test-document"
            );

            segment.metadata().put(
                    "chunkId",
                    String.valueOf(i)
            );

            embeddingStore.add(
                    embeddings.get(i),
                    segment
            );

            System.out.println(
                    "✅ Stored chunk " + i
            );
        }

        System.out.println(
                "\n🎉 Day 20 test data created!"
        );
    }
}