package com.vignesh.ai.day35_spring_ai_rag.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RagService {

    private final ChatClient chatClient;
    private final SimpleVectorStore vectorStore;

    public RagService(ChatClient.Builder builder,
                      SimpleVectorStore vectorStore) {

        this.chatClient = builder.build();
        this.vectorStore = vectorStore;
    }

    public String ask(String question) {

        List<Document> results =
                vectorStore.similaritySearch(question);

        String context = results.stream()
                .map(Document::getText)
                .reduce("", (a, b) -> a + "\n" + b);

        return chatClient
                .prompt()
                .system("""
                        Answer the question using only the provided context.
                        If the answer is not present in the context,
                        say that you don't know.

                        Context:
                        %s
                        """.formatted(context))
                .user(question)
                .call()
                .content();
    }
}