package com.vignesh.ai.day49.service;

import com.vignesh.ai.day49.model.KnowledgeSource;
import com.vignesh.ai.day49.model.QueryPlan;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class QueryPlanner {

    private final ChatClient chatClient;
    private final KnowledgeSourceRegistry registry;

    public QueryPlanner(
            ChatClient.Builder chatClientBuilder,
            KnowledgeSourceRegistry registry) {

        this.chatClient = chatClientBuilder.build();
        this.registry = registry;
    }

    public QueryPlan plan(String question) {

        String sourceCatalog = registry.getSources()
                .stream()
                .map(this::formatSource)
                .reduce("", (a, b) -> a + b);

        String result = chatClient.prompt()
                .system("""
                        You are a knowledge source router.

                        Choose the knowledge source IDs that can help
                        answer the user's question.

                        Available sources:
                        %s

                        IMPORTANT:
                        Return ONLY the IDs.
                        Return them as a comma-separated list.

                        Example:
                        java,spring

                        Do not return JSON.
                        Do not explain anything.
                        Do not return the source names.
                        """.formatted(sourceCatalog))
                .user(question)
                .call()
                .content();

        System.out.println("Planner raw response: " + result);

        String cleaned = result
                .replace("<think>", "")
                .replace("</think>", "")
                .trim()
                .toLowerCase();

        List<String> sourceIds = Arrays.stream(cleaned.split(","))
                .map(String::trim)
                .filter(id -> registry.getSources()
                        .stream()
                        .anyMatch(source -> source.id().equals(id)))
                .distinct()
                .toList();

        System.out.println("Selected sources: " + sourceIds);

        return new QueryPlan(sourceIds);
    }

    private String formatSource(KnowledgeSource source) {
        return """
                ID: %s
                Name: %s
                Description: %s

                """.formatted(
                source.id(),
                source.name(),
                source.description()
        );
    }
}