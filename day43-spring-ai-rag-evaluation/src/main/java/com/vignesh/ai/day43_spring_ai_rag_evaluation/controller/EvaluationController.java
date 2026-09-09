package com.vignesh.ai.day43_spring_ai_rag_evaluation.controller;

import com.vignesh.ai.day43_spring_ai_rag_evaluation.model.EvaluationResult;
import com.vignesh.ai.day43_spring_ai_rag_evaluation.model.EvaluationReport;
import com.vignesh.ai.day43_spring_ai_rag_evaluation.service.EvaluationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/evaluation")
public class EvaluationController {

    private final EvaluationService evaluationService;

    public EvaluationController(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    @GetMapping
    public EvaluationResult evaluate(
            @RequestParam String question,
            @RequestParam String expectedSource,
            @RequestParam String expectedAnswer) {

        return evaluationService.evaluate(
                question,
                expectedSource,
                expectedAnswer
        );
    }
    @GetMapping("/run")
    public EvaluationReport runEvaluation() {
        return evaluationService.evaluateDataset();
    }
    	
}