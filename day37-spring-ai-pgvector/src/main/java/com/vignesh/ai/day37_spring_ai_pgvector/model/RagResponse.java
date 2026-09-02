package com.vignesh.ai.day37_spring_ai_pgvector.model;

import java.util.List;

public record RagResponse(
        String answer,
        List<String> sources
) {
}