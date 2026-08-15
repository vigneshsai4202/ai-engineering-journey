package com.vignesh.ai;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.huggingface.HuggingFaceEmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;

import io.github.cdimascio.dotenv.Dotenv;

public class HyDETest {

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
        // 3. BGE-M3 EMBEDDING MODEL
        // =====================================================

        EmbeddingModel embeddingModel =
                HuggingFaceEmbeddingModel.builder()
                        .accessToken(dotenv.get("HF_API_KEY"))
                        .modelId("BAAI/bge-m3")
                        .build();

        System.out.println("✅ BGE-M3 ready!");

        // =====================================================
        // 4. ORIGINAL QUESTION
        // =====================================================

        String question =
                "What are the advantages of using Spring Boot?";

        System.out.println(
                "\n===== Original Question ====="
        );

        System.out.println(question);

        // =====================================================
        // 5. HYDE PROMPT
        // =====================================================

        String prompt =
                """
                Write a short hypothetical document that directly
                answers the following question.

                Do not mention that the document is hypothetical.
                Write it like a factual technical document.

                Question:
                """ + question;

        // =====================================================
        // 6. GENERATE HYPOTHETICAL DOCUMENT
        // =====================================================

        String hypotheticalDocument =
                chatModel.chat(prompt);

        System.out.println(
                "\n===== Hypothetical Document ====="
        );

        System.out.println(
                hypotheticalDocument
        );

        // =====================================================
        // 7. EMBED HYPOTHETICAL DOCUMENT
        // =====================================================

        Embedding embedding =
                embeddingModel
                        .embed(hypotheticalDocument)
                        .content();

        System.out.println(
                "\n===== Hypothetical Document Embedding ====="
        );

        System.out.println(
                "Embedding dimension: "
                        + embedding.dimension()
        );

        // =====================================================
        // 8. DISPLAY FIRST 10 VALUES
        // =====================================================

        float[] vector =
                embedding.vector();

        System.out.println(
                "\n===== First 10 Embedding Values ====="
        );

        int limit =
                Math.min(10, vector.length);

        for (int i = 0; i < limit; i++) {

            System.out.println(
                    "[" + i + "] = " + vector[i]
            );
        }

        // =====================================================
        // 9. HYDE FLOW
        // =====================================================

        System.out.println(
                "\n===== DAY 23 HYDE COMPLETE ====="
        );

        System.out.println(
                "Question"
        );

        System.out.println(
                "   ↓"
        );

        System.out.println(
                "Groq LLM"
        );

        System.out.println(
                "   ↓"
        );

        System.out.println(
                "Hypothetical Document"
        );

        System.out.println(
                "   ↓"
        );

        System.out.println(
                "BGE-M3"
        );

        System.out.println(
                "   ↓"
        );

        System.out.println(
                "1024D Embedding"
        );

        System.out.println(
                "   ↓"
        );

        System.out.println(
                "Vector Database Retrieval"
        );
    }
}