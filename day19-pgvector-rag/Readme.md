# Day 20 — Advanced RAG Retrieval with Reranking

## Overview

Today I upgraded my Day 19 persistent RAG system by improving the **retrieval stage**.

In Day 19, the system retrieved the Top-K most similar chunks from PGVector and directly passed them to the LLM.

In Day 20, I added **metadata filtering, similarity thresholds and reranking** so that the LLM receives more relevant context.

The main goal was to improve **retrieval precision** and reduce irrelevant context.

## Architecture

### Retrieval

```text
User Question
 ↓
BGE-M3
 ↓
Query Embedding
 ↓
Metadata Filter
 ↓
PGVector Similarity Search
 ↓
Top-K + minScore
 ↓
Jina Reranker
 ↓
Best Context
 Project Files
DatabaseTest.java

Tests the connection to Neon PostgreSQL.

Java → JDBC → Neon PostgreSQL
MetadataFilterTest.java

Tests metadata filtering using fields such as:

documentName
chunkId

This allows retrieval to focus on a specific document.

AdvancedRetriever.java

Tests:

Top-K = 5
minScore = 0.70

This controls how many results are returned and removes results below the similarity threshold.

Day20TestData.java

Creates five test chunks related to Spring Boot, PostgreSQL, Docker and Java.

The chunks are stored in:

day20_test_embeddings

This provides multiple candidates for reranking.

Day20RetrievalBaseline.java

Tests PGVector retrieval before reranking.

For:

What is Spring Boot used for?

PGVector returned:

Spring Boot → 0.8457
Spring Boot → 0.7965
PostgreSQL  → 0.6668
Java        → 0.6637
Docker      → 0.6569
JinaRerankerTest.java

Tests Jina reranking on the same candidates.

Spring Boot → 0.7431
Spring Boot → 0.5111
PostgreSQL  → 0.0675
Docker      → 0.0592
Java        → 0.0481

This showed how reranking improves relevance selection.

AdvancedRag.java

Combines the complete pipeline:

Question
 ↓
BGE-M3
 ↓
Metadata Filter
 ↓
PGVector
 ↓
Top-K
 ↓
Jina Reranker
 ↓
Best 2 Chunks
 ↓
Groq
 ↓
Answer + Sources

The final test correctly answered:

Spring Boot is used to build production-ready applications.

Sources were also returned:

day20-test-document | Chunk 0
day20-test-document | Chunk 1
Why Reranking?

Vector search finds semantically similar candidates, but some may be only loosely related.

Reranking evaluates the retrieved candidates against the actual question and helps select the most relevant context.

Vector Search
 ↓
Candidates
 ↓
Reranker
 ↓
Best Context
 ↓
LLM
Day 19 vs Day 20
Day 19
Question
 ↓
Embedding
 ↓
PGVector
 ↓
Top-K
 ↓
LLM

Focus: Persistent vector storage and basic retrieval.

Day 20
Question
 ↓
Embedding
 ↓
Metadata Filter
 ↓
PGVector
 ↓
Top-K + minScore
 ↓
Jina Reranker
 ↓
Best Context
 ↓
LLM

Focus: Better retrieval quality.

How I Tested

I tested each component separately:

PostgreSQL
 ↓
Metadata Filtering
 ↓
Top-K + minScore
 ↓
Test Dataset
 ↓
PGVector Baseline
 ↓
Jina Reranking
 ↓
Complete RAG

This made it easier to verify each stage before combining them.

Technologies
Java 17
Maven
LangChain4j
BGE-M3
Neon PostgreSQL
PGVector
Jina Reranker
Groq / Llama
Key Takeaway

Day 20 taught me that good RAG is not only about retrieving similar information.

The system should:

Retrieve → Filter → Rerank → Select Context → Generate

This produces more relevant context and makes the RAG pipeline closer to a production-style architecture
Groq
 ↓
Final Answer + Sources
