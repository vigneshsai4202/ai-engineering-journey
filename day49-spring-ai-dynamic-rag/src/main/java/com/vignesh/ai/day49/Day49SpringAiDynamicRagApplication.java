package com.vignesh.ai.day49;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        exclude = {
                org.springframework.ai.model.ollama.autoconfigure.OllamaChatAutoConfiguration.class
        }
)
public class Day49SpringAiDynamicRagApplication {

    public static void main(String[] args) {

        SpringApplication.run(
                Day49SpringAiDynamicRagApplication.class,
                args
        );
    }
}