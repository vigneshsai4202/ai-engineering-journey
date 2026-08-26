package com.vignesh.ai.day33_spring_ai_structed_output.model;

import java.util.List;

public record TopicResponse(
        String topic,
        String difficulty,
        String summary,
        List<String> keyPoints
) {
}