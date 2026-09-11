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

    public RagService(
            VectorStore vectorStore,
            ChatClient.Builder chatClientBuilder,
            RagMetricsService metricsService,
            RerankerService rerankerService) {

        this.vectorStore = vectorStore;
        this.chatClient = chatClientBuilder.build();
        this.metricsService = metricsService;
        this.rerankerService = rerankerService;
    }

    public String ask(String question) {

        long requestStart = System.currentTimeMillis();

        metricsService.request();

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

            System.out.println("\n========== RAG OBSERVABILITY ==========");

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
            // 3. Reranking
            // =====================================

            Timer.Sample rerankTimer =
                    metricsService.startTimer();

            List<Document> rerankedDocuments =
                    rerankerService.rerank(
                            question,
                            documents
                    );

            long rerankTime =
                    System.currentTimeMillis()
                            - requestStart;

            System.out.println(
                    "Reranking completed."
            );

            System.out.println(
                    "Reranked Documents: "
                            + rerankedDocuments.size()
            );

            System.out.println(
                    "Reranking Time: "
                            + rerankTime
                            + " ms"
            );


            // =====================================
            // 4. Build Context
            // =====================================

            String context =
                    rerankedDocuments.stream()
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

                            // Groq/Qwen token limit
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
            // 6. Total Request Latency
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


            // =====================================
            // 7. Clean Qwen <think> Output
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

    private String cleanResponse(
            String response) {

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