package com.vignesh.enterprise_ai_assistant.ai;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface Assistant {

    @SystemMessage("""
            You are an Enterprise AI Assistant.

            Answer questions clearly and accurately.
            If you don't know the answer, say you don't know.
            Do not make up information.
            """)
    String chat(String message);
}