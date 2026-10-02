package com.vignesh.ai.day51.service;

import com.vignesh.ai.day51.model.WorkflowState;
import com.vignesh.ai.day51.tool.CalculatorTool;
import com.vignesh.ai.day51.tool.EmployeeTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ExecutorService {

    private final ChatClient chatClient;
    private final EmployeeTool employeeTool;
    private final CalculatorTool calculatorTool;

    public ExecutorService(
            ChatClient.Builder chatClientBuilder,
            EmployeeTool employeeTool,
            CalculatorTool calculatorTool) {

        this.chatClient = chatClientBuilder.build();
        this.employeeTool = employeeTool;
        this.calculatorTool = calculatorTool;
    }

    public String execute(
            java.util.List<String> steps,
            String request) {

        WorkflowState state = new WorkflowState();

        try {

            // STEP 1 — Get employee information
            String employeeId = extractEmployeeId(request);

            String employeeResult =
                    employeeTool.getEmployee(employeeId);

            state.completeStep(
                    steps.get(0),
                    employeeResult
            );

            System.out.println("STEP 1 RESULT:");
            System.out.println(employeeResult);


            // STEP 2 — Calculate bonus
            double salary = extractSalary(employeeResult);

            double bonus =
                    calculatorTool.calculatePercentage(
                            salary,
                            10
                    );

            String bonusResult =
                    "10% bonus = " + bonus + " INR";

            state.completeStep(
                    steps.size() > 1
                            ? steps.get(1)
                            : "Calculate bonus",
                    bonusResult
            );

            System.out.println("STEP 2 RESULT:");
            System.out.println(bonusResult);


            // STEP 3 — Generate final summary
            String context = """
                    Employee Information:
                    %s

                    Calculation:
                    %s
                    """.formatted(
                    employeeResult,
                    bonusResult
            );

            String finalAnswer = chatClient.prompt()
                    .system("""
                            Create a concise employee bonus summary.

                            Use ONLY the provided workflow results.
                            Do not invent any information.
                            """)
                    .user(context)
                    .call()
                    .content();

            state.completeStep(
                    steps.size() > 2
                            ? steps.get(2)
                            : "Create summary",
                    finalAnswer
            );

            state.setStatus("COMPLETED");

            printState(state);

            return finalAnswer;

        } catch (Exception e) {

            state.setStatus("FAILED");

            System.out.println("Workflow failed: "
                    + e.getMessage());

            return "Workflow failed: " + e.getMessage();
        }
    }

    private String extractEmployeeId(String request) {

        Pattern pattern =
                Pattern.compile("EMP\\d+",
                        Pattern.CASE_INSENSITIVE);

        Matcher matcher = pattern.matcher(request);

        if (matcher.find()) {
            return matcher.group().toUpperCase();
        }

        throw new IllegalArgumentException(
                "Employee ID not found"
        );
    }

    private double extractSalary(String employeeResult) {

        Pattern pattern =
                Pattern.compile(
                        "Annual Salary:\\s*(\\d+(?:\\.\\d+)?)",
                        Pattern.CASE_INSENSITIVE
                );

        Matcher matcher = pattern.matcher(employeeResult);

        if (matcher.find()) {
            return Double.parseDouble(matcher.group(1));
        }

        throw new IllegalArgumentException(
                "Salary not found in employee result"
        );
    }

    private void printState(WorkflowState state) {

        System.out.println();
        System.out.println("===== WORKFLOW STATE =====");

        System.out.println(
                "Current Step: "
                        + state.getCurrentStep()
        );

        System.out.println(
                "Completed Steps: "
                        + state.getCompletedSteps()
        );

        System.out.println(
                "Tool Results: "
                        + state.getToolResults()
        );

        System.out.println(
                "Status: "
                        + state.getStatus()
        );

        System.out.println(
                "=========================="
        );
    }
}