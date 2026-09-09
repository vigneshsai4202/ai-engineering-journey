package com.vignesh.ai.day43_spring_ai_rag_evaluation.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RagEvaluationService {

    private final VectorStore vectorStore;
    private final ChatClient chatClient;

    public RagEvaluationService(
            VectorStore vectorStore,
            ChatClient.Builder chatClientBuilder) {

        this.vectorStore = vectorStore;
        this.chatClient = chatClientBuilder.build();
    }

    public String ask(String question) {

        // 1. Retrieve relevant documents from PGVector
        List<Document> documents = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(question)
                        .topK(3)
                        .build()
        );

        // 2. Build context from retrieved documents
        String context = documents.stream()
                .map(Document::getText)
                .reduce("", (a, b) -> a + "\n" + b);

        // 3. Send context + question to the LLM
        String response = chatClient.prompt()
                .system("""
                        Answer the question using only the provided context.

                        If the answer is not present in the context, say:
                        "I don't know based on the provided context."

                        Do not include your reasoning or thinking process.
                        """)
                .user("""
                        Context:
                        %s

                        Question:
                        %s
                        """.formatted(context, question))
                .call()
                .content();

        // 4. Remove Qwen <think>...</think> output
        return cleanResponse(response);
    }

    private String cleanResponse(String response) {

        if (response == null) {
            return "";
        }

        return response
                .replaceAll("(?s)<think>.*?</think>", "")
                .trim();
    }
}