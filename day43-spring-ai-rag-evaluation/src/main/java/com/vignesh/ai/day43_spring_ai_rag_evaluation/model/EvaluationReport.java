package com.vignesh.ai.day43_spring_ai_rag_evaluation.model;

import java.util.List;

public record EvaluationReport(
        List<EvaluationResult> results,
        double averagePrecision,
        double averageRecall,
        double averageReciprocalRank
) {
}