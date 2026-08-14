package com.vignesh.ai;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.data.segment.TextSegment;

import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.huggingface.HuggingFaceEmbeddingModel;

import dev.langchain4j.rag.AugmentationRequest;
import dev.langchain4j.rag.AugmentationResult;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;

import dev.langchain4j.rag.content.aggregator.ContentAggregator;
import dev.langchain4j.rag.content.aggregator.DefaultContentAggregator;

import dev.langchain4j.rag.content.injector.ContentInjector;
import dev.langchain4j.rag.content.injector.DefaultContentInjector;

import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;

import dev.langchain4j.rag.query.Metadata;

import dev.langchain4j.rag.query.router.DefaultQueryRouter;
import dev.langchain4j.rag.query.router.QueryRouter;

import dev.langchain4j.rag.query.transformer.DefaultQueryTransformer;
import dev.langchain4j.rag.query.transformer.QueryTransformer;

import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;

import io.github.cdimascio.dotenv.Dotenv;

import java.util.List;

public class AdvancedRagAbstractionsTest {

    public static void main(String[] args) {

        // =====================================================
        // 1. LOAD ENVIRONMENT VARIABLES
        // =====================================================

        Dotenv dotenv = Dotenv.load();

        // =====================================================
        // 2. BGE-M3 EMBEDDING MODEL
        // =====================================================

        EmbeddingModel embeddingModel =
                HuggingFaceEmbeddingModel.builder()
                        .accessToken(dotenv.get("HF_API_KEY"))
                        .modelId("BAAI/bge-m3")
                        .build();

        System.out.println("✅ BGE-M3 ready!");

        // =====================================================
        // 3. PGVECTOR
        // =====================================================

        EmbeddingStore<TextSegment> embeddingStore =
                PgVectorEmbeddingStore.builder()
                        .host(dotenv.get("NEON_HOST"))
                        .port(Integer.parseInt(dotenv.get("NEON_PORT")))
                        .database(dotenv.get("NEON_DATABASE"))
                        .user(dotenv.get("NEON_USERNAME"))
                        .password(dotenv.get("NEON_PASSWORD"))
                        .table("day20_test_embeddings")
                        .dimension(1024)
                        .build();

        System.out.println("✅ PGVector connected!");

        // =====================================================
        // 4. CONTENT RETRIEVER
        // =====================================================

        ContentRetriever contentRetriever =
                EmbeddingStoreContentRetriever.builder()
                        .embeddingModel(embeddingModel)
                        .embeddingStore(embeddingStore)
                        .maxResults(5)
                        .minScore(0.70)
                        .build();

        System.out.println("✅ ContentRetriever ready!");

        // =====================================================
        // 5. QUERY TRANSFORMER
        //
        // DefaultQueryTransformer passes the query through
        // without changing it.
        //
        // We will replace this with ExpandingQueryTransformer
        // later.
        // =====================================================

        QueryTransformer queryTransformer =
                new DefaultQueryTransformer();

        System.out.println("✅ QueryTransformer ready!");

        // =====================================================
        // 6. QUERY ROUTER
        //
        // Routes the query to the configured retriever.
        // =====================================================

        QueryRouter queryRouter =
                new DefaultQueryRouter(
                        List.of(contentRetriever)
                );

        System.out.println("✅ QueryRouter ready!");

        // =====================================================
        // 7. CONTENT AGGREGATOR
        //
        // Combines results from queries/retrievers.
        // Default implementation uses RRF.
        // =====================================================

        ContentAggregator contentAggregator =
                new DefaultContentAggregator();

        System.out.println("✅ ContentAggregator ready!");

        // =====================================================
        // 8. CONTENT INJECTOR
        //
        // Injects retrieved content into the UserMessage.
        // =====================================================

        ContentInjector contentInjector =
                new DefaultContentInjector();

        System.out.println("✅ ContentInjector ready!");

        // =====================================================
        // 9. RETRIEVAL AUGMENTOR
        //
        // Orchestrates:
        //
        // QueryTransformer
        //       ↓
        // QueryRouter
        //       ↓
        // ContentRetriever
        //       ↓
        // ContentAggregator
        //       ↓
        // ContentInjector
        // =====================================================

        RetrievalAugmentor retrievalAugmentor =
                DefaultRetrievalAugmentor.builder()
                        .queryTransformer(queryTransformer)
                        .queryRouter(queryRouter)
                        .contentAggregator(contentAggregator)
                        .contentInjector(contentInjector)
                        .build();

        System.out.println("✅ RetrievalAugmentor ready!");

        // =====================================================
        // 10. USER QUESTION
        // =====================================================

        String question =
                "What is Spring Boot used for?";

        UserMessage userMessage =
                UserMessage.from(question);

        System.out.println(
                "\n===== Original User Message ====="
        );

        System.out.println(
                userMessage.singleText()
        );

        // =====================================================
        // 11. RAG METADATA
        //
        // IMPORTANT:
        // This Metadata is RAG-query metadata.
        // It is NOT the document metadata stored in PGVector.
        // =====================================================

        Metadata metadata =
                Metadata.from(
                        userMessage,
                        null,
                        List.of()
                );

        // =====================================================
        // 12. AUGMENTATION REQUEST
        // =====================================================

        AugmentationRequest augmentationRequest =
                new AugmentationRequest(
                        userMessage,
                        metadata
                );

        // =====================================================
        // 13. RUN RETRIEVAL AUGMENTOR
        // =====================================================

        AugmentationResult augmentationResult =
                retrievalAugmentor.augment(
                        augmentationRequest
                );

        // =====================================================
        // 14. GET AUGMENTED MESSAGE
        // =====================================================

        ChatMessage augmentedMessage =
                augmentationResult.chatMessage();

        // =====================================================
        // 15. DISPLAY RESULT
        // =====================================================

        System.out.println(
                "\n===== Augmented User Message ====="
        );

        System.out.println(
                augmentedMessage
        );

        // =====================================================
        // 16. DISPLAY RETRIEVED CONTENT
        // =====================================================

        System.out.println(
                "\n===== Retrieved Content ====="
        );

        augmentationResult.contents()
                .forEach(content -> {

                    System.out.println(
                            content.textSegment().text()
                    );

                    System.out.println(
                            "-----------------------------"
                    );
                });

        // =====================================================
        // 17. FINAL FLOW
        // =====================================================

        System.out.println(
                "\n===== DAY 22 COMPLETE ====="
        );

        System.out.println(
                "Query"
        );

        System.out.println(
                "  ↓"
        );

        System.out.println(
                "QueryTransformer"
        );

        System.out.println(
                "  ↓"
        );

        System.out.println(
                "QueryRouter"
        );

        System.out.println(
                "  ↓"
        );

        System.out.println(
                "ContentRetriever"
        );

        System.out.println(
                "  ↓"
        );

        System.out.println(
                "ContentAggregator"
        );

        System.out.println(
                "  ↓"
        );

        System.out.println(
                "ContentInjector"
        );

        System.out.println(
                "  ↓"
        );

        System.out.println(
                "Augmented UserMessage"
        );

        System.out.println(
                "  ↓"
        );

        System.out.println(
                "LLM"
        );
    }
}