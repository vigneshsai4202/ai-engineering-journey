package com.vignesh.ai.day37_spring_ai_pgvector.service;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.ParagraphPdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.core.io.ClassPathResource;
import com.vignesh.ai.day37_spring_ai_pgvector.model.RagResponse;
@Service
public class RagService {

    private final VectorStore vectorStore;
    private final ChatClient chatClient;

    public RagService(VectorStore vectorStore, ChatClient.Builder chatClientBuilder) {
        this.vectorStore = vectorStore;
        this.chatClient = chatClientBuilder.build();
    }

    public String loadPdf() {

        ParagraphPdfDocumentReader reader =
                new ParagraphPdfDocumentReader(
                        new ClassPathResource("documents/java-notes.pdf"));

        List<Document> documents = reader.get();

        TokenTextSplitter splitter = new TokenTextSplitter();

        List<Document> chunks = splitter.apply(documents);

        // Add source metadata to every chunk
        chunks.forEach(document ->
                document.getMetadata().put("source", "documents/sample.pdf")
        );

        vectorStore.add(chunks);

        return "PDF successfully stored in PGVector";
    }
    public RagResponse ask(String question) {

        SearchRequest searchRequest = SearchRequest.builder()
                .query(question)
                .topK(3)
                .similarityThreshold(0.5)
                .build();

        List<Document> documents =
                vectorStore.similaritySearch(searchRequest);

        if (documents.isEmpty()) {
            return new RagResponse(
                    "I don't know. The information is not available in the provided document.",
                    List.of()
            );
        }

        String context = documents.stream()
                .map(Document::getText)
                .reduce("", (a, b) -> a + "\n\n" + b);

        String prompt = """
                Answer the question using ONLY the context below.

                If the answer is not present in the context, say:
                "I don't know."

                Context:
                %s

                Question:
                %s
                """.formatted(context, question);

        String answer = chatClient.prompt()
                .user(prompt)
                .call()
                .content();

        // Remove Qwen thinking output
        answer = answer
                .replaceAll("(?s)<think>.*?</think>", "")
                .trim();

        // Extract source information
        List<String> sources = documents.stream()
                .map(document -> document.getMetadata()
                        .getOrDefault("source", "Unknown source")
                        .toString())
                .distinct()
                .toList();

        return new RagResponse(answer, sources);
    }
    
}