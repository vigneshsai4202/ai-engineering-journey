package com.vignesh.enterprise_ai_assistant.service;

import java.io.InputStream;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;

@Service
public class DocumentLoaderService {

    public Document loadDocument() {

        ClassPathResource resource =
                new ClassPathResource("docs/spring-boot.txt");

        try (InputStream inputStream = resource.getInputStream()) {

            return Document.from(
                    new String(inputStream.readAllBytes())
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to load document", e
            );
        }
    }
}