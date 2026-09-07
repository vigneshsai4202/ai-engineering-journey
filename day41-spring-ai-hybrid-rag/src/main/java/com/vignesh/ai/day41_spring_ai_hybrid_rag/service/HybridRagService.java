package com.vignesh.ai.day41_spring_ai_hybrid_rag.service;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;

@Service
public class HybridRagService {

    private final ChatClient chatClient;
    private final HybridSearchService hybridSearchService;

    public HybridRagService(
            ChatClient.Builder chatClientBuilder,
            HybridSearchService hybridSearchService) {

        this.chatClient = chatClientBuilder.build();
        this.hybridSearchService = hybridSearchService;
    }

    public String ask(String question) {

        List<Document> documents =
                hybridSearchService.search(question);

        if (documents.isEmpty()) {
            return "I could not find relevant information.";
        }

        String context = documents.stream()
                .map(Document::getText)
                .reduce("", (a, b) -> a + "\n\n" + b);

        return chatClient.prompt()
                .system("""
                        You are a helpful AI assistant.

                        Answer the user's question using ONLY the
                        information provided in the context.

                        If the answer is not present in the context,
                        say that you do not have enough information.

                        Context:
                        %s
                        """.formatted(context))
                .user(question)
                .call()
                .content()
                .replaceAll("(?s)<think>.*?</think>", "")
                .trim();
    }
}