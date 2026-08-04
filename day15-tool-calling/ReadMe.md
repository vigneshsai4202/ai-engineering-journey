# Day 15 – Tool Calling with LangChain4j

##  Objective

Learnt how to use Tool Calling in LangChain4j to allow an LLM to invoke Java methods for performing real-world tasks instead of relying only on its trained knowledge.

---

##  Topics Covered

- Tool Calling
- @Tool Annotation
- Tool Registration
- Tool Execution Lifecycle
- Java Method Invocation



---

##  What is Tool Calling?

Tool Calling enables an LLM to execute external Java methods whenever it requires live information or needs to perform a specific action.

Instead of generating every response from its training data, the LLM can call Java methods, APIs, or databases and use the returned result to generate an accurate response.

---

##  Mini Project

### AI Utility Assistant

The assistant uses Tool Calling to execute Java methods automatically.

### Supported Tools

- Multiply two numbers
- Get today's date
- Get current time
- Convert text to uppercase
- Count words in a sentence

---

##  Key Learnings

- Learned Tool Calling in LangChain4j.
- Understood the purpose of the @Tool annotation.
- Built an AI assistant capable of invoking Java methods.
- Learned how LangChain4j registers and executes tools.
- Understood the complete Tool Calling lifecycle.
- Reduced reliance on LLM-generated responses by integrating external functionality.

---

##  Real-World Applications

- Banking Assistants
- HR Chatbots
- Customer Support Systems
- Calendar Scheduling
- Weather Information
- E-commerce Order Tracking

---

##  Next Topic

Day 16 – Retrieval-Augmented Generation (RAG) Fundamentals
