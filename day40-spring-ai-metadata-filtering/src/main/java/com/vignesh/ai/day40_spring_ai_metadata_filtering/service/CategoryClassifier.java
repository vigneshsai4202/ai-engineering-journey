package com.vignesh.ai.day40_spring_ai_metadata_filtering.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class CategoryClassifier {

    private final ChatClient chatClient;

    public CategoryClassifier(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public String classify(String question) {

        return chatClient.prompt()
                .system("""
                        Classify the user's question into exactly one category.

                        Allowed categories:
                        java
                        spring
                        ai

                        Return ONLY the category name.
                        Do not explain.
                        """)
                .user(question)
                .call()
                .content()
                .replaceAll("(?s)<think>.*?</think>", "")
                .trim()
                .toLowerCase();
    }
}