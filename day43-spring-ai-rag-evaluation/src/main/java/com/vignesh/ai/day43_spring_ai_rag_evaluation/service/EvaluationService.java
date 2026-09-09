package com.vignesh.ai.day43_spring_ai_rag_evaluation.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vignesh.ai.day43_spring_ai_rag_evaluation.model.EvaluationItem;
import com.vignesh.ai.day43_spring_ai_rag_evaluation.model.EvaluationReport;
import com.vignesh.ai.day43_spring_ai_rag_evaluation.model.EvaluationResult;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.ai.openai.OpenAiChatOptions;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class EvaluationService {

    private final VectorStore vectorStore;
    private final ChatClient chatClient;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public EvaluationService(
            VectorStore vectorStore,
            ChatClient.Builder chatClientBuilder) {

        this.vectorStore = vectorStore;
        this.chatClient = chatClientBuilder.build();
    }

    // Evaluate the complete dataset
    public EvaluationReport evaluateDataset() {

        List<EvaluationItem> dataset = loadDataset();

        List<EvaluationResult> results = new ArrayList<>();

        for (EvaluationItem item : dataset) {

            EvaluationResult result = evaluate(
                    item.question(),
                    item.expectedSource(),
                    item.expectedAnswer()
            );

            results.add(result);
        }

        double averagePrecision = results.stream()
                .mapToDouble(EvaluationResult::precision)
                .average()
                .orElse(0.0);

        double averageRecall = results.stream()
                .mapToDouble(EvaluationResult::recall)
                .average()
                .orElse(0.0);

        double averageReciprocalRank = results.stream()
                .mapToDouble(EvaluationResult::reciprocalRank)
                .average()
                .orElse(0.0);

        return new EvaluationReport(
                results,
                averagePrecision,
                averageRecall,
                averageReciprocalRank
        );
    }

    // Evaluate one question
    public EvaluationResult evaluate(
            String question,
            String expectedSource,
            String expectedAnswer) {

        List<Document> documents = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(question)
                        .topK(3)
                        .build()
        );

        int relevantDocuments = 0;
        int firstRelevantRank = -1;

        for (int i = 0; i < documents.size(); i++) {

            Document document = documents.get(i);

            String content = document.getText().toLowerCase();

            if (isRelevant(content, expectedSource)) {

                relevantDocuments++;

                if (firstRelevantRank == -1) {
                    firstRelevantRank = i + 1;
                }
            }
        }

        int retrievedDocuments = documents.size();

        // Precision@3
        double precision = retrievedDocuments == 0
                ? 0.0
                : (double) relevantDocuments / retrievedDocuments;

        // Recall
        double recall = relevantDocuments > 0
                ? 1.0
                : 0.0;

        // MRR
        double reciprocalRank = firstRelevantRank == -1
                ? 0.0
                : 1.0 / firstRelevantRank;

        // Build context
        String context = documents.stream()
                .map(Document::getText)
                .reduce("", (a, b) -> a + "\n" + b);

        // Generate actual RAG answer
        String answer = generateAnswer(question, context);

        return new EvaluationResult(
                question,
                retrievedDocuments,
                relevantDocuments,
                precision,
                recall,
                reciprocalRank,
                answer
        );
    }

    // Load evaluation-dataset.json
    private List<EvaluationItem> loadDataset() {

        try (InputStream inputStream =
                     getClass()
                             .getClassLoader()
                             .getResourceAsStream("evaluation-dataset.json")) {

            if (inputStream == null) {
                throw new IllegalStateException(
                        "evaluation-dataset.json not found"
                );
            }

            return objectMapper.readValue(
                    inputStream,
                    new TypeReference<List<EvaluationItem>>() {}
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to load evaluation dataset",
                    e
            );
        }
    }

    // Generate answer using retrieved context
    private String generateAnswer(
            String question,
            String context) {

        String response = chatClient.prompt()
                .system("""
                        You are a RAG assistant.

                        Answer the question using only the provided context.

                        If the answer is not present in the context, say:
                        "I don't know based on the provided context."

                        Do not include your reasoning or thinking process.

                        Keep the answer concise.
                        """)
                .user("""
                        Context:
                        %s

                        Question:
                        %s
                        """.formatted(context, question))
                .options(
                        OpenAiChatOptions.builder()
                        .maxTokens(500)
        )
                .call()
                .content();

        return cleanResponse(response);
    }

    // Remove Qwen thinking output
    private String cleanResponse(String response) {

        if (response == null) {
            return "";
        }

        return response
                .replaceAll("(?s)<think>.*?</think>", "")
                .trim();
    }

    // Determine whether a document is relevant
    private boolean isRelevant(
            String content,
            String expectedSource) {

        return switch (expectedSource.toLowerCase()) {

            case "rag" ->
                    content.contains("retrieval augmented generation")
                            || content.contains("(rag)");

            case "java" ->
                    content.contains("java");

            case "spring" ->
                    content.contains("spring");

            case "ai" ->
                    content.contains("artificial intelligence")
                            || content.contains("large language model")
                            || content.contains(" ai ");

            default -> false;
        };
    }
}