package com.vignesh.ai.day51.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
public class EmployeeTool {

    @Tool(description = "Gets employee information using employee ID")
    public String getEmployee(String employeeId) {

        if ("EMP101".equalsIgnoreCase(employeeId)) {
            return "Employee ID: EMP101, Name: Vignesh, Role: Java Developer, Department: Engineering, Annual Salary: 600000 INR";
        }

        if ("EMP102".equalsIgnoreCase(employeeId)) {
            return "Employee ID: EMP102, Name: Rahul, Role: Software Engineer, Department: Development, Annual Salary: 700000 INR";
        }

        return "Employee not found";
    }
}