package com.vignesh.ai.day41_spring_ai_hybrid_rag.service;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class KeywordSearchService {

    private final JdbcTemplate jdbcTemplate;

    public KeywordSearchService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Document> search(String question) {

        String sql = """
                SELECT id, content, metadata
                FROM vector_store
                WHERE to_tsvector('english', content)
                      @@ plainto_tsquery('english', ?)
                LIMIT 3
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {

        	Document document = new Document(
        	        rs.getString("id"),
        	        rs.getString("content"),
        	        java.util.Map.of()
        	);
            document.getMetadata().put(
                    "id",
                    rs.getString("id")
            );

            return document;
        }, question);
    }
}