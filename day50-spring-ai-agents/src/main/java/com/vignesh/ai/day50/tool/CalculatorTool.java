package com.vignesh.ai.day50.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
public class CalculatorTool {

    @Tool(description = "Adds two numbers together")
    public double add(double a, double b) {
        return a + b;
    }

    @Tool(description = "Multiplies two numbers")
    public double multiply(double a, double b) {
        return a * b;
    }
}