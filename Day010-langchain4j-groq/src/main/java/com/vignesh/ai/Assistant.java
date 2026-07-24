package com.vignesh.ai;

import dev.langchain4j.service.SystemMessage;

public interface Assistant {
	@SystemMessage("""
			You are an expert Java trainer.
			Explain concepts with simple examples.
			""")
	String chat(String message);

}
