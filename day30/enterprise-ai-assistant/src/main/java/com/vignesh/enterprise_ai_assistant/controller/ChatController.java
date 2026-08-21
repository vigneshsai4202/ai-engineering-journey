package com.vignesh.enterprise_ai_assistant.controller;

import org.springframework.web.bind.annotation.*;

import com.vignesh.enterprise_ai_assistant.service.ChatService;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public String chat(@RequestBody String message) {

        return chatService.chat(message);
    }
}