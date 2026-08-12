package com.vignesh.ai;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.huggingface.HuggingFaceEmbeddingModel;
import dev.langchain4j.model.jina.JinaScoringModel;
import dev.langchain4j.model.scoring.ScoringModel;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.filter.Filter;
import dev.langchain4j.store.embedding.filter.MetadataFilterBuilder;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import dev.langchain4j.model.openai.OpenAiChatModel;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AdvancedRag {

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
                        .port(Integer.parseInt(
                                dotenv.get("NEON_PORT")))
                        .database(dotenv.get("NEON_DATABASE"))
                        .user(dotenv.get("NEON_USERNAME"))
                        .password(dotenv.get("NEON_PASSWORD"))
                        .table("day20_test_embeddings")
                        .dimension(1024)
                        .build();

        System.out.println("✅ PGVector connected!");

        // =====================================================
        // 3. JINA RERANKER
        // =====================================================

        ScoringModel reranker =
                JinaScoringModel.builder()
                        .apiKey(dotenv.get("JINA_API_KEY"))
                        .modelName(
                                "jina-reranker-v2-base-multilingual"
                        )
                        .build();

        System.out.println("✅ Jina reranker ready!");

        // =====================================================
        // 4. GROQ CHAT MODEL
        // =====================================================

        ChatModel chatModel =
                OpenAiChatModel.builder()
                        .apiKey(dotenv.get("GROQ_API_KEY"))
                        .baseUrl(
                                "https://api.groq.com/openai/v1"
                        )
                        .modelName(
                                "llama-3.3-70b-versatile"
                        )
                        .build();

        System.out.println("✅ Groq model ready!");

        // =====================================================
        // 5. USER QUESTION
        // =====================================================

        String question =
                "What is Spring Boot used for?";

        System.out.println(
                "\nQuestion: " + question
        );

        // =====================================================
        // 6. QUERY EMBEDDING
        // =====================================================

        Embedding queryEmbedding =
                embeddingModel
                        .embed(question)
                        .content();

        // =====================================================
        // 7. METADATA FILTER
        // =====================================================

        Filter filter =
                MetadataFilterBuilder
                        .metadataKey("documentName")
                        .isEqualTo("day20-test-document");

        // =====================================================
        // 8. VECTOR SEARCH
        // =====================================================

        EmbeddingSearchRequest searchRequest =
                EmbeddingSearchRequest.builder()
                        .queryEmbedding(queryEmbedding)

                        // Retrieve candidates
                        .maxResults(5)

                        // Ignore extremely weak matches
                        .minScore(0.0)

                        // Metadata filtering
                        .filter(filter)

                        .build();

        var searchResult =
                embeddingStore.search(searchRequest);

        System.out.println(
                "\n===== PGVector Candidates ====="
        );

        // =====================================================
        // 9. COLLECT CANDIDATES
        // =====================================================

        List<TextSegment> candidates =
                new ArrayList<>();

        int rank = 1;

        for (var match : searchResult.matches()) {

            TextSegment segment =
                    match.embedded();

            candidates.add(segment);

            System.out.println(
                    "Rank " + rank++
                            + " | Vector Score: "
                            + match.score()
            );

            System.out.println(
                    "Chunk: "
                            + segment.metadata()
                            .getString("chunkId")
            );

            System.out.println(
                    segment.text()
            );

            System.out.println(
                    "-----------------------------"
            );
        }

        // =====================================================
        // 10. RERANK
        // =====================================================

        var rerankResponse =
                reranker.scoreAll(
                        candidates,
                        question
                );

        List<Double> rerankScores =
                rerankResponse.content();

        // =====================================================
        // 11. COMBINE CHUNKS + RERANK SCORES
        // =====================================================

        List<RerankedChunk> rerankedChunks =
                new ArrayList<>();

        for (int i = 0;
             i < candidates.size();
             i++) {

            rerankedChunks.add(
                    new RerankedChunk(
                            candidates.get(i),
                            rerankScores.get(i)
                    )
            );
        }

        // =====================================================
        // 12. SORT BY RERANK SCORE
        // =====================================================

        rerankedChunks.sort(
                Comparator.comparing(
                        RerankedChunk::score
                ).reversed()
        );

        // =====================================================
        // 13. DISPLAY RERANKED RESULTS
        // =====================================================

        System.out.println(
                "\n===== Jina Reranked Results ====="
        );

        int rerankPosition = 1;

        for (RerankedChunk item :
                rerankedChunks) {

            TextSegment segment =
                    item.segment();

            System.out.println(
                    "\nRank: "
                            + rerankPosition++
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
        }

        // =====================================================
        // 14. SELECT BEST CONTEXT
        // =====================================================

        int contextLimit =
                Math.min(2, rerankedChunks.size());

        StringBuilder context =
                new StringBuilder();

        for (int i = 0;
             i < contextLimit;
             i++) {

            TextSegment segment =
                    rerankedChunks
                            .get(i)
                            .segment();

            context.append(
                    "Source: "
                            + segment.metadata()
                            .getString("documentName")
            );

            context.append("\n");

            context.append(
                    "Chunk: "
                            + segment.metadata()
                            .getString("chunkId")
            );

            context.append("\n");

            context.append(
                    segment.text()
            );

            context.append("\n\n");
        }

        // =====================================================
        // 15. RAG PROMPT
        // =====================================================

        String prompt = """
                You are a helpful AI assistant.

                Answer the question using ONLY the provided context.

                If the answer cannot be found in the context,
                say that the information is not available
                in the provided context.

                Context:
                %s

                Question:
                %s

                Provide a concise answer.
                """.formatted(
                        context,
                        question
                );

        // =====================================================
        // 16. GENERATE ANSWER
        // =====================================================

        String answer =
                chatModel.chat(prompt);

        // =====================================================
        // 17. FINAL OUTPUT
        // =====================================================

        System.out.println(
                "\n===== Final RAG Answer ====="
        );

        System.out.println(answer);

        System.out.println(
                "\n===== Sources ====="
        );

        for (int i = 0;
             i < contextLimit;
             i++) {

            TextSegment segment =
                    rerankedChunks
                            .get(i)
                            .segment();

            System.out.println(
                    "- "
                            + segment.metadata()
                            .getString("documentName")
                            + " | Chunk "
                            + segment.metadata()
                            .getString("chunkId")
            );
        }
    }

    // =========================================================
    // SMALL RECORD TO HOLD RERANKED RESULTS
    // =========================================================

    record RerankedChunk(
            TextSegment segment,
            Double score
    ) {
    }
}