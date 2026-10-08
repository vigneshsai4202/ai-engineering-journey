package com.vignesh.ai.day53.agent;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class GeneralSupportAgent {

    private final ChatClient chatClient;

    public GeneralSupportAgent(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public String respond(String question) {

        return chatClient.prompt()
                .system("""
                        You are a General Customer Support Agent.

                        Help users with:
                        - Password resets
                        - Account questions
                        - How-to questions
                        - General support requests

                        Give simple, clear and useful instructions.
                        """)
                .user(question)
                .call()
                .content();
    }
}