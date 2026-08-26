package com.vignesh.ai.day33_spring_ai_structed_output.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import com.vignesh.ai.day33_spring_ai_structed_output.model.TopicResponse;

@Service
public class ChatService {

    private final ChatClient chatClient;

    public ChatService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public TopicResponse explainTopic(String topic) {

        return chatClient
                .prompt()
                .system("""
                        You are a Java and Spring AI instructor.
                        Explain technical topics clearly for beginners.
                        """)
                .user("Explain this topic: " + topic)
                .call()
                .entity(TopicResponse.class);
    }
}