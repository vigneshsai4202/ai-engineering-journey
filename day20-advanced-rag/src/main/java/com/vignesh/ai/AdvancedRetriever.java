package com.vignesh.ai;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.huggingface.HuggingFaceEmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.filter.Filter;
import dev.langchain4j.store.embedding.filter.MetadataFilterBuilder;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import io.github.cdimascio.dotenv.Dotenv;

public class AdvancedRetriever {

    public static void main(String[] args) {

        Dotenv dotenv = Dotenv.load();

        // -----------------------------------------
        // 1. Embedding Model
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
                        .table("day19_pdf_embeddings")
                        .dimension(1024)
                        .build();

        System.out.println("✅ PGVector connected!");

        // -----------------------------------------
        // 3. User Question
        // -----------------------------------------

        String question =
                "What is Spring Boot used for?";

        // -----------------------------------------
        // 4. Convert question to embedding
        // -----------------------------------------

        Embedding queryEmbedding =
                embeddingModel
                        .embed(question)
                        .content();

        // -----------------------------------------
        // 5. Metadata Filter
        // -----------------------------------------

        Filter filter =
                MetadataFilterBuilder.metadataKey(
                        "documentName"
                ).isEqualTo(
                        "sample-java-notes.pdf"
                );

        // -----------------------------------------
        // 6. Advanced Search Request
        // -----------------------------------------

        EmbeddingSearchRequest searchRequest =
                EmbeddingSearchRequest.builder()
                        .queryEmbedding(queryEmbedding)

                        // Maximum number of results
                        .maxResults(5)

                        // Minimum similarity score
                        .minScore(0.70)

                        // Metadata filter
                        .filter(filter)

                        .build();

        // -----------------------------------------
        // 7. Execute Search
        // -----------------------------------------

        var result =
                embeddingStore.search(searchRequest);

        // -----------------------------------------
        // 8. Display Results
        // -----------------------------------------

        System.out.println(
                "\n===== Advanced Retrieval ====="
        );

        System.out.println(
                "Maximum results: 5"
        );

        System.out.println(
                "Minimum score: 0.70"
        );

        System.out.println(
                "Actual results: "
                        + result.matches().size()
        );

        System.out.println(
                "-----------------------------"
        );

        for (var match : result.matches()) {

            TextSegment segment =
                    match.embedded();

            System.out.println(
                    "Score: "
                            + match.score()
            );

            System.out.println(
                    "Document: "
                            + segment.metadata()
                                    .getString("documentName")
            );

            System.out.println(
                    "Chunk ID: "
                            + segment.metadata()
                                    .getString("chunkId")
            );

            System.out.println(
                    "Content:\n"
                            + segment.text()
            );

            System.out.println(
                    "-----------------------------"
            );
        }
    }
}