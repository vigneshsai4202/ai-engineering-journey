package com.vignesh.ai;
import java.util.*;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner sc=new Scanner(System.in);
		
		ChatModel model=OpenAiChatModel.builder()
				.baseUrl("https://api.groq.com/openai/v1")
				.apiKey("YOUR_GROQ_API_KEY")
				.modelName("llama-3.3-70b-versatile")
				.build();
		
		Assistant assistant=AiServices.builder(Assistant.class)
				.chatModel(model)
				.tools(new UtliltyTools())
				.build();
		
		System.out.println("======AI Utility Assistant=====");
		
		
		while(true) {
			System.out.println("\nYou");
			
			String input=sc.nextLine();
			
			if(input.equalsIgnoreCase("exit")) {
				break;
			}
			String response=assistant.chat(input);
			
			System.out.println("\nAI: "+response);
			
		}
		sc.close();
		
		

	}

}
