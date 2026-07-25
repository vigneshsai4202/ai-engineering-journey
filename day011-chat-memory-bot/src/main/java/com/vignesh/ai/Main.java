package com.vignesh.ai;

import java.util.Scanner;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import io.github.cdimascio.dotenv.Dotenv;

public class Main {

    public static void main(String[] args) {

        // Load API key from .env
        Dotenv dotenv = Dotenv.load();
        String apiKey = dotenv.get("GROQ_API_KEY");

        // Create Groq Chat Model
        OpenAiChatModel model = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .baseUrl("https://api.groq.com/openai/v1")
                .modelName("llama-3.3-70b-versatile")
                .build();

        // Create Chat Memory
        MessageWindowChatMemory chatMemory =
                MessageWindowChatMemory.withMaxMessages(10);

        // Create AI Assistant
        Assistant assistant = AiServices.builder(Assistant.class)
                .chatModel(model)
                .chatMemory(chatMemory)
                .build();

        Scanner scanner = new Scanner(System.in);

        System.out.println("====================================");
        System.out.println(" Java AI Assistant");
        System.out.println(" Type 'exit' to quit.");
        System.out.println("====================================");

        while (true) {

            System.out.print("\nYou: ");
            String userMessage = scanner.nextLine();

            if (userMessage.equalsIgnoreCase("exit")) {
                System.out.println("Goodbye!");
                break;
            }

            String response = assistant.chat(userMessage);

            System.out.println("\nAI: " + response);
        }

        scanner.close();
    }
}