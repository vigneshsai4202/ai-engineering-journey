package com.vignesh.ai;

import java.util.*;

public class HybridRagDemo {

    // Represents one retrieved document
    static class DocumentResult {

        String documentId;
        String content;
        int rank;

        DocumentResult(
                String documentId,
                String content,
                int rank) {

            this.documentId = documentId;
            this.content = content;
            this.rank = rank;
        }
    }

    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println("       DAY 24 - PRODUCTION RAG");
        System.out.println("======================================");

        String query =
                "What are the advantages of Spring Boot?";

        System.out.println("\n===== User Query =====");
        System.out.println(query);

        // =====================================================
        // 1. VECTOR SEARCH RESULTS
        // =====================================================

        List<DocumentResult> vectorResults =
                List.of(

                        new DocumentResult(
                                "DOC-A",
                                "Spring Boot provides auto-configuration " +
                                "and simplifies Java application development.",
                                1
                        ),

                        new DocumentResult(
                                "DOC-B",
                                "Spring Boot applications can use embedded servers.",
                                2
                        ),

                        new DocumentResult(
                                "DOC-C",
                                "Spring Boot provides production-ready features.",
                                3
                        )
                );

        System.out.println(
                "\n===== Vector Search ====="
        );

        printResults(vectorResults);

        // =====================================================
        // 2. KEYWORD / BM25-STYLE RESULTS
        //
        // In a real system this list would come from
        // PostgreSQL full-text / BM25-style keyword search.
        //
        // Here we use predefined results only to demonstrate
        // the RRF fusion algorithm.
        // =====================================================

        List<DocumentResult> keywordResults =
                List.of(

                        new DocumentResult(
                                "DOC-C",
                                "Spring Boot provides production-ready features.",
                                1
                        ),

                        new DocumentResult(
                                "DOC-A",
                                "Spring Boot provides auto-configuration " +
                                "and simplifies Java application development.",
                                2
                        ),

                        new DocumentResult(
                                "DOC-D",
                                "Spring Boot supports dependency injection.",
                                3
                        )
                );

        System.out.println(
                "\n===== Keyword / BM25 Search ====="
        );

        printResults(keywordResults);

        // =====================================================
        // 3. RRF FUSION
        // =====================================================

        List<DocumentResult> fusedResults =
                reciprocalRankFusion(
                        vectorResults,
                        keywordResults
                );

        System.out.println(
                "\n===== RRF FUSED RESULTS ====="
        );

        for (int i = 0; i < fusedResults.size(); i++) {

            DocumentResult result =
                    fusedResults.get(i);

            System.out.printf(
                    "%d. %s%n",
                    i + 1,
                    result.documentId
            );

            System.out.println(
                    "   " + result.content
            );
        }

        // =====================================================
        // 4. FINAL CONTEXT
        // =====================================================

        int finalTopK =
                Math.min(3, fusedResults.size());

        System.out.println(
                "\n===== FINAL CONTEXT ====="
        );

        for (int i = 0; i < finalTopK; i++) {

            DocumentResult result =
                    fusedResults.get(i);

            System.out.println(
                    "[Source: " +
                    result.documentId +
                    "]"
            );

            System.out.println(
                    result.content
            );

            System.out.println(
                    "-----------------------------"
            );
        }

        // =====================================================
        // 5. PRODUCTION FLOW
        // =====================================================

        System.out.println(
                "\n===== DAY 24 RAG FLOW ====="
        );

        System.out.println(
                "User Query"
        );

        System.out.println(
                "    ↓"
        );

        System.out.println(
                "Vector Search + Keyword Search"
        );

        System.out.println(
                "    ↓"
        );

        System.out.println(
                "RRF Fusion"
        );

        System.out.println(
                "    ↓"
        );

        System.out.println(
                "Top Candidates"
        );

        System.out.println(
                "    ↓"
        );

        System.out.println(
                "Reranker"
        );

        System.out.println(
                "    ↓"
        );

        System.out.println(
                "Final Context"
        );

        System.out.println(
                "    ↓"
        );

        System.out.println(
                "LLM"
        );

        System.out.println(
                "\n===== DAY 24 COMPLETE ====="
        );
    }

    // =========================================================
    // RRF IMPLEMENTATION
    // =========================================================

    private static List<DocumentResult> reciprocalRankFusion(
            List<DocumentResult> vectorResults,
            List<DocumentResult> keywordResults) {

        // RRF constant
        int k = 60;

        Map<String, Double> scores =
                new HashMap<>();

        Map<String, String> contents =
                new HashMap<>();

        // -----------------------------------------------------
        // Vector ranking
        // -----------------------------------------------------

        for (DocumentResult result : vectorResults) {

            double score =
                    1.0 /
                    (k + result.rank);

            scores.merge(
                    result.documentId,
                    score,
                    Double::sum
            );

            contents.put(
                    result.documentId,
                    result.content
            );
        }

        // -----------------------------------------------------
        // Keyword ranking
        // -----------------------------------------------------

        for (DocumentResult result : keywordResults) {

            double score =
                    1.0 /
                    (k + result.rank);

            scores.merge(
                    result.documentId,
                    score,
                    Double::sum
            );

            contents.put(
                    result.documentId,
                    result.content
            );
        }

        // -----------------------------------------------------
        // Sort by RRF score
        // -----------------------------------------------------

        List<String> documentIds =
                new ArrayList<>(
                        scores.keySet()
                );

        documentIds.sort(
                (a, b) ->
                        Double.compare(
                                scores.get(b),
                                scores.get(a)
                        )
        );

        // -----------------------------------------------------
        // Create final result list
        // -----------------------------------------------------

        List<DocumentResult> results =
                new ArrayList<>();

        int finalRank = 1;

        for (String documentId :
                documentIds) {

            results.add(
                    new DocumentResult(
                            documentId,
                            contents.get(documentId),
                            finalRank
                    )
            );

            finalRank++;
        }

        return results;
    }

    // =========================================================
    // PRINT RESULTS
    // =========================================================

    private static void printResults(
            List<DocumentResult> results) {

        for (DocumentResult result :
                results) {

            System.out.println(
                    result.rank +
                    ". " +
                    result.documentId
            );

            System.out.println(
                    "   " +
                    result.content
            );
        }
    }
}