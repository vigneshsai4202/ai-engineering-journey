package com.vignesh.ai;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface Assistant {

    @SystemMessage("""
            You are a helpful Java mentor.
            Explain Java concepts simply with examples.
            """)
    String chat(@MemoryId int memoryId,
                @UserMessage String userMessage);
}