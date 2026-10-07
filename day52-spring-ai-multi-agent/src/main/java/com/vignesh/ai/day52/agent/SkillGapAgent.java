package com.vignesh.ai.day52.agent;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class SkillGapAgent {

    private final ChatClient chatClient;

    public SkillGapAgent(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public String analyze(String jobAnalysis, String candidateSkills) {

        return chatClient.prompt()
                .system("""
                        You are a Skill Gap Analysis Agent.

                        Compare the job requirements with the candidate's skills.

                        Identify:
                        1. Skills the candidate already has
                        2. Missing technical skills
                        3. Missing frameworks or technologies
                        4. Priority areas to learn

                        Only use information provided in the input.
                        Do not invent candidate skills or job requirements.

                        Keep the response structured and concise.
                        """)
                .user("""
                        JOB ANALYSIS:
                        %s

                        CANDIDATE SKILLS:
                        %s
                        """.formatted(jobAnalysis, candidateSkills))
                .call()
                .content();
    }
}