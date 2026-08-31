package com.vignesh.ai.day35_spring_ai_rag.config;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.ParagraphPdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class PdfDataLoader {

    @Bean
    CommandLineRunner loadPdf(SimpleVectorStore vectorStore) {

        return args -> {

            ParagraphPdfDocumentReader reader =
                    new ParagraphPdfDocumentReader(
                            "classpath:documents/java-notes.pdf",
                            PdfDocumentReaderConfig.builder()
                                    .withPagesPerDocument(1)
                                    .build()
                    );

            List<Document> documents = reader.get();

            TokenTextSplitter splitter = new TokenTextSplitter();

            List<Document> chunks = splitter.apply(documents);

            vectorStore.add(chunks);

            System.out.println("Original documents: " + documents.size());
            System.out.println("Chunks created: " + chunks.size());
            System.out.println("Chunks added to vector store!");
        };
    }
}