package com.vignesh.ai;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.huggingface.HuggingFaceEmbeddingModel;
import dev.langchain4j.model.jina.JinaScoringModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.scoring.ScoringModel;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AdvancedRagV2 {

    public static void main(String[] args) {

        Dotenv dotenv = Dotenv.load();

        // =====================================================
        // 1. BGE-M3 EMBEDDING MODEL
        // =====================================================

        EmbeddingModel embeddingModel =
                HuggingFaceEmbeddingModel.builder()
                        .accessToken(dotenv.get("HF_API_KEY"))
                        .modelId("BAAI/bge-m3")
                        .build();

        System.out.println("BGE-M3 ready!");

        // =====================================================
        // 2. PGVECTOR
        // =====================================================

        EmbeddingStore<TextSegment> embeddingStore =
                PgVectorEmbeddingStore.builder()
                        .host(dotenv.get("NEON_HOST"))
                        .port(Integer.parseInt(dotenv.get("NEON_PORT")))
                        .database(dotenv.get("NEON_DATABASE"))
                        .user(dotenv.get("NEON_USERNAME"))
                        .password(dotenv.get("NEON_PASSWORD"))
                        .table("day20_test_embeddings")
                        .dimension(1024)
                        .build();

        System.out.println("PGVector connected!");

        // =====================================================
        // 3. GROQ CHAT MODEL
        // =====================================================

        ChatModel chatModel =
                OpenAiChatModel.builder()
                        .apiKey(dotenv.get("GROQ_API_KEY"))
                        .baseUrl("https://api.groq.com/openai/v1")
                        .modelName("llama-3.3-70b-versatile")
                        .build();

        System.out.println("Groq ready!");

        // =====================================================
        // 4. JINA RERANKER
        // =====================================================

        ScoringModel reranker =
                JinaScoringModel.builder()
                        .apiKey(dotenv.get("JINA_API_KEY"))
                        .modelName("jina-reranker-v2-base-multilingual")
                        .build();

        System.out.println("Jina reranker ready!");

        // =====================================================
        // 5. USER QUESTION
        // =====================================================

        String question =
                "What is Spring Boot used for?";

        System.out.println("\n===== Original Question =====");
        System.out.println(question);

        // =====================================================
        // 6. QUERY EXPANSION
        // One question -> multiple search queries
        // =====================================================

        String expansionPrompt =
                "Generate exactly 3 different search queries for the "
                + "following question.\n\n"
                + "Make each query useful for semantic document retrieval.\n\n"
                + "Return ONLY the queries, one per line. "
                + "Do not number them.\n\n"
                + "Question:\n"
                + question;

        String expansionResponse =
                chatModel.chat(expansionPrompt);

        List<String> queries =
                new ArrayList<>();

        // Keep the original query
        queries.add(question);

        for (String line : expansionResponse.split("\\R")) {

            String query = line.trim();

            if (!query.isEmpty()) {
                queries.add(query);
            }

            // Original + 3 expanded queries
            if (queries.size() == 4) {
                break;
            }
        }

        System.out.println("\n===== Expanded Queries =====");

        for (String query : queries) {
            System.out.println("- " + query);
        }

        // =====================================================
        // 7. RETRIEVE FOR EACH QUERY
        // =====================================================

        Map<String, TextSegment> uniqueChunks =
                new LinkedHashMap<>();

        for (String query : queries) {

            System.out.println(
                    "\nSearching for: " + query
            );

            var queryEmbedding =
                    embeddingModel
                            .embed(query)
                            .content();

            EmbeddingSearchRequest searchRequest =
                    EmbeddingSearchRequest.builder()
                            .queryEmbedding(queryEmbedding)
                            .maxResults(5)
                            .minScore(0.0)
                            .build();

            var searchResult =
                    embeddingStore.search(searchRequest);

            for (var match : searchResult.matches()) {

                TextSegment segment =
                        match.embedded();

                if (segment == null) {
                    continue;
                }

                String chunkId =
                        segment.metadata()
                                .getString("chunkId");

                if (chunkId == null) {
                    chunkId = segment.text();
                }

                // Remove duplicate chunks returned
                // by different expanded queries.
                uniqueChunks.put(
                        chunkId,
                        segment
                );
            }
        }

        List<TextSegment> candidates =
                new ArrayList<>(
                        uniqueChunks.values()
                );

        System.out.println(
                "\n===== Aggregated Candidates ====="
        );

        System.out.println(
                "Unique candidates: "
                        + candidates.size()
        );

        // =====================================================
        // 8. JINA RERANKING
        // =====================================================

        var rerankResponse =
                reranker.scoreAll(
                        candidates,
                        question
                );

        List<Double> scores =
                rerankResponse.content();

        List<RerankedChunk> reranked =
                new ArrayList<>();

        for (int i = 0;
             i < candidates.size();
             i++) {

            reranked.add(
                    new RerankedChunk(
                            candidates.get(i),
                            scores.get(i)
                    )
            );
        }

        // Highest score first
        reranked.sort(
                (a, b) ->
                        Double.compare(
                                b.score(),
                                a.score()
                        )
        );

        // =====================================================
        // 9. SELECT BEST CONTEXT
        // =====================================================

        int contextLimit =
                Math.min(
                        2,
                        reranked.size()
                );

        StringBuilder context =
                new StringBuilder();

        System.out.println(
                "\n===== Reranked Results ====="
        );

        for (int i = 0;
             i < contextLimit;
             i++) {

            RerankedChunk item =
                    reranked.get(i);

            TextSegment segment =
                    item.segment();

            System.out.println(
                    "\nRank: " + (i + 1)
            );

            System.out.println(
                    "Reranker Score: "
                            + item.score()
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

            context.append(
                    segment.text()
            );

            context.append("\n\n");
        }

        // =====================================================
        // 10. FINAL RAG PROMPT
        // =====================================================

        String ragPrompt =
                "You are a helpful AI assistant.\n\n"
                + "Answer the question using ONLY the provided context.\n\n"
                + "If the answer is not available in the context, "
                + "say that the information is not available.\n\n"
                + "Context:\n"
                + context
                + "\nQuestion:\n"
                + question
                + "\n\nGive a concise answer.";

        // =====================================================
        // 11. GENERATE FINAL ANSWER
        // =====================================================

        String answer =
                chatModel.chat(ragPrompt);

        System.out.println(
                "\n===== Final RAG Answer ====="
        );

        System.out.println(answer);

        // =====================================================
        // 12. SOURCES
        // =====================================================

        System.out.println(
                "\n===== Sources ====="
        );

        for (int i = 0;
             i < contextLimit;
             i++) {

            TextSegment segment =
                    reranked
                            .get(i)
                            .segment();

            System.out.println(
                    "- "
                            + segment.metadata()
                                    .getString(
                                            "documentName"
                                    )
                            + " | Chunk "
                            + segment.metadata()
                                    .getString(
                                            "chunkId"
                                    )
            );
        }
    }

    // =========================================================
    // RERANKED RESULT HOLDER
    // =========================================================

    record RerankedChunk(
            TextSegment segment,
            Double score
    ) {
    }
}