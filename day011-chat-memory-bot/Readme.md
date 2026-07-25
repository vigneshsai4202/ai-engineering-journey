# Day 11 - AI Services with Chat Memory | LangChain4j

Today I learned how to build a conversational AI assistant using LangChain4j AI Services and Chat Memory.

## What I Learned

- AI Services
- `@SystemMessage`
- Java Dynamic Proxy
- `MessageWindowChatMemory`
- Builder Pattern
- Stateless LLMs
- Interactive chatbot using Scanner
- Groq API integration

## Project Overview

Built a Java AI Assistant that:

- Accepts user input continuously
- Uses Groq as the LLM
- Maintains conversation history
- Remembers previous user messages
- Behaves like a Java mentor using `@SystemMessage`

## Key Concepts

### AI Services

AI Services provide an abstraction over the language model. Instead of directly calling the model every time, we define an interface and let LangChain4j generate its implementation at runtime using Java Dynamic Proxy.

### System Message

`@SystemMessage` defines the AI's behavior. Every request automatically includes this instruction, ensuring consistent responses.

### Chat Memory

Large Language Models are stateless by default. `MessageWindowChatMemory` stores recent conversation history and sends it with each new request, allowing the assistant to remember previous messages.

### Builder Pattern

`AiServices.builder()` allows additional configurations such as:

- Chat Memory
- RAG
- Tools
- Moderation
- Structured Output

Unlike `AiServices.create()`, the Builder Pattern supports advanced AI features.

## Technologies Used

- Java 17
- Maven
- LangChain4j
- Groq API
- dotenv-java

## Sample Conversation

You: My name is Vignesh

AI: Nice to meet you, Vignesh!

You: What is my name?

AI: Your name is Vignesh.

This demonstrates that Chat Memory is working correctly.

## Key Takeaway

An LLM doesn't remember previous conversations by itself. Chat Memory provides context by storing previous messages and including them in future requests, making conversations feel natural.
