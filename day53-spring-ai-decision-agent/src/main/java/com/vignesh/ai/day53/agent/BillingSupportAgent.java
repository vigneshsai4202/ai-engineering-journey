package com.vignesh.ai.day53.agent;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class BillingSupportAgent {

    private final ChatClient chatClient;

    public BillingSupportAgent(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public String respond(String question) {

        return chatClient.prompt()
                .system("""
                        You are a Billing Support Agent.

                        Help users with:
                        - Payments
                        - Duplicate charges
                        - Refunds
                        - Invoices
                        - Subscriptions
                        - Pricing questions

                        Give a clear and practical response.
                        Never invent transaction details.
                        """)
                .user(question)
                .call()
                .content();
    }
}