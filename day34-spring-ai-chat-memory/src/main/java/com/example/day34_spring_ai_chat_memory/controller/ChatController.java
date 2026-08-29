package com.example.day34_spring_ai_chat_memory.controller;


import com.example.day34_spring_ai_chat_memory.service.ChatService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/api/chat")
    public String chat(
            @RequestParam String conversationId,
            @RequestParam String message) {

        return chatService.chat(conversationId, message);
    }
}
