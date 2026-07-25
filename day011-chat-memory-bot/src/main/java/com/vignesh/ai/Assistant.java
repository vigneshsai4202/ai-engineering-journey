package com.vignesh.ai;

import dev.langchain4j.service.SystemMessage;

public interface Assistant {

    @SystemMessage("""
            You are an expert Java mentor.
            Explain Java concepts in simple language.
            Give examples whenever possible.
            """)
    String chat(String userMessage);
}