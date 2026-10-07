package com.vignesh.ai.day52.agent;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class JobAnalystAgent {

    private final ChatClient chatClient;

    public JobAnalystAgent(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public String analyze(String jobDescription) {

        return chatClient.prompt()
                .system("""
                        You are a Job Analyst Agent.

                        Analyze the given job description.

                        Identify:
                        1. Required technical skills
                        2. Required frameworks and technologies
                        3. Experience requirements
                        4. Important responsibilities

                        Keep the response structured and concise.
                        Do not invent requirements that are not present.
                        """)
                .user(jobDescription)
                .call()
                .content();
    }
}