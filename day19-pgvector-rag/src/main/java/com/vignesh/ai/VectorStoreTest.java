package com.vignesh.ai;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.List;

public class VectorStoreTest {

    public static void main(String[] args) {

        // Load environment variables
        Dotenv dotenv = Dotenv.load();

        // Create PGVector embedding store
        EmbeddingStore<TextSegment> embeddingStore =
                PgVectorEmbeddingStore.builder()
                        .host(dotenv.get("NEON_HOST"))
                        .port(Integer.parseInt(dotenv.get("NEON_PORT")))
                        .database(dotenv.get("NEON_DATABASE"))
                        .user(dotenv.get("NEON_USERNAME"))
                        .password(dotenv.get("NEON_PASSWORD"))
                        .table("day19_embeddings")
                        .dimension(3)
                        .build();

        // Create a dummy 3-dimensional embedding
        Embedding embedding = Embedding.from(
                new float[]{0.1f, 0.2f, 0.3f}
        );

        // Store the embedding
        embeddingStore.add(embedding);

        System.out.println("✅ Embedding stored successfully!");

        // Create query embedding
        Embedding queryEmbedding = Embedding.from(
                new float[]{0.1f, 0.2f, 0.3f}
        );

        // Build search request
        EmbeddingSearchRequest searchRequest =
                EmbeddingSearchRequest.builder()
                        .queryEmbedding(queryEmbedding)
                        .maxResults(1)
                        .minScore(0.0)
                        .build();

        // Perform similarity search
        EmbeddingSearchResult<TextSegment> searchResult =
                embeddingStore.search(searchRequest);

        // Get matches
        List<EmbeddingMatch<TextSegment>> matches =
                searchResult.matches();

        System.out.println("Number of results: " + matches.size());

        if (!matches.isEmpty()) {

            EmbeddingMatch<TextSegment> match = matches.get(0);

            System.out.println(
                    "Similarity score: " + match.score()
            );

            System.out.println("✅ Vector retrieval successful!");

        } else {

            System.out.println("❌ No similar vector found!");
        }
    }
}