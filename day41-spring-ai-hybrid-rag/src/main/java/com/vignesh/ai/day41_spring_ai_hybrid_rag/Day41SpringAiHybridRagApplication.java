package com.vignesh.ai.day41_spring_ai_hybrid_rag;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        exclude = {
                org.springframework.ai.model.ollama.autoconfigure.OllamaChatAutoConfiguration.class
        }
)
public class Day41SpringAiHybridRagApplication {

    public static void main(String[] args) {
        SpringApplication.run(
                Day41SpringAiHybridRagApplication.class,
                args
        );
    }
}