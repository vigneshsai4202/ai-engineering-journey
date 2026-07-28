package com.vignesh.ai;



import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;

import java.util.Map;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter your name: ");
        String name = sc.nextLine();

        System.out.print("Enter your role: ");
        String role = sc.nextLine();

        System.out.print("Enter topic: ");
        String topic = sc.nextLine();

        ChatLanguageModel model = GroqChatModel.builder()
                .apiKey("YOUR_GROQ_API_KEY")
                .modelName("llama-3.3-70b-versatile")
                .build();

        PromptTemplate template = PromptTemplate.from("""
                You are an expert {{role}}.

                Hello {{name}}.

                Explain {{topic}} in simple language with one example.
                """);

        Prompt prompt = template.apply(Map.of(
                "name", name,
                "role", role,
                "topic", topic
        ));

        String response = model.chat(UserMessage.from(prompt.text()));

        System.out.println("\n========== AI Response ==========\n");
        System.out.println(response);

        sc.close();
    }
}
