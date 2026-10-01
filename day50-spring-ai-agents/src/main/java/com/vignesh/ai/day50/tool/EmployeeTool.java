package com.vignesh.ai.day50.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
public class EmployeeTool {

    @Tool(description = "Gets employee information using the employee ID")
    public String getEmployee(String employeeId) {

        if ("EMP101".equalsIgnoreCase(employeeId)) {
            return "Employee: Vignesh, Role: Java Developer, Department: Engineering";
        }

        if ("EMP102".equalsIgnoreCase(employeeId)) {
            return "Employee: Rahul, Role: Software Engineer, Department: Development";
        }

        return "Employee not found";
    }
}