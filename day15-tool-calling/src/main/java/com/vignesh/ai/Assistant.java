package com.vignesh.ai;
import dev.langchain4j.service.UserMessage;

public interface Assistant {
	String chat(@UserMessage String message);

}
