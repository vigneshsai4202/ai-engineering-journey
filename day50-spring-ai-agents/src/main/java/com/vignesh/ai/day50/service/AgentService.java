package com.vignesh.ai.day50.service;

import com.vignesh.ai.day50.tool.CalculatorTool;
import com.vignesh.ai.day50.tool.DateTimeTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import com.vignesh.ai.day50.tool.EmployeeTool;


@Service
public class AgentService {

    private final ChatClient chatClient;
    private final CalculatorTool calculatorTool;
    private final DateTimeTool dateTimeTool;
    private final EmployeeTool employeeTool;

    public AgentService(
            ChatClient.Builder chatClientBuilder,
            CalculatorTool calculatorTool,
            DateTimeTool dateTimeTool,
            EmployeeTool employeeTool) {

        this.chatClient = chatClientBuilder.build();
        this.calculatorTool = calculatorTool;
        this.dateTimeTool = dateTimeTool;
        this.employeeTool = employeeTool;
    }
    public String ask(String question) {

        return chatClient.prompt()
                .system("""
                        You are an AI assistant.

                        When a calculation is required,
                        use the available calculator tools.

                        When the user asks for the current date or time,
                        use the date/time tool.

                        Do not calculate manually when a tool
                        can perform the calculation.
                        """)
                .user(question)
                .tools(calculatorTool, dateTimeTool, employeeTool)
                .call()
                .content();
    }
}