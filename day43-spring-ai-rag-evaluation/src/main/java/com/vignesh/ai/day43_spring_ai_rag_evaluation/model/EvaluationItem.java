package com.vignesh.ai.day43_spring_ai_rag_evaluation.model;

public record EvaluationItem(
        String question,
        String expectedAnswer,
        String expectedSource
) {
}