# Day 12 - Multi User Chat Memory with LangChain4j

Today I learned how to build a multi-user AI chatbot using LangChain4j. Instead of sharing one chat memory for all users, each user gets their own conversation history using `ChatMemoryProvider` and `@MemoryId`.

## What I Learned

- ChatMemoryProvider
- @MemoryId
- Multi-user chat memory
- MessageWindowChatMemory
- User-specific conversation history
- Groq API integration
- Interactive chatbot using Scanner

## Project Overview

Built a Java AI chatbot that:

- Accepts user input from the console
- Uses the Groq API through LangChain4j
- Supports multiple users
- Maintains a separate conversation history for each user
- Prevents conversations from mixing

## Key Concepts

### @MemoryId

`@MemoryId` is a unique identifier for a user or conversation. It allows LangChain4j to retrieve the correct chat memory for each request.

### ChatMemoryProvider

`ChatMemoryProvider` creates or retrieves a separate `MessageWindowChatMemory` for every user based on the provided `memoryId`. This ensures that each user's conversation remains isolated.

### MessageWindowChatMemory

`MessageWindowChatMemory` stores the conversation history for a specific user and keeps only the most recent messages based on the configured window size.

## Why ChatMemoryProvider?

Using a single `MessageWindowChatMemory` works only for one user.

In a real-world application, multiple users interact with the AI simultaneously. `ChatMemoryProvider` ensures that every user has their own chat memory, preventing conversation histories from mixing.

## Technologies Used

- Java 17
- Maven
- LangChain4j 1.18.0
- Groq API
- dotenv-java

## Sample Conversation

User 101

You: My name is Vignesh

AI: Nice to meet you, Vignesh!

You: What is my name?

AI: Your name is Vignesh.

---

User 202

You: My name is Rahul

AI: Nice to meet you, Rahul!

You: What is my name?

AI: Your name is Rahul.

Both users have independent conversation histories.

## Key Takeaway

`MessageWindowChatMemory` stores conversation history, while `ChatMemoryProvider` manages separate chat memories for different users. Together, they enable scalable, multi-user AI chat applications.
