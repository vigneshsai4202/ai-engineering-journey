package com.vignesh.ai.day44_spring_ai_rag_observability.service;

import io.micrometer.core.instrument.Timer;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
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

    public RagService(
            VectorStore vectorStore,
            ChatClient.Builder chatClientBuilder,
            RagMetricsService metricsService) {

        this.vectorStore = vectorStore;
        this.chatClient = chatClientBuilder.build();
        this.metricsService = metricsService;
    }

    public String ask(String question) {

        long requestStart = System.currentTimeMillis();

        metricsService.request();

        try {

            // =====================================
            // 1. Retrieve Documents
            // =====================================

            Timer.Sample retrievalTimer =
                    metricsService.startTimer();

            List<Document> documents =
                    vectorStore.similaritySearch(
                            SearchRequest.builder()
                                    .query(question)
                                    .topK(3)
                                    .build()
                    );

            metricsService.recordRetrieval(
                    retrievalTimer
            );

            long retrievalTime =
                    System.currentTimeMillis() - requestStart;

            System.out.println("\n========== RAG OBSERVABILITY ==========");

            System.out.println(
                    "Question: " + question
            );

            System.out.println(
                    "Retrieved Documents: "
                            + documents.size()
            );

            System.out.println(
                    "Retrieval Latency: "
                            + retrievalTime
                            + " ms"
            );


            // =====================================
            // 2. Log Retrieved Documents
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
                        "Document "
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
            // 3. Build Context
            // =====================================

            String context =
                    documents.stream()
                            .map(Document::getText)
                            .collect(
                                    Collectors.joining("\n\n")
                            );


            // =====================================
            // 4. Call LLM
            // =====================================

            Timer.Sample llmTimer =
                    metricsService.startTimer();

            String answer =
                    chatClient.prompt()

                            .system("""
                                    You are a helpful RAG assistant.

                                    Answer ONLY using the
                                    provided context.

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
            // 5. Total Latency
            // =====================================

            long totalTime =
                    System.currentTimeMillis()
                            - requestStart;

            System.out.println(
                    "LLM Latency: "
                            + totalTime
                            + " ms"
            );

            System.out.println(
                    "Total Request Latency: "
                            + totalTime
                            + " ms"
            );

            System.out.println(
                    "=======================================\n"
            );


            // =====================================
            // 6. Clean Qwen <think> Output
            // =====================================

            return cleanResponse(answer);

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

                // Truncated thinking response
                return "";
            }
        }

        return response.trim();
    }
}