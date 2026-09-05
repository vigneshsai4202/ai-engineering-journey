package com.vignesh.ai.day39_spring_ai_query_rewriting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
    exclude = {
        org.springframework.ai.model.ollama.autoconfigure.OllamaChatAutoConfiguration.class
    }
)
public class Day39SpringAiQueryRewritingApplication {

    public static void main(String[] args) {
        SpringApplication.run(Day39SpringAiQueryRewritingApplication.class, args);
    }
}  