package com.vignesh.ai.day33_spring_ai_structed_output.controller;

import com.vignesh.ai.day33_spring_ai_structed_output.model.TopicResponse;
import com.vignesh.ai.day33_spring_ai_structed_output.service.ChatService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/api/topic")
    public TopicResponse explainTopic(@RequestParam String topic) {
        return chatService.explainTopic(topic);
    }
}