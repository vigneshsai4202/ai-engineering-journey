# Day 19 — Persistent RAG with PGVector

## Overview

Today I upgraded my Day 18 RAG application by replacing the in-memory vector store with **PostgreSQL + PGVector**.

The main reason was **persistence**.

With `InMemoryEmbeddingStore`, embeddings disappear when the application stops. With PGVector, embeddings are stored permanently in PostgreSQL, so the application can reuse them without re-processing the PDF every time.

## Architecture

### Indexing

```text
PDF
 ↓
Apache PDFBox
 ↓
Chunking
 ↓
TextSegment + Metadata
 ↓
BGE-M3
 ↓
1024D Embeddings
 ↓
PGVector
 ↓
Neon PostgreSQL
```

### Retrieval

```text
User Question
 ↓
BGE-M3
 ↓
Query Embedding
 ↓
PGVector Similarity Search
 ↓
Top-K Results
 ↓
Context
 ↓
Groq
 ↓
Final Answer
```

## Project Files

### `DatabaseTest.java`

First, I tested the PostgreSQL connection independently.

```text
Java → JDBC → Neon PostgreSQL
```

Output confirmed:

```text
✅ Connected to Neon PostgreSQL!
```

### `VectorStoreTest.java`

Next, I tested whether LangChain4j could store and retrieve vectors using PGVector.

I first used a small dummy vector to verify the database integration before using real embeddings.

### `EmbeddingModelTest.java`

I tested the BGE-M3 embedding model separately.

The model converts text into a **1024-dimensional vector**.

### `BgePgVectorTest.java`

After testing both components separately, I connected:

```text
BGE-M3 → PGVector
```

This confirmed that real embeddings could be stored and searched.

### `PdfIndexer.java`

This handles the indexing pipeline:

```text
PDF → Text → Chunks → Metadata → Embeddings → PGVector
```

I used Apache PDFBox to extract readable text from the PDF.

I also processed embeddings in batches because sending hundreds of chunks at once caused a timeout.

### `PdfRetriever.java`

This tests retrieval independently:

```text
Question
 ↓
BGE-M3
 ↓
PGVector
 ↓
Similarity Search
 ↓
Top-K Results
```

The retrieval successfully returned a similarity score of approximately:

```text
0.7514
```

### `PersistentPdfRag.java`

Finally, I combined retrieval with Groq.

The retrieved chunks are added to the prompt as context, and Groq generates the final answer.

The final test correctly answered:

> Spring Boot is used to build production-ready Java applications.

## Metadata

Each stored chunk also contains metadata such as:

```text
documentName
chunkId
```

Example:

```text
Document: sample-java-notes.pdf
Chunk ID: 0
```

This helps identify where retrieved information came from.

## Why PostgreSQL + PGVector?

### Day 18

```text
PDF → Embeddings → InMemoryEmbeddingStore
```

Embeddings were temporary.

### Day 19

```text
PDF → Embeddings → PGVector → PostgreSQL
```

Embeddings are persistent.

This means restarting the application does not require generating all embeddings again.

For a larger application with thousands of documents, persistent storage becomes much more practical.

## Problems I Faced

### PDF parsing issue

Initially, the PDF was being stored as raw `%PDF-1.5` content.

I fixed this by explicitly using:

```text
ApachePdfBoxDocumentParser
```

### Embedding timeout

When hundreds of chunks were sent to BGE-M3 at once, the request timed out.

I solved this by processing chunks in smaller batches.

### Metadata initially returned `null`

The first indexed records did not contain metadata because they were created before metadata was added.

I cleared the table and re-indexed the PDF. After that:

```text
Document: sample-java-notes.pdf
Chunk ID: 0
```

worked correctly.

## Technologies

* Java 17
* Maven
* LangChain4j
* BGE-M3
* Hugging Face
* PostgreSQL
* PGVector
* Neon
* Apache PDFBox
* Groq

## Key Takeaway

Day 19 taught me that a practical RAG system needs two separate stages:

```text
INDEXING
Documents → Chunks → Embeddings → Vector Database
```

and:

```text
RETRIEVAL
Question → Embedding → Similarity Search → Context → LLM
```

The biggest upgrade from Day 18 was moving from temporary in-memory storage to **persistent PostgreSQL + PGVector**, making the RAG architecture more suitable for real applications.
