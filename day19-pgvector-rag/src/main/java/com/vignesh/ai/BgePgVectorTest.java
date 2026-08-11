package com.vignesh.ai;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.huggingface.HuggingFaceEmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import io.github.cdimascio.dotenv.Dotenv;

public class BgePgVectorTest {

    public static void main(String[] args) {

        // Load environment variables
        Dotenv dotenv = Dotenv.load();

        // --------------------------------------------------
        // 1. Create BGE-M3 embedding model
        // --------------------------------------------------

        EmbeddingModel embeddingModel =
                HuggingFaceEmbeddingModel.builder()
                        .accessToken(dotenv.get("HF_API_KEY"))
                        .modelId("BAAI/bge-m3")
                        .build();

        System.out.println("✅ BGE-M3 model created");

        // --------------------------------------------------
        // 2. Create PGVector store
        // --------------------------------------------------

        EmbeddingStore<TextSegment> embeddingStore =
                PgVectorEmbeddingStore.builder()
                        .host(dotenv.get("NEON_HOST"))
                        .port(Integer.parseInt(dotenv.get("NEON_PORT")))
                        .database(dotenv.get("NEON_DATABASE"))
                        .user(dotenv.get("NEON_USERNAME"))
                        .password(dotenv.get("NEON_PASSWORD"))
                        .table("day19_bge_embeddings")
                        .dimension(1024)
                        .build();

        System.out.println("✅ PGVector store created");

        // --------------------------------------------------
        // 3. Text to embed
        // --------------------------------------------------

        String text =
                "Java is an object-oriented programming language "
                + "used to build backend applications.";

        TextSegment segment = TextSegment.from(text);

        // --------------------------------------------------
        // 4. Generate BGE-M3 embedding
        // --------------------------------------------------

        Embedding embedding =
                embeddingModel.embed(segment.text()).content();

        System.out.println(
                "Embedding dimension: " + embedding.dimension()
        );

        // --------------------------------------------------
        // 5. Store embedding + text
        // --------------------------------------------------

        embeddingStore.add(embedding, segment);

        System.out.println(
                "✅ BGE-M3 embedding stored in Neon PostgreSQL!"
        );

        // --------------------------------------------------
        // 6. Create query
        // --------------------------------------------------

        String query =
                "What programming language is used for backend applications?";

        Embedding queryEmbedding =
                embeddingModel.embed(query).content();

        // --------------------------------------------------
        // 7. Similarity search
        // --------------------------------------------------

        var searchRequest =
                dev.langchain4j.store.embedding.EmbeddingSearchRequest
                        .builder()
                        .queryEmbedding(queryEmbedding)
                        .maxResults(3)
                        .minScore(0.0)
                        .build();

        var searchResult =
                embeddingStore.search(searchRequest);

        // --------------------------------------------------
        // 8. Display results
        // --------------------------------------------------

        System.out.println(
                "Number of results: "
                + searchResult.matches().size()
        );

        searchResult.matches().forEach(match -> {

            System.out.println("-----------------------------");

            System.out.println(
                    "Score: " + match.score()
            );

            if (match.embedded() != null) {
                System.out.println(
                        "Text: " + match.embedded().text()
                );
            }
        });

        System.out.println("-----------------------------");
        System.out.println("✅ BGE-M3 + PGVector test completed!");
    }
}