package com.vignesh.ai;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface StudentExtractor {

    @SystemMessage("""
            You are an expert information extractor.

            Extract the student's:
            - Name
            - Age
            - Email

            Return only the extracted information.
            If a birth date is provided instead of age,
calculate the current age accurately.
            """)
    Student extract(@UserMessage String text);

}