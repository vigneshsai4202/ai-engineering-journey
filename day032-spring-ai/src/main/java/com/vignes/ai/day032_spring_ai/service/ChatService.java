package com.vignes.ai.day032_spring_ai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.ai.openai.OpenAiChatOptions;

@Service
public class ChatService {

    private final ChatClient chatClient;

    public ChatService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public String chat(String message) {		

        return chatClient
                .prompt()
                .user("Explain the following topic to a beginner: " + message)
                .system("You are a helpful Java and Spring AI instructor. Explain concepts simply with practical examples.")
                .options(OpenAiChatOptions.builder()
                        .temperature(0.2))
                .call()
                .content();
    }
}