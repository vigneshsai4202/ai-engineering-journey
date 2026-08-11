package com.vignesh.ai;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.huggingface.HuggingFaceEmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import io.github.cdimascio.dotenv.Dotenv;

public class PersistentPdfRag {

    public static void main(String[] args) {

        Dotenv dotenv = Dotenv.load();

        // -----------------------------------------
        // 1. BGE-M3 Embedding Model
        // -----------------------------------------

        EmbeddingModel embeddingModel =
                HuggingFaceEmbeddingModel.builder()
                        .accessToken(dotenv.get("HF_API_KEY"))
                        .modelId("BAAI/bge-m3")
                        .build();

        System.out.println(
                "✅ BGE-M3 ready!"
        );

        // -----------------------------------------
        // 2. PGVector
        // -----------------------------------------

        EmbeddingStore<TextSegment> embeddingStore =
                PgVectorEmbeddingStore.builder()
                        .host(dotenv.get("NEON_HOST"))
                        .port(
                                Integer.parseInt(
                                        dotenv.get("NEON_PORT")
                                )
                        )
                        .database(dotenv.get("NEON_DATABASE"))
                        .user(dotenv.get("NEON_USERNAME"))
                        .password(dotenv.get("NEON_PASSWORD"))
                        .table("day19_pdf_embeddings")
                        .dimension(1024)
                        .build();

        System.out.println(
                "✅ PGVector connected!"
        );

        // -----------------------------------------
        // 3. Groq Chat Model
        // -----------------------------------------

        ChatModel chatModel =
                OpenAiChatModel.builder()
                        .apiKey(dotenv.get("GROQ_API_KEY"))
                        .baseUrl("https://api.groq.com/openai/v1")
                        .modelName("llama-3.3-70b-versatile")
                        .build();

        System.out.println(
                "✅ Groq model ready!"
        );

        // -----------------------------------------
        // 4. User Question
        // -----------------------------------------

        String question =
                "What is Spring Boot used for?";

        // -----------------------------------------
        // 5. Embed the question
        // -----------------------------------------

        Embedding queryEmbedding =
                embeddingModel
                        .embed(question)
                        .content();

        // -----------------------------------------
        // 6. Search PGVector
        // -----------------------------------------

        EmbeddingSearchRequest searchRequest =
                EmbeddingSearchRequest.builder()
                        .queryEmbedding(queryEmbedding)
                        .maxResults(3)
                        .minScore(0.0)
                        .build();

        var searchResult =
                embeddingStore.search(searchRequest);

        // -----------------------------------------
        // 7. Build context
        // -----------------------------------------

        StringBuilder context =
                new StringBuilder();

        for (var match : searchResult.matches()) {

            TextSegment segment = match.embedded();

            if (segment != null) {

                System.out.println(
                        "Score: " + match.score()
                );

                System.out.println(
                        "Document: "
                                + segment.metadata()
                                        .getString("documentName")
                );

                System.out.println(
                        "Chunk ID: "
                                + segment.metadata()
                                        .getString("chunkId")
                );

                System.out.println(
                        "Content:\n"
                                + segment.text()
                );

                context.append(segment.text());
                context.append("\n\n");
            }
        }

        // -----------------------------------------
        // 8. Create RAG prompt
        // -----------------------------------------

        String prompt = """
                You are a helpful AI assistant.

                Answer the question using ONLY the provided context.

                If the answer cannot be found in the context,
                say that the information is not available
                in the provided document.

                Context:
                %s

                Question:
                %s
                """.formatted(
                        context,
                        question
                );

        // -----------------------------------------
        // 9. Send context + question to Groq
        // -----------------------------------------

        String answer =
                chatModel.chat(prompt);

        // -----------------------------------------
        // 10. Display answer
        // -----------------------------------------

        System.out.println(
                "\n===== RAG Answer ====="
        );

        System.out.println(answer);
    }
}