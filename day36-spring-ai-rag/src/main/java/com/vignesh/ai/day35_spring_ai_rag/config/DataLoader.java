package com.vignesh.ai.day35_spring_ai_rag.config;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;


public class DataLoader {

    @Bean
    CommandLineRunner loadData(SimpleVectorStore vectorStore) {
        return args -> {

            List<Document> documents = List.of(
                new Document("""
                    Vignesh is a Computer Science and Artificial Intelligence
                    student who is learning Java, Spring Boot, Spring AI,
                    Generative AI and backend development.
                    """),

                new Document("""
                    Spring AI is a framework that makes it easier to build
                    AI-powered applications using the Spring ecosystem.
                    """),

                new Document("""
                    RAG stands for Retrieval-Augmented Generation.
                    It allows an AI application to retrieve relevant information
                    from external data and provide that information to an LLM
                    as context before generating an answer.
                    """)
            );

            vectorStore.add(documents);

            System.out.println("Documents added to vector store!");
        };
    }
}