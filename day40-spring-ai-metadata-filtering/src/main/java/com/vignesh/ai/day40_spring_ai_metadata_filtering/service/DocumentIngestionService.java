package com.vignesh.ai.day40_spring_ai_metadata_filtering.service;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

@Service
public class DocumentIngestionService {

    private final VectorStore vectorStore;

    public DocumentIngestionService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public String loadDocuments() {

        Document javaDoc = new Document("""
                Java is a general-purpose, object-oriented programming language.
                Java was created by James Gosling at Sun Microsystems and released in 1995.
                Java code runs on the JVM.
                """);

        javaDoc.getMetadata().put("category", "java");
        javaDoc.getMetadata().put("level", "beginner");

        Document springDoc = new Document("""
                Spring Boot is a Java framework used to build production-ready
                applications quickly. It provides auto-configuration,
                dependency injection, and embedded servers.
                """);

        springDoc.getMetadata().put("category", "spring");
        springDoc.getMetadata().put("level", "advanced");

        Document aiDoc = new Document("""
                Retrieval Augmented Generation combines information retrieval
                with large language models. Relevant documents are retrieved
                before the language model generates an answer.
                """);

        aiDoc.getMetadata().put("category", "ai");
        aiDoc.getMetadata().put("level", "advanced");

        vectorStore.add(List.of(javaDoc, springDoc, aiDoc));

        return "Documents stored successfully";
    }
}