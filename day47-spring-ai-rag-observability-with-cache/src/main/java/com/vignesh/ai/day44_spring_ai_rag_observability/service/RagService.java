package com.vignesh.ai.day44_spring_ai_rag_observability.service;

import io.micrometer.core.instrument.Timer;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RagService {

    private final VectorStore vectorStore;
    private final ChatClient chatClient;
    private final RagMetricsService metricsService;
    private final RerankerService rerankerService;
    private final RagCacheService cacheService;

    public RagService(
            VectorStore vectorStore,
            ChatClient.Builder chatClientBuilder,
            RagMetricsService metricsService,
            RerankerService rerankerService,
            RagCacheService cacheService) {

        this.vectorStore = vectorStore;
        this.chatClient = chatClientBuilder.build();
        this.metricsService = metricsService;
        this.rerankerService = rerankerService;
        this.cacheService = cacheService;
    }

    public String ask(String question) {

        long requestStart = System.currentTimeMillis();

        metricsService.request();

        // =====================================
        // 0. Cache Check
        // =====================================

        String cachedAnswer =
                cacheService.get(question);

        if (cachedAnswer != null) {

            System.out.println(
                    "\n========== RAG CACHE =========="
            );

            System.out.println(
                    "Cache HIT"
            );

            System.out.println(
                    "Question: " + question
            );

            System.out.println(
                    "Returning cached answer."
            );

            System.out.println(
                    "================================\n"
            );

            return cachedAnswer;
        }

        System.out.println(
                "\n========== RAG CACHE =========="
        );

        System.out.println(
                "Cache MISS"
        );

        System.out.println(
                "Question: " + question
        );

        System.out.println(
                "Running RAG pipeline..."
        );

        System.out.println(
                "================================\n"
        );

        try {

            // =====================================
            // 1. Vector Search
            // =====================================

            Timer.Sample retrievalTimer =
                    metricsService.startTimer();

            List<Document> documents =
                    vectorStore.similaritySearch(
                            SearchRequest.builder()
                                    .query(question)
                                    .topK(10)
                                    .build()
                    );

            metricsService.recordRetrieval(
                    retrievalTimer
            );

            System.out.println(
                    "\n========== RAG OBSERVABILITY =========="
            );

            System.out.println(
                    "Question: " + question
            );

            System.out.println(
                    "Vector Search Candidates: "
                            + documents.size()
            );


            // =====================================
            // 2. Display Vector Search Results
            // =====================================

            for (int i = 0; i < documents.size(); i++) {

                Document document = documents.get(i);

                String content = document.getText();

                String preview =
                        content.substring(
                                0,
                                Math.min(
                                        100,
                                        content.length()
                                )
                        );

                System.out.println(
                        "Candidate "
                                + (i + 1)
                                + ": "
                                + preview
                );

                System.out.println(
                        "Metadata: "
                                + document.getMetadata()
                );
            }


            // =====================================
            // 3. Reranking + Fallback
            // =====================================

            List<Document> finalDocuments;

            try {

                long rerankStart =
                        System.currentTimeMillis();

                finalDocuments =
                        rerankerService.rerank(
                                question,
                                documents
                        );

                long rerankTime =
                        System.currentTimeMillis()
                                - rerankStart;

                System.out.println(
                        "Reranking completed successfully."
                );

                System.out.println(
                        "Reranking Latency: "
                                + rerankTime
                                + " ms"
                );

            } catch (Exception e) {

                System.out.println(
                        "Reranking failed."
                );

                System.out.println(
                        "Falling back to PGVector results."
                );

                System.out.println(
                        "Fallback Reason: "
                                + e.getMessage()
                );

                /*
                 * Fallback:
                 *
                 * Jina failed
                 *      ↓
                 * PGVector original ranking
                 *      ↓
                 * Top 3 documents
                 */

                finalDocuments =
                        documents.stream()
                                .limit(3)
                                .toList();

                System.out.println(
                        "Fallback Documents: "
                                + finalDocuments.size()
                );
            }

            System.out.println(
                    "Final Documents Used: "
                            + finalDocuments.size()
            );


            // =====================================
            // 4. Build Context
            // =====================================

            String context =
                    finalDocuments.stream()
                            .map(Document::getText)
                            .collect(
                                    Collectors.joining(
                                            "\n\n"
                                    )
                            );


            // =====================================
            // 5. LLM Generation
            // =====================================

            Timer.Sample llmTimer =
                    metricsService.startTimer();

            String answer =
                    chatClient.prompt()

                            .options(
                                    OpenAiChatOptions.builder()
                                            .maxTokens(200)
                            )

                            .system("""
                                    You are a helpful RAG assistant.

                                    Answer ONLY using the
                                    provided context.

                                    Keep the answer concise.

                                    Do not use information
                                    outside the context.

                                    If the answer is not available
                                    in the context, say:

                                    I don't know based on
                                    the provided context.
                                    """)

                            .user("""
                                    Context:

                                    %s

                                    Question:

                                    %s
                                    """.formatted(
                                    context,
                                    question
                            ))

                            .call()

                            .content();

            metricsService.recordLlm(
                    llmTimer
            );


            // =====================================
            // 6. Clean Qwen <think> Output
            // =====================================

            String finalAnswer =
                    cleanResponse(answer);


            // =====================================
            // 7. Save Answer to Cache
            // =====================================

            cacheService.put(
                    question,
                    finalAnswer
            );

            System.out.println(
                    "Answer stored in RAG cache."
            );


            // =====================================
            // 8. Total Request Latency
            // =====================================

            long totalTime =
                    System.currentTimeMillis()
                            - requestStart;

            System.out.println(
                    "LLM completed."
            );

            System.out.println(
                    "Total Request Latency: "
                            + totalTime
                            + " ms"
            );

            System.out.println(
                    "=======================================\n"
            );


            return finalAnswer;

        } catch (Exception e) {

            metricsService.error();

            System.err.println(
                    "RAG Error: "
                            + e.getMessage()
            );

            throw new RuntimeException(
                    "RAG request failed",
                    e
            );
        }
    }


    // =========================================
    // Remove Qwen <think> blocks
    // =========================================

    private String cleanResponse(String response) {

        if (response == null) {
            return "";
        }

        int thinkStart =
                response.indexOf("<think>");

        if (thinkStart >= 0) {

            int thinkEnd =
                    response.indexOf("</think>");

            if (thinkEnd >= 0) {

                response =
                        response.substring(
                                thinkEnd
                                        + "</think>".length()
                        );

            } else {

                // Response was truncated
                // while Qwen was thinking

                return "";
            }
        }

        return response.trim();
    }
}