package com.vignesh.ai.day52.agent;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class CareerAdvisorAgent {

    private final ChatClient chatClient;

    public CareerAdvisorAgent(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public String createPlan(String skillGapReport) {

        return chatClient.prompt()
                .system("""
                        You are a Career Advisor Agent.

                        Based only on the provided skill gap report,
                        create a practical learning plan.

                        Include:
                        1. Skills to learn
                        2. Recommended learning order
                        3. Practical projects to build
                        4. A short action plan

                        Do not invent requirements or skills.
                        Keep the plan realistic and concise.
                        """)
                .user("""
                        SKILL GAP REPORT:

                        %s
                        """.formatted(skillGapReport))
                .call()
                .content();
    }
}