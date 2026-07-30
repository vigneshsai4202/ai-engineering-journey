package com.vignesh.ai;

import java.util.Scanner;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("Enter student information:");
        String input = sc.nextLine();

        ChatModel model = OpenAiChatModel.builder()
                .baseUrl("https://api.groq.com/openai/v1")
                .apiKey("YOUR_GROQ_API")
                .modelName("llama-3.3-70b-versatile")
                .build();

        StudentExtractor extractor =
                AiServices.create(StudentExtractor.class, model);

        Student student = extractor.extract(input);

        System.out.println("\n===== Structured Output =====");
        System.out.println(student);

        sc.close();
    }
}