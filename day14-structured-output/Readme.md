# Day 14 – Structured Output with LangChain4j

## 📌 Objective

Learn how to use Structured Output in LangChain4j to convert natural language into Java objects instead of plain text.

---

## 📚 Topics Covered

- Structured Output
- AI Services
- @SystemMessage
- @UserMessage
- POJO (Plain Old Java Object)
- Object Mapping
- OpenAI Compatible API (Groq)

---

## 🛠️ Technologies Used

- Java 17
- Maven
- LangChain4j 1.18.0
- Groq API
- Llama 3.3 70B

---

## 📖 What is Structured Output?

Structured Output allows an LLM to return data as a Java object instead of plain text.

Instead of returning:

Name: Sai

Age: 22

Email: sai@gmail.com

The AI returns:

```java
Student {
    name = "Sai",
    age = 22,
    email = "sai@gmail.com"
}
```

This makes AI responses easier to use in Java applications.

---

## 💻 Mini Project

### Student Information Extractor

The application extracts structured student information from natural language.

### Example Input

```
My name is Sai.
I am 22 years old.
My email is sai@gmail.com.
```

### Output

```
Student{
name='Sai',
age=22,
email='sai@gmail.com'
}
```

---

## 📂 Project Structure

```
day14-structured-output
│
├── Main.java
├── Student.java
├── StudentExtractor.java
├── pom.xml
└── README.md
```

---

## 🎯 Key Learnings

- Learned AI Services in LangChain4j.
- Understood Structured Output.
- Converted AI responses directly into Java objects.
- Built a Student Information Extractor.
- Used annotations like @SystemMessage and @UserMessage.
- Understood runtime implementation generation.

---

## 🚀 Future Improvements

- Extract additional fields.
- Support multiple students.
- Validate extracted data.
- Store extracted information in a database.

---

## 📅 Next Topic

Day 15 – Chat Memory in LangChain4j
