package com.vignesh.ai.day49.service;

import com.vignesh.ai.day49.model.QueryPlan;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DynamicRagService {

    private final QueryPlanner queryPlanner;
    private final DynamicRetrievalService retrievalService;
    private final ChatClient chatClient;

    public DynamicRagService(
            QueryPlanner queryPlanner,
            DynamicRetrievalService retrievalService,
            ChatClient.Builder chatClientBuilder) {

        this.queryPlanner = queryPlanner;
        this.retrievalService = retrievalService;
        this.chatClient = chatClientBuilder.build();
    }

    public String ask(String question) {

        // 1. Dynamically select relevant sources
        QueryPlan plan = queryPlanner.plan(question);

        if (plan.sourceIds().isEmpty()) {
            return "I couldn't identify a relevant knowledge source.";
        }

        // 2. Retrieve documents from selected sources
        List<Document> documents =
                retrievalService.retrieve(
                        question,
                        plan.sourceIds()
                );

        if (documents.isEmpty()) {
            return "I don't know based on the available documents.";
        }

        // 3. Build context
        String context = documents.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n\n"));

        // 4. Generate grounded answer
        return chatClient.prompt()
                .system("""
                        You are a Dynamic RAG assistant.

                        Answer the user's question using ONLY the
                        provided context.

                        If the answer is not present in the context,
                        say that you don't know.

                        Do not use outside knowledge.
                        """)
                .user("""
                        Context:
                        %s

                        Question:
                        %s
                        """.formatted(context, question))
                .call()
                .content();
    }
}