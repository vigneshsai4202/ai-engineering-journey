package com.vignesh.ai.day39_spring_ai_query_rewriting.conroller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vignesh.ai.day39_spring_ai_query_rewriting.service.ChatService;

@RestController
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/chat")
    public String chat(
            @RequestParam String question,
            @RequestParam(defaultValue = "user1") String conversationId) {

        return chatService.chat(question, conversationId);
    }
}