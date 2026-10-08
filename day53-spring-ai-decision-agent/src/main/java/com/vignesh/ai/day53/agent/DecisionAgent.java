package com.vignesh.ai.day53.agent;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class DecisionAgent {

    private final ChatClient chatClient;

    public DecisionAgent(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public String decide(String question) {

        return chatClient.prompt()
                .system("""
                        You are a customer support decision agent.

                        Classify the user's query into exactly ONE category.

                        Available categories:
                        TECHNICAL
                        BILLING
                        GENERAL

                        Rules:
                        - TECHNICAL: APIs, errors, bugs, login failures,
                          software problems, technical issues.
                        - BILLING: payments, charges, refunds, invoices,
                          subscriptions, pricing.
                        - GENERAL: password resets, account information,
                          how-to questions, and other general requests.

                        Return ONLY the category name.
                        Do not add explanations.
                        """)
                .user(question)
                .call()
                .content()
                .trim()
                .toUpperCase();
    }
}