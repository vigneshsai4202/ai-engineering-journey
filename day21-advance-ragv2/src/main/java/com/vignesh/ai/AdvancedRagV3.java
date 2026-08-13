package com.vignesh.ai;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.huggingface.HuggingFaceEmbeddingModel;
import dev.langchain4j.model.jina.JinaScoringModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.scoring.ScoringModel;
import dev.langchain4j.rag.query.Query;
import dev.langchain4j.rag.query.transformer.ExpandingQueryTransformer;
import dev.langchain4j.rag.query.transformer.QueryTransformer;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AdvancedRagV3 {

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

        System.out.println("✅ BGE-M3 ready!");

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

        System.out.println("✅ PGVector connected!");

        // =====================================================
        // 3. GROQ
        // =====================================================

        ChatModel chatModel =
                OpenAiChatModel.builder()
                        .apiKey(dotenv.get("GROQ_API_KEY"))
                        .baseUrl("https://api.groq.com/openai/v1")
                        .modelName("llama-3.3-70b-versatile")
                        .build();

        System.out.println("✅ Groq ready!");

        // =====================================================
        // 4. JINA RERANKER
        // =====================================================

        ScoringModel reranker =
                JinaScoringModel.builder()
                        .apiKey(dotenv.get("JINA_API_KEY"))
                        .modelName("jina-reranker-v2-base-multilingual")
                        .build();

        System.out.println("✅ Jina reranker ready!");

        // =====================================================
        // 5. LANGCHAIN4J QUERY TRANSFORMER
        // =====================================================

        QueryTransformer queryTransformer =
                ExpandingQueryTransformer.builder()
                        .chatModel(chatModel)
                        .n(3)
                        .build();

        System.out.println(
                "✅ LangChain4j QueryTransformer ready!"
        );

        // =====================================================
        // 6. USER QUESTION
        // =====================================================

        String question =
                "What is Spring Boot used for?";

        System.out.println(
                "\n===== Original Question ====="
        );

        System.out.println(question);

        // =====================================================
        // 7. CREATE LANGCHAIN4J QUERY
        // =====================================================

        Query originalQuery =
                Query.from(question);

        // =====================================================
        // 8. QUERY TRANSFORMATION
        // =====================================================

        Collection<Query> transformedQueries =
                queryTransformer.transform(
                        originalQuery
                );

        System.out.println(
                "\n===== Transformed Queries ====="
        );

        int queryNumber = 1;

        for (Query query :
                transformedQueries) {

            System.out.println(
                    queryNumber
                            + ". "
                            + query.text()
            );

            queryNumber++;
        }

        // =====================================================
        // 9. CONVERT QUERY OBJECTS TO TEXT SEGMENTS
        // =====================================================

        List<Query> queryList =
                new ArrayList<>(
                        transformedQueries
                );

        List<TextSegment> querySegments =
                new ArrayList<>();

        for (Query query : queryList) {

            querySegments.add(
                    TextSegment.from(
                            query.text()
                    )
            );
        }

        // =====================================================
        // 10. EMBED ALL QUERIES
        // =====================================================

        System.out.println(
                "\n===== Embedding Queries ====="
        );

        var embeddingResponse =
                embeddingModel.embedAll(
                        querySegments
                );

        List<Embedding> embeddings =
                embeddingResponse.content();

        System.out.println(
                "✅ Embedded "
                        + embeddings.size()
                        + " queries"
        );

        // =====================================================
        // 11. RETRIEVE FROM PGVECTOR
        // =====================================================

        Map<String, TextSegment> uniqueChunks =
                new LinkedHashMap<>();

        System.out.println(
                "\n===== Vector Retrieval ====="
        );

        for (int i = 0;
             i < queryList.size();
             i++) {

            Query query =
                    queryList.get(i);

            Embedding queryEmbedding =
                    embeddings.get(i);

            System.out.println(
                    "\nSearching: "
                            + query.text()
            );

            EmbeddingSearchRequest searchRequest =
                    EmbeddingSearchRequest.builder()
                            .queryEmbedding(
                                    queryEmbedding
                            )
                            .maxResults(5)
                            .minScore(0.0)
                            .build();

            var searchResult =
                    embeddingStore.search(
                            searchRequest
                    );

            for (var match :
                    searchResult.matches()) {

                TextSegment segment =
                        match.embedded();

                if (segment == null) {
                    continue;
                }

                String chunkId =
                        segment.metadata()
                                .getString(
                                        "chunkId"
                                );

                if (chunkId == null) {

                    chunkId =
                            segment.text();
                }

                // Remove duplicate chunks
                // returned by different queries
                uniqueChunks.put(
                        chunkId,
                        segment
                );
            }
        }

        // =====================================================
        // 12. AGGREGATED CANDIDATES
        // =====================================================

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
        // 13. JINA RERANKING
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
        // 14. SELECT BEST CONTEXT
        // =====================================================

        int contextLimit =
                Math.min(
                        2,
                        reranked.size()
                );

        StringBuilder context =
                new StringBuilder();

        System.out.println(
                "\n===== Jina Reranked Results ====="
        );

        for (int i = 0;
             i < contextLimit;
             i++) {

            RerankedChunk result =
                    reranked.get(i);

            TextSegment segment =
                    result.segment();

            System.out.println(
                    "\nRank: "
                            + (i + 1)
            );

            System.out.println(
                    "Reranker Score: "
                            + result.score()
            );

            System.out.println(
                    "Document: "
                            + segment.metadata()
                                    .getString(
                                            "documentName"
                                    )
            );

            System.out.println(
                    "Chunk ID: "
                            + segment.metadata()
                                    .getString(
                                            "chunkId"
                                    )
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

            context.append(
                    "\n\n"
            );
        }

        // =====================================================
        // 15. FINAL RAG PROMPT
        // =====================================================

        String ragPrompt =
                """
                You are a helpful AI assistant.

                Answer the question using ONLY the provided context.

                If the answer cannot be found in the context,
                say that the information is not available.

                Context:
                %s

                Question:
                %s

                Give a concise answer.
                """.formatted(
                        context,
                        question
                );

        // =====================================================
        // 16. GROQ FINAL ANSWER
        // =====================================================

        String answer =
                chatModel.chat(
                        ragPrompt
                );

        System.out.println(
                "\n===== Final RAG Answer ====="
        );

        System.out.println(answer);

        // =====================================================
        // 17. SOURCES
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
    // RERANKED CHUNK
    // =========================================================

    record RerankedChunk(
            TextSegment segment,
            Double score
    ) {
    }
}