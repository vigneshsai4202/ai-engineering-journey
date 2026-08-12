package com.vignesh.ai;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.jina.JinaScoringModel;
import dev.langchain4j.model.scoring.ScoringModel;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.List;

public class JinaRerankerTest {

    public static void main(String[] args) {

        Dotenv dotenv = Dotenv.load();

        // -----------------------------------------
        // 1. Create Jina Scoring Model
        // -----------------------------------------

        ScoringModel scoringModel =
                JinaScoringModel.builder()
                        .apiKey(dotenv.get("JINA_API_KEY"))
                        .modelName(
                                "jina-reranker-v2-base-multilingual"
                        )
                        .build();

        System.out.println(
                "✅ Jina reranker ready!"
        );

        // -----------------------------------------
        // 2. User question
        // -----------------------------------------

        String question =
                "What is Spring Boot used for?";

        // -----------------------------------------
        // 3. Candidate chunks
        // -----------------------------------------

        List<TextSegment> candidates = List.of(

                TextSegment.from(
                        "Spring Boot is a Java framework used to build production-ready applications."
                ),

                TextSegment.from(
                        "Spring Boot provides features such as dependency injection and auto-configuration."
                ),

                TextSegment.from(
                        "PostgreSQL is a relational database commonly used in backend applications."
                ),

                TextSegment.from(
                        "Docker packages applications and their dependencies into containers."
                ),

                TextSegment.from(
                        "Java is a strongly typed, object-oriented programming language."
                )
        );

        // -----------------------------------------
        // 4. Rerank candidates
        // -----------------------------------------

        var response =
                scoringModel.scoreAll(
                        candidates,
                        question
                );

        List<Double> scores =
                response.content();

        // -----------------------------------------
        // 5. Display scores
        // -----------------------------------------

        System.out.println(
                "\n===== Jina Reranker Scores ====="
        );

        for (int i = 0; i < candidates.size(); i++) {

            System.out.println(
                    "\nCandidate: " + i
            );

            System.out.println(
                    "Reranker Score: "
                            + scores.get(i)
            );

            System.out.println(
                    "Content: "
                            + candidates.get(i).text()
            );

            System.out.println(
                    "-----------------------------"
            );
        }
    }
}