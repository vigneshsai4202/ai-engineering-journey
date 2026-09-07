package com.vignesh.ai.day41_spring_ai_hybrid_rag.service;

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
                Java applications run on the Java Virtual Machine (JVM).
                """);

        javaDoc.getMetadata().put("category", "java");
        javaDoc.getMetadata().put("level", "beginner");

        Document springDoc = new Document("""
                Spring Boot is a Java framework for building production-ready applications.
                It provides auto-configuration, dependency injection, embedded servers,
                and simplifies the development of Spring applications.
                """);

        springDoc.getMetadata().put("category", "spring");
        springDoc.getMetadata().put("level", "advanced");

        Document ragDoc = new Document("""
                Retrieval Augmented Generation (RAG) combines information retrieval
                with large language models. Relevant documents are retrieved first
                and then provided to the language model to generate a grounded answer.
                """);

        ragDoc.getMetadata().put("category", "ai");
        ragDoc.getMetadata().put("level", "advanced");

        Document microservicesDoc = new Document("""
                Microservices architecture structures an application as a collection
                of small, independently deployable services. Each service can own
                its business logic and communicate with other services through APIs.
                """);

        microservicesDoc.getMetadata().put("category", "architecture");
        microservicesDoc.getMetadata().put("level", "advanced");

        vectorStore.add(List.of(
                javaDoc,
                springDoc,
                ragDoc,
                microservicesDoc
        ));

        return "Documents stored successfully";
    }
}