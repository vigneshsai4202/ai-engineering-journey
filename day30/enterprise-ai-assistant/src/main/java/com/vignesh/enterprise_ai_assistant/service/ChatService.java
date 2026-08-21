package com.vignesh.enterprise_ai_assistant.service;

import org.springframework.stereotype.Service;

import com.vignesh.enterprise_ai_assistant.ai.Assistant;

@Service
public class ChatService {

    private final Assistant assistant;

    public ChatService(Assistant assistant) {
        this.assistant = assistant;
    }

    public String chat(String message) {
        return assistant.chat(message);
    }
}