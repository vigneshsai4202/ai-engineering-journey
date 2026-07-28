# Day 13 – Prompt Templates with LangChain4j

## 📌 Objective

Learn how to create reusable and dynamic prompts using PromptTemplate in LangChain4j and integrate them with the Groq LLM.

---

## 📚 Topics Covered

- What is Prompt Engineering?
- What are Prompt Templates?
- Dynamic Prompt Creation
- Placeholder Replacement
- Using `PromptTemplate`
- Using `Map.of()` for variables
- Connecting LangChain4j with Groq
- Sending prompts to an LLM
- Receiving AI-generated responses

---

## 🛠️ Technologies Used

- Java 17
- Maven
- LangChain4j 1.18.0
- Groq API
- Llama 3.3 70B Versatile

---

## 📖 What is a Prompt Template?

A Prompt Template is a reusable prompt containing placeholders. Instead of writing a new prompt every time, placeholders are replaced with dynamic values at runtime.

Example:

```text
You are an expert {{role}}.

Hello {{name}}.

Explain {{topic}} in simple words with an example.
```

---

## 📌 How It Works

1. Read user input.
2. Create a PromptTemplate.
3. Replace placeholders using `Map.of()`.
4. Generate the final prompt.
5. Send the prompt to the Groq LLM.
6. Receive the AI response.
7. Display the response on the console.

---

## 💻 Mini Project

### Personalized AI Tutor

The application accepts:

- Name
- Role
- Topic

It generates a personalized prompt and sends it to the Groq LLM.

### Example Input

```
Enter your name: Sai
Enter your role: Java Developer
Enter topic: Strings
```

### Generated Prompt

```text
You are an expert Java Developer.

Hello Sai.

Explain Strings in simple words with an example.
```

### Sample Output

```text
Hello Sai,

A String in Java is a sequence of characters used to represent text. For example:

String name = "Sai";

Strings provide many built-in methods such as length(), substring(), and toUpperCase().
```

---

## 📂 Project Structure

```
day13-prompt-templates
│
├── src
│   └── main
│       └── java
│           └── com
│               └── vignesh
│                   └── ai
│                       ├── Main.java
│                       └── Assistant.java
│
├── pom.xml
└── README.md
```

---

## 🎯 Key Learnings

- Understood Prompt Engineering fundamentals.
- Learned how PromptTemplate works.
- Used placeholders for dynamic prompt generation.
- Connected Java applications with the Groq API.
- Sent prompts to an LLM using LangChain4j.
- Generated AI responses dynamically.
- Built a reusable AI-powered console application.

---

## 🚀 Future Improvements

- Accept multiple questions in a loop.
- Add chat memory.
- Store conversation history.
- Build a GUI using JavaFX or Swing.
- Develop a web version using Spring Boot.

---

## 📖 Conclusion

This project demonstrated how Prompt Templates make prompts reusable and dynamic. Instead of manually creating prompts for every request, placeholders allow user-specific information to be inserted at runtime, making AI applications cleaner, scalable, and easier to maintain.

---

## 📅 Next Topic

**Day 14 – Structured Output in LangChain4j**
