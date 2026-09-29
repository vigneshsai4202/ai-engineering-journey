package com.vignesh.ai.day49.model;

import java.util.List;

public record QueryPlan(
        List<String> sourceIds
) {
}