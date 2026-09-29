package com.vignesh.ai.day49.service;

import com.vignesh.ai.day49.model.KnowledgeSource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KnowledgeSourceRegistry {

    private final List<KnowledgeSource> sources = List.of(

            new KnowledgeSource(
                    "java",
                    "Java",
                    "Java programming language, JVM, OOP, collections and core Java concepts"
            ),

            new KnowledgeSource(
                    "spring",
                    "Spring",
                    "Spring Framework, Spring Boot, dependency injection, REST APIs and Spring concepts"
            ),

            new KnowledgeSource(
                    "rag",
                    "RAG",
                    "Retrieval Augmented Generation, embeddings, vector databases and retrieval systems"
            )
    );

    public List<KnowledgeSource> getSources() {
        return sources;
    }
}