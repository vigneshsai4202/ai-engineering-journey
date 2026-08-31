package com.vignesh.ai.day35_spring_ai_rag.model;

import java.util.List;

public record RagResponse(
        String answer,
        List<String> sources
) {
}