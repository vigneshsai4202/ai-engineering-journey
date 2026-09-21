package com.vignesh.ai.day44_spring_ai_rag_observability;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        exclude = {
                org.springframework.ai.model.ollama.autoconfigure.OllamaChatAutoConfiguration.class
        }
)
public class Day44SpringAiRagObservabilityApplication {

    public static void main(String[] args) {
        SpringApplication.run(
                Day44SpringAiRagObservabilityApplication.class,
                args
        );
    }
}