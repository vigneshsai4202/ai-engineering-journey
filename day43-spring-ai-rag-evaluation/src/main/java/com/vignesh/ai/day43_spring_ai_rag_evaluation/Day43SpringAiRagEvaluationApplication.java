package com.vignesh.ai.day43_spring_ai_rag_evaluation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        exclude = {
                org.springframework.ai.model.ollama.autoconfigure.OllamaChatAutoConfiguration.class
        }
)
public class Day43SpringAiRagEvaluationApplication {

    public static void main(String[] args) {
        SpringApplication.run(
                Day43SpringAiRagEvaluationApplication.class,
                args
        );
    }
}