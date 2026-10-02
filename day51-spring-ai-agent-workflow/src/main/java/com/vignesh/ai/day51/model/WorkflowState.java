package com.vignesh.ai.day51.model;

import java.util.ArrayList;
import java.util.List;

public class WorkflowState {

    private int currentStep;
    private final List<String> completedSteps = new ArrayList<>();
    private final List<String> toolResults = new ArrayList<>();
    private String status;

    public WorkflowState() {
        this.status = "STARTED";
    }

    public void completeStep(String step, String result) {
        completedSteps.add(step);
        toolResults.add(result);
        currentStep++;
    }

    public int getCurrentStep() {
        return currentStep;
    }

    public List<String> getCompletedSteps() {
        return completedSteps;
    }

    public List<String> getToolResults() {
        return toolResults;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}