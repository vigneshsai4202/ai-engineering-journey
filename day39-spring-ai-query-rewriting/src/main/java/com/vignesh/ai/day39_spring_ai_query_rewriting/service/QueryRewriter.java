package com.vignesh.ai.day39_spring_ai_query_rewriting.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;

@Service
public class QueryRewriter {

    private final ChatClient chatClient;
    private final ChatMemory chatMemory;

    public QueryRewriter(
            ChatClient.Builder chatClientBuilder,
            ChatMemory chatMemory) {

        this.chatClient = chatClientBuilder.build();
        this.chatMemory = chatMemory;
    }

    public String rewrite(String question, String conversationId) {

        var messages = chatMemory.get(conversationId);

        String history = messages.stream()
                .map(message -> message.getMessageType() + ": " + message.getText())
                .reduce("", (a, b) -> a + "\n" + b);

        return chatClient.prompt()
                .system("""
                        Rewrite the user's question into a standalone search query.

                        Use the conversation history to resolve references
                        such as "it", "they", "this", or "that".

                        Do not answer the question.
                        Return ONLY the rewritten search query.

                        Conversation history:
                        %s
                        """.formatted(history))
                .user(question)
                .call()
                .content()
                .replaceAll("(?s)<think>.*?</think>", "")
                .trim();
    }
}