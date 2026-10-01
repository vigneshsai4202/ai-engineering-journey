package com.vignesh.ai.day50.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DateTimeTool {

    @Tool(description = "Returns the current date and time")
    public String getCurrentDateTime() {
        return LocalDateTime.now().toString();
    }
}