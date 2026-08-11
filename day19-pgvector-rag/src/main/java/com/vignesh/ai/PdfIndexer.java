package com.vignesh.ai;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.huggingface.HuggingFaceEmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import io.github.cdimascio.dotenv.Dotenv;

import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class PdfIndexer {

    public static void main(String[] args) {

        Dotenv dotenv = Dotenv.load();

        // -----------------------------------------
        // 1. Load PDF
        // -----------------------------------------

        Path pdfPath = Path.of(
                "C:\\Users\\DELL\\Downloads\\sample-java-notes.pdf"
        );

        Document document =
                FileSystemDocumentLoader.loadDocument(
                        pdfPath,
                        new ApachePdfBoxDocumentParser()
                );

        System.out.println("✅ PDF loaded successfully!");

        // -----------------------------------------
        // 2. Split document
        // -----------------------------------------

        var splitter =
                DocumentSplitters.recursive(
                        1000,
                        100
                );

        List<TextSegment> originalSegments =
                splitter.split(document);

        System.out.println(
                "Number of chunks: "
                        + originalSegments.size()
        );

        // -----------------------------------------
        // 3. Add metadata
        // -----------------------------------------

        List<TextSegment> segments =
                new ArrayList<>();

        for (int i = 0;
             i < originalSegments.size();
             i++) {

            TextSegment original =
                    originalSegments.get(i);

            TextSegment segment =
                    TextSegment.from(
                            original.text()
                    );

            segment.metadata().put(
                    "documentName",
                    pdfPath.getFileName().toString()
            );

            segment.metadata().put(
                    "chunkId",
                    String.valueOf(i)
            );

            segments.add(segment);
        }

        System.out.println(
                "✅ Metadata added to chunks!"
        );

        // -----------------------------------------
        // 4. BGE-M3
        // -----------------------------------------

        EmbeddingModel embeddingModel =
                HuggingFaceEmbeddingModel.builder()
                        .accessToken(
                                dotenv.get("HF_API_KEY")
                        )
                        .modelId("BAAI/bge-m3")
                        .timeout(Duration.ofMinutes(5))
                        .build();

        System.out.println(
                "✅ BGE-M3 ready!"
        );

        // -----------------------------------------
        // 5. PGVector
        // -----------------------------------------

        EmbeddingStore<TextSegment> embeddingStore =
                PgVectorEmbeddingStore.builder()
                        .host(dotenv.get("NEON_HOST"))
                        .port(
                                Integer.parseInt(
                                        dotenv.get("NEON_PORT")
                                )
                        )
                        .database(dotenv.get("NEON_DATABASE"))
                        .user(dotenv.get("NEON_USERNAME"))
                        .password(dotenv.get("NEON_PASSWORD"))
                        .table("day19_pdf_embeddings")
                        .dimension(1024)
                        .build();

        System.out.println(
                "✅ PGVector ready!"
        );

        // -----------------------------------------
        // 6. Process in batches
        // -----------------------------------------

        int batchSize = 10;

        for (int start = 0;
             start < segments.size();
             start += batchSize) {

            int end =
                    Math.min(
                            start + batchSize,
                            segments.size()
                    );

            List<TextSegment> batch =
                    segments.subList(start, end);

            System.out.println(
                    "Processing chunks "
                            + (start + 1)
                            + " - "
                            + end
            );

            var embeddings =
                    embeddingModel
                            .embedAll(batch)
                            .content();

            embeddingStore.addAll(
                    embeddings,
                    batch
            );

            System.out.println(
                    "✅ Stored chunks "
                            + (start + 1)
                            + " - "
                            + end
            );
        }

        System.out.println(
                "\n🎉 PDF indexing completed!"
        );
    }
}