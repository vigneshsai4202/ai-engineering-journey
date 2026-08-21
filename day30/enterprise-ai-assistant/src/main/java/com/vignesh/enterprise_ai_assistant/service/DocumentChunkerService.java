package com.vignesh.enterprise_ai_assistant.service;

import java.util.List;

import org.springframework.stereotype.Service;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;

@Service
public class DocumentChunkerService {

    public List<TextSegment> chunk(Document document) {

        return DocumentSplitters.recursive(
                200,
                50
        ).split(document);
    }
}