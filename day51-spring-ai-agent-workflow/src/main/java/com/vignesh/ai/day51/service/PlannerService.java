package com.vignesh.ai.day51.service;

import com.vignesh.ai.day51.model.TaskPlan;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class PlannerService {

    private final ChatClient chatClient;

    public PlannerService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public TaskPlan createPlan(String request) {

        String response = chatClient.prompt()
                .system("""
                        You are a task planner.

                        Break the user's request into clear,
                        sequential steps.

                        Rules:
                        - Return one step per line.
                        - Number each step.
                        - Keep the steps simple.
                        - Do not execute the task.
                        - Only create the plan.
                        """)
                .user(request)
                .call()
                .content();

        System.out.println("Generated Plan:");
        System.out.println(response);

        var steps = Arrays.stream(response.split("\\n"))
                .map(String::trim)
                .filter(step -> !step.isEmpty())
                .toList();

        return new TaskPlan(steps);
    }
}