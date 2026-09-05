package com.vignesh.ai.day39_spring_ai_query_rewriting.service;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final ChatClient chatClient;
    private final ChatMemory chatMemory;
    private final QueryRewriter queryRewriter;
    private final VectorStore vectorStore;

    public ChatService(
            ChatClient.Builder chatClientBuilder,
            ChatMemory chatMemory,
            QueryRewriter queryRewriter,
            VectorStore vectorStore) {

        this.chatClient = chatClientBuilder.build();
        this.chatMemory = chatMemory;
        this.queryRewriter = queryRewriter;
        this.vectorStore = vectorStore;
    }

    public String chat(String question, String conversationId) {

        // 1. Store user question
        chatMemory.add(
                conversationId,
                new UserMessage(question)
        );

        // 2. Rewrite the question
        String rewrittenQuery =
                queryRewriter.rewrite(question, conversationId);

        // 3. Search PGVector
        List<Document> documents = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(rewrittenQuery)
                        .topK(2)
                        .similarityThreshold(0.5)
                        .build()
        );

        // 4. Build context
        String context = documents.stream()
                .map(Document::getText)
                .reduce("", (a, b) -> a + "\n\n" + b);

        // 5. Generate final answer
        String answer = chatClient.prompt()
                .system("""
                        Answer the question using ONLY the provided context.

                        If the answer is not available in the context,
                        say "I don't know."

                        Keep the answer concise.

                        Context:
                        %s
                        """.formatted(context))
                .user(question)
                .call()
                .content();

        // 6. Remove Qwen thinking tags
        return answer
                .replaceAll("(?s)<think>.*?</think>", "")
                .trim();
    }
}