package com.vignesh.ai.day48_spring_ai_secure_rag.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SecureRagService {

    private final VectorStore vectorStore;
    private final ChatClient chatClient;

    public SecureRagService(
            VectorStore vectorStore,
            ChatClient.Builder chatClientBuilder
    ) {
        this.vectorStore = vectorStore;
        this.chatClient = chatClientBuilder.build();
    }

    public String ask(
            String userId,
            String question
    ) {

        // 1. Secure vector search
        SearchRequest searchRequest = SearchRequest.builder()
                .query(question)
                .topK(5)
                .similarityThreshold(0.5)
                .filterExpression(
                        "userId == '" + userId + "'"
                )
                .build();

        List<Document> documents =
                vectorStore.similaritySearch(searchRequest);

        // 2. No relevant documents
        if (documents.isEmpty()) {
            return "I don't know based on the available documents.";
        }

        // 3. Build context
        String context = documents.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n\n"));

        // 4. Ask Qwen using only authorized context
        String answer = chatClient.prompt()
                .system("""
                        You are a secure RAG assistant.

                        Answer the question using ONLY the provided context.

                        If the answer is not present in the context,
                        say you don't know.

                        Do not use information from outside the context.
                        """)
                .user("""
                        Context:
                        %s

                        Question:
                        %s
                        """.formatted(context, question))
                .call()
                .content();

        return answer;
    }
}