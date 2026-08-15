package com.vignesh.ai;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;

import dev.langchain4j.rag.query.Metadata;
import dev.langchain4j.rag.query.Query;
import dev.langchain4j.rag.query.transformer.CompressingQueryTransformer;
import dev.langchain4j.rag.query.transformer.QueryTransformer;

import io.github.cdimascio.dotenv.Dotenv;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class QueryCompressionTest {

    public static void main(String[] args) {

        // =====================================================
        // 1. LOAD ENVIRONMENT VARIABLES
        // =====================================================

        Dotenv dotenv = Dotenv.load();

        // =====================================================
        // 2. GROQ CHAT MODEL
        // =====================================================

        ChatModel chatModel =
                OpenAiChatModel.builder()
                
                        .baseUrl("https://api.groq.com/openai/v1")
                        .apiKey(dotenv.get("GROQ_API_KEY"))
                        .modelName("llama-3.3-70b-versatile")
                        .temperature(0.0)
                        .build();

        System.out.println("✅ Groq model ready!");

        // =====================================================
        // 3. PREVIOUS CONVERSATION
        // =====================================================

        List<ChatMessage> chatMemory =
                new ArrayList<>();

        chatMemory.add(
                UserMessage.from(
                        "Tell me about Spring Boot."
                )
        );

        chatMemory.add(
                AiMessage.from(
                        "Spring Boot is a Java framework used " +
                        "to build production-ready applications."
                )
        );

        // =====================================================
        // 4. FOLLOW-UP QUESTION
        // =====================================================

        String followUpQuestion =
                "What about its advantages?";

        System.out.println(
                "\n===== Previous Conversation ====="
        );

        for (ChatMessage message : chatMemory) {

            System.out.println(message);
        }

        System.out.println(
                "\n===== Original Query ====="
        );

        System.out.println(
                followUpQuestion
        );

        // =====================================================
        // 5. USER MESSAGE
        // =====================================================

        UserMessage userMessage =
                UserMessage.from(
                        followUpQuestion
                );

        // =====================================================
        // 6. RAG METADATA
        //
        // Metadata contains the original user message
        // and previous conversation history.
        // =====================================================

        Metadata metadata =
                Metadata.from(
                        userMessage,
                        null,
                        chatMemory
                );

        // =====================================================
        // 7. CREATE QUERY
        // =====================================================

        Query query =
                Query.from(
                        followUpQuestion,
                        metadata
                );

        // =====================================================
        // 8. CREATE QUERY TRANSFORMER
        // =====================================================

        QueryTransformer queryTransformer =
                new CompressingQueryTransformer(
                        chatModel
                );

        System.out.println(
                "\n✅ CompressingQueryTransformer ready!"
        );

        // =====================================================
        // 9. TRANSFORM QUERY
        // =====================================================

        Collection<Query> transformedQueries =
                queryTransformer.transform(
                        query
                );

        // =====================================================
        // 10. DISPLAY COMPRESSED QUERY
        // =====================================================

        System.out.println(
                "\n===== Compressed Query ====="
        );

        for (Query transformedQuery :
                transformedQueries) {

            System.out.println(
                    transformedQuery.text()
            );
        }

        // =====================================================
        // 11. FLOW
        // =====================================================

        System.out.println(
                "\n===== DAY 23 QUERY COMPRESSION ====="
        );

        System.out.println(
                "Conversation + Follow-up Query"
        );

        System.out.println(
                "            ↓"
        );

        System.out.println(
                "CompressingQueryTransformer"
        );

        System.out.println(
                "            ↓"
        );

        System.out.println(
                "Standalone Retrieval Query"
        );
    }
}