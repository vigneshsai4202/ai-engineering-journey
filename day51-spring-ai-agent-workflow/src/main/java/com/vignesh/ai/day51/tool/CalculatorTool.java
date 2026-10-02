package com.vignesh.ai.day51.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
public class CalculatorTool {

    @Tool(description = "Calculates a percentage of a number")
    public double calculatePercentage(double amount, double percentage) {
        return amount * percentage / 100;
    }
}