package com.vignesh.ai;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.huggingface.HuggingFaceEmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import io.github.cdimascio.dotenv.Dotenv;

public class PdfRetriever {

    public static void main(String[] args) {

        Dotenv dotenv = Dotenv.load();

        // -----------------------------------------
        // 1. Create BGE-M3 embedding model
        // -----------------------------------------

        EmbeddingModel embeddingModel =
                HuggingFaceEmbeddingModel.builder()
                        .accessToken(dotenv.get("HF_API_KEY"))
                        .modelId("BAAI/bge-m3")
                        .build();

        System.out.println(
                "✅ BGE-M3 model ready!"
        );

        // -----------------------------------------
        // 2. Connect to existing PGVector table
        // -----------------------------------------

        EmbeddingStore<TextSegment> embeddingStore =
                PgVectorEmbeddingStore.builder()
                        .host(dotenv.get("NEON_HOST"))
                        .port(
                                Integer.parseInt(
                                        dotenv.get("NEON_PORT")
                                )
                        )
                        .database(dotenv.get("NEON_DATABASE"))
                        .user(dotenv.get("NEON_USERNAME"))
                        .password(dotenv.get("NEON_PASSWORD"))
                        .table("day19_pdf_embeddings")
                        .dimension(1024)
                        .build();

        System.out.println(
                "✅ PGVector store connected!"
        );

        // -----------------------------------------
        // 3. User question
        // -----------------------------------------

        String question =
                "What is Spring Boot used for?";

        // -----------------------------------------
        // 4. Convert question into embedding
        // -----------------------------------------

        Embedding queryEmbedding =
                embeddingModel
                        .embed(question)
                        .content();

        // -----------------------------------------
        // 5. Create search request
        // -----------------------------------------

        var searchRequest =
                dev.langchain4j.store.embedding
                        .EmbeddingSearchRequest
                        .builder()
                        .queryEmbedding(queryEmbedding)
                        .maxResults(3)
                        .minScore(0.0)
                        .build();

        // -----------------------------------------
        // 6. Perform similarity search
        // -----------------------------------------

        var searchResult =
                embeddingStore.search(searchRequest);

        // -----------------------------------------
        // 7. Display retrieved chunks
        // -----------------------------------------

        System.out.println(
                "\n===== Retrieved Results ====="
        );

        searchResult.matches().forEach(match -> {

            System.out.println(
                    "Similarity Score: "
                            + match.score()
            );

            if (match.embedded() != null) {

                System.out.println(
                        "Content:"
                );

                System.out.println(
                        match.embedded().text()
                );
            }

            System.out.println(
                    "-----------------------------"
            );
        });
    }
}