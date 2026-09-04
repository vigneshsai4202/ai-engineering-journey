package com.vignesh.ai.day38_spring_ai_conversational_rag.service;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    public ChatService(
            ChatClient.Builder chatClientBuilder,
            ChatMemory chatMemory,
            VectorStore vectorStore) {

        this.chatClient = chatClientBuilder.build();
        this.vectorStore = vectorStore;
    }

    public String chat(String question, String conversationId) {

        List<Document> documents = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(question)
                        .topK(2)
                        .similarityThreshold(0.6)
                        .build()
        );

        String context = documents.stream()
                .map(Document::getText)
                .reduce("", (a, b) -> a + "\n" + b);

        return chatClient.prompt()
                .system("""
                        Answer briefly using the context below.
                        If the answer is not in the context, say "I don't know."

                        Context:
                        %s

                        Keep the answer under 50 words.
                        """.formatted(context))
                .advisors(advisorSpec -> advisorSpec
                        .param(ChatMemory.CONVERSATION_ID, conversationId))
                .user(question)
                .call()
                .content();
    }
}
    
