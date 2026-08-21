package com.vignesh.enterprise_ai_assistant.service;

import java.util.List;

import org.springframework.stereotype.Service;

import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.data.segment.TextSegment;

@Service
public class RagService {

    private final VectorSearchService vectorSearchService;
    private final ChatService chatService;

    public RagService(
            VectorSearchService vectorSearchService,
            ChatService chatService) {

        this.vectorSearchService = vectorSearchService;
        this.chatService = chatService;
    }

    public String answer(String question) {

        List<EmbeddingMatch<TextSegment>> matches =
                vectorSearchService.search(question);

        String context = matches.stream()
                .map(match -> match.embedded().text())
                .reduce("", (a, b) -> a + "\n" + b);

        String prompt = """
                Answer the question using ONLY the provided context.

                If the answer cannot be found in the context,
                say that the information is not available.

                Context:
                %s

                Question:
                %s
                """.formatted(context, question);

        return chatService.chat(prompt);
    }
}