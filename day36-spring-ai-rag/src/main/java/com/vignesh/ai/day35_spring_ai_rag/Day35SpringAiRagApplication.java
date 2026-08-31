package com.vignesh.ai.day35_spring_ai_rag;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
    exclude = {
        org.springframework.ai.model.ollama.autoconfigure.OllamaChatAutoConfiguration.class
    }
)
public class Day35SpringAiRagApplication {

    public static void main(String[] args) {
        SpringApplication.run(Day35SpringAiRagApplication.class, args);
    }
}