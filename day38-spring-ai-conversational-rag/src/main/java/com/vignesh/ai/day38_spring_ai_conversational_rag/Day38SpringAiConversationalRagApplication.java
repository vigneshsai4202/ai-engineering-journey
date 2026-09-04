package com.vignesh.ai.day38_spring_ai_conversational_rag;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
    exclude = {
        org.springframework.ai.model.ollama.autoconfigure.OllamaChatAutoConfiguration.class
    }
)
public class Day38SpringAiConversationalRagApplication {

    public static void main(String[] args) {
        SpringApplication.run(
                Day38SpringAiConversationalRagApplication.class,
                args
        );
    }
}