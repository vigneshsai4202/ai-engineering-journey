package com.vignesh.ai.day43_spring_ai_rag_evaluation.model;

public record EvaluationResult(
        String question,
        int retrievedDocuments,
        int relevantDocuments,
        double precision,
        double recall,
        double reciprocalRank,
        String answer
) {
}