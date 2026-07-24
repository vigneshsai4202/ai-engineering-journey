package com.vignesh.ai;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import io.github.cdimascio.dotenv.Dotenv;


public class Main {

    public static void main(String[] args) {

        // Load your API key from .env
        Dotenv dotenv = Dotenv.load();
        String apiKey = dotenv.get("GROQ_API_KEY");

        // Creating the  Groq Chat Model with support of openaichatmodel
        ChatModel model = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .baseUrl("https://api.groq.com/openai/v1")
                .modelName("llama-3.3-70b-versatile")
                .build();

        // first prompt using the low level api 
       // String response = model.chat("Hello! Introduce yourself in one sentence.");
 
        
        
        //Using the AI services
        Assistant assistant = AiServices.create(Assistant.class, model);

        String response = assistant.chat("Explain Java");
        System.out.println(response);
    }
}