package com.vignesh.ai;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.rag.query.Query;
import dev.langchain4j.rag.query.transformer.ExpandingQueryTransformer;
import dev.langchain4j.rag.query.transformer.QueryTransformer;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.Collection;

public class QueryTransformerTest {

    public static void main(String[] args) {

        Dotenv dotenv = Dotenv.load();

        // =========================================
        // 1. Groq Chat Model
        // =========================================

        ChatModel chatModel =
                OpenAiChatModel.builder()
                        .apiKey(dotenv.get("GROQ_API_KEY"))
                        .baseUrl("https://api.groq.com/openai/v1")
                        .modelName("llama-3.3-70b-versatile")
                        .build();

        System.out.println("✅ Groq model ready!");

        // =========================================
        // 2. LangChain4j QueryTransformer
        // =========================================

        QueryTransformer queryTransformer =
                ExpandingQueryTransformer.builder()
                        .chatModel(chatModel)
                        .n(3)
                        .build();

        System.out.println(
                "✅ LangChain4j QueryTransformer ready!"
        );

        // =========================================
        // 3. Original User Query
        // =========================================

        Query query =
                Query.from(
                        "What is Spring Boot used for?"
                );

        System.out.println(
                "\n===== Original Query ====="
        );

        System.out.println(
                query.text()
        );

        // =========================================
        // 4. Transform Query
        // =========================================

        Collection<Query> transformedQueries =
                queryTransformer.transform(query);

        // =========================================
        // 5. Display Results
        // =========================================

        System.out.println(
                "\n===== Transformed Queries ====="
        );

        int count = 1;

        for (Query transformedQuery :
                transformedQueries) {

            System.out.println(
                    count + ". "
                            + transformedQuery.text()
            );

            count++;
        }

        System.out.println(
                "\nTotal queries: "
                        + transformedQueries.size()
        );
    }
}