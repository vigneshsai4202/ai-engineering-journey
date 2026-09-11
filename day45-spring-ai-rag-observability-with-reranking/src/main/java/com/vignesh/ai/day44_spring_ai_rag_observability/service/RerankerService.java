package com.vignesh.ai.day44_spring_ai_rag_observability.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class RerankerService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public RerankerService(
            RestClient.Builder restClientBuilder) {

        this.restClient = restClientBuilder
                .baseUrl("https://api.jina.ai")
                .build();

        this.objectMapper = new ObjectMapper();
    }

    public List<Document> rerank(
            String query,
            List<Document> documents) {

        if (documents == null || documents.isEmpty()) {
            return List.of();
        }

        try {

            List<String> documentTexts =
                    documents.stream()
                            .map(Document::getText)
                            .toList();

            String requestBody = """
                    {
                      "model": "jina-reranker-v2-base-multilingual",
                      "query": %s,
                      "documents": %s,
                      "top_n": 3
                    }
                    """.formatted(
                    objectMapper.writeValueAsString(query),
                    objectMapper.writeValueAsString(documentTexts)
            );

            String response = restClient.post()
                    .uri("/v1/rerank")
                    .header(
                            "Authorization",
                            "Bearer " + System.getenv("JINA_API_KEY")
                    )
                    .header(
                            "Content-Type",
                            "application/json"
                    )
                    .body(requestBody)
                    .retrieve()
                    .body(String.class);

            JsonNode root =
                    objectMapper.readTree(response);

            JsonNode results =
                    root.get("results");

            List<Document> rerankedDocuments =
                    new ArrayList<>();

            for (JsonNode result : results) {

                int index =
                        result.get("index").asInt();

                double score =
                        result.get("relevance_score").asDouble();

                Document document =
                        documents.get(index);

                System.out.println(
                        "Rerank Score: "
                                + score
                );

                rerankedDocuments.add(document);
            }

            return rerankedDocuments;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Reranking failed",
                    e
            );
        }
    }
}