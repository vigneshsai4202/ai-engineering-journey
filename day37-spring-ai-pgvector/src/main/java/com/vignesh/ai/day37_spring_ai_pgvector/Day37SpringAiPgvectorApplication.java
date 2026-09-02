package com.vignesh.ai.day37_spring_ai_pgvector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
    exclude = {
        org.springframework.ai.model.ollama.autoconfigure.OllamaChatAutoConfiguration.class
    }
)
public class Day37SpringAiPgvectorApplication {

    public static void main(String[] args) {
        SpringApplication.run(Day37SpringAiPgvectorApplication.class, args);
    }
}