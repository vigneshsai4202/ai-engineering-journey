package com.vignesh.ai.day40_spring_ai_metadata_filtering;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
    exclude = {
        org.springframework.ai.model.ollama.autoconfigure.OllamaChatAutoConfiguration.class
    }
)
public class Day40SpringAiMetadataFilteringApplication {

    public static void main(String[] args) {
        SpringApplication.run(Day40SpringAiMetadataFilteringApplication.class, args);
    }
}