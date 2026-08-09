# Day 18 - PDF RAG Assistant with LangChain4j

Part of my **100 Days of AI Engineering** journey.

In Day 16 and Day 17, I focused on understanding RAG, embeddings, vector stores, semantic search, retrieval, and context injection.

In Day 18, I implemented those concepts by building a PDF-based RAG assistant using Java and LangChain4j.

The application reads a PDF, splits it into chunks, creates embeddings, stores them, retrieves relevant chunks for a user's question, and uses an LLM to generate a grounded answer.

---

## What I Learned

### Day 16 - RAG Fundamentals

- What is RAG and why it is needed
- Retrieval vs Generation
- Document loading and chunking
- Role of embeddings and retrievers
- Context injection
- RAG pipeline
- RAG vs Fine-Tuning
- Real-world RAG applications

### Day 17 - Embeddings & Semantic Search

- What embeddings are
- Why text is converted into vectors
- Embedding Model vs LLM
- Vector stores
- Semantic search
- Similarity search
- Top-K retrieval

I used **BAAI/bge-m3** as the embedding model.

BGE-M3 produced **1024-dimensional embeddings** in my implementation.

---

# Day 18 - Building the PDF RAG Assistant

## Tech Stack

- Java
- Maven
- LangChain4j 1.18.0
- Apache PDFBox
- BGE-M3
- Hugging Face
- Groq
- Llama 3.3 70B
- InMemoryEmbeddingStore

## RAG Pipeline

```text
PDF
 ↓
PDF Parser
 ↓
Document Chunks
 ↓
BGE-M3 Embeddings
 ↓
EmbeddingStore
 ↓
User Question
 ↓
Query Embedding
 ↓
Similarity Search
 ↓
Top-K Relevant Chunks
 ↓
Context Injection
 ↓
Groq LLM
 ↓
Grounded Answer
