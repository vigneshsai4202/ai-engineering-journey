package com.vignesh.ai.day53.agent;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class TechnicalSupportAgent {

    private final ChatClient chatClient;

    public TechnicalSupportAgent(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public String respond(String question) {

        return chatClient.prompt()
                .system("""
                        You are a Technical Support Agent.

                        Help users with technical problems such as:
                        - API errors
                        - Authentication errors
                        - Software bugs
                        - Application issues

                        Give a clear and practical solution.
                        If you do not have enough information, ask for
                        the missing details.
                        """)
                .user(question)
                .call()
                .content();
    }
}