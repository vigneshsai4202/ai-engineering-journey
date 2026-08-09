package com.vignesh.ai;

import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.huggingface.HuggingFaceEmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;

import io.github.cdimascio.dotenv.Dotenv;

public class RagAssistant {

    public static void main(String[] args) {

        // Loadig environment variables
        Dotenv dotenv = Dotenv.load();

        String hfApiKey = dotenv.get("HF_API_KEY");
        String groqApiKey = dotenv.get("GROQ_API_KEY");

        // Creating BGE-M3 embedding model
        EmbeddingModel embeddingModel =
                HuggingFaceEmbeddingModel.builder()
                        .accessToken(hfApiKey)
                        .modelId("BAAI/bge-m3")
                        .build();

        // Loading PDF
        Document document =
                FileSystemDocumentLoader.loadDocument(
                        Path.of("documents/sample.pdf"),
                        new ApachePdfBoxDocumentParser()
                );

        // Spliting document into chunks
        List<TextSegment> segments =
                DocumentSplitters.recursive(300, 50)
                        .split(document);

        System.out.println("Total chunks: " + segments.size());

        // Creating embeddings for all chunks
        List<Embedding> embeddings =
                embeddingModel.embedAll(segments).content();

        // Creating vector store
        EmbeddingStore<TextSegment> embeddingStore =
                new InMemoryEmbeddingStore<>();

        // Store embeddings together with original chunks
        embeddingStore.addAll(embeddings, segments);

        System.out.println("Embeddings stored successfully.");

        // Creating Groq Chat Model
        ChatModel chatModel =
                OpenAiChatModel.builder()
                        .apiKey(groqApiKey)
                        .baseUrl("https://api.groq.com/openai/v1")
                        .modelName("llama-3.3-70b-versatile")
                        .build();

        // Interactive question loop
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n========================================");
        System.out.println("          PDF RAG ASSISTANT");
        System.out.println("========================================");
        System.out.println("Document loaded successfully.");
        System.out.println("Ask questions about the PDF.");
        System.out.println("Type 'exit' to quit.");

        while (true) {

            System.out.print("\n> ");
            String question = scanner.nextLine();

            // Exit condition
            if (question.equalsIgnoreCase("exit")) {
                System.out.println("\nThank you for using PDF RAG Assistant!");
                break;
            }

            // Convert user question into an embedding
            Embedding queryEmbedding =
                    embeddingModel.embed(question).content();

            // Searching for the most relevant chunks
            EmbeddingSearchRequest searchRequest =
                    EmbeddingSearchRequest.builder()
                            .queryEmbedding(queryEmbedding)
                            .maxResults(5)
                            .minScore(0.0)
                            .build();

            EmbeddingSearchResult<TextSegment> searchResult =
                    embeddingStore.search(searchRequest);

            List<EmbeddingMatch<TextSegment>> matches =
                    searchResult.matches();

            // Building context from retrieved chunks
            StringBuilder context = new StringBuilder();

            for (EmbeddingMatch<TextSegment> match : matches) {
                context.append(match.embedded().text());
                context.append("\n\n");
            }

            // Building RAG prompt
            String prompt = """
                    Answer the question using only the context provided below.

                    If the answer is not available in the context,
                    say that the information is not available in the document.

                    Context:
                    %s

                    Question:
                    %s
                    """.formatted(context, question);

            // Sending context + question to the LLM
            String answer = chatModel.chat(prompt);

            // Displaying answer
            System.out.println("\n===== RAG ANSWER =====");
            System.out.println(answer);
        }

        scanner.close();
    }
}