package com.vignesh.ai;


import java.util.Scanner;

import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import io.github.cdimascio.dotenv.Dotenv;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Dotenv dotenv = Dotenv.load();

		OpenAiChatModel model = OpenAiChatModel.builder()
		        .apiKey(dotenv.get("GROQ_API_KEY"))
		        .baseUrl("https://api.groq.com/openai/v1")
		        .modelName("llama-3.3-70b-versatile")
		        .build();
		
		ChatMemoryProvider memoryProvider = memoryId ->
        MessageWindowChatMemory.withMaxMessages(10);
        
        
        Assistant assistant = AiServices.builder(Assistant.class)
                .chatModel(model)
                .chatMemoryProvider(memoryProvider)
                .build();
        Scanner scanner = new Scanner(System.in);

        System.out.println("===== Multi User AI Chat Bot =====");

        while (true) {

            System.out.print("\nEnter Memory ID: ");
            int memoryId = Integer.parseInt(scanner.nextLine());

            System.out.print("You: ");
            String userMessage = scanner.nextLine();

            if (userMessage.equalsIgnoreCase("exit")) {
                break;
            }

            String response = assistant.chat(memoryId, userMessage);

            System.out.println("AI: " + response);
        }

        scanner.close();

	}

}
