Day 51 — Planner → Executor Agent Workflow 🤖
Overview

Today I built a Planner → Executor workflow using Spring AI.

Instead of directly asking an LLM to solve a complex task, the system first creates a plan and then executes the required steps using Java tools.

Architecture
User Request
     ↓
Planner
     ↓
Task Plan
     ↓
Executor
     ↓
Java Tools
     ↓
Workflow State
     ↓
Final Answer
What I Learned
LLM-based task planning
Sequential workflow execution
Tool-based execution
Workflow state tracking
Preventing LLM hallucination by using real tool results
Separating planning from execution
Tools
Employee Tool

Retrieves employee information such as:

Employee ID
Name
Role
Department
Annual salary
Calculator Tool

Calculates percentage-based values such as an employee's bonus.

Example

Request:

Calculate the 10% bonus for employee EMP101.

Generated plan:

1. Retrieve employee information
2. Extract salary
3. Calculate 10% bonus
4. Compile summary
5. Present the result

Result:

Employee Bonus Summary

Employee Name: Vignesh
Employee ID: EMP101
Role: Java Developer
Department: Engineering
Annual Salary: 600,000 INR
Bonus Amount: 60,000 INR (10%)
Workflow State

The workflow tracks:

Current Step
Completed Steps
Tool Results
Status

Example:

Current Step: 3
Status: COMPLETED
Key Takeaway

An agent becomes more controllable when planning, execution, tools, and state are separated into clear stages.

Tech Stack
Java 17
Spring Boot 4.1.1
Spring AI 2.0.1
Groq
Qwen 3.8 27B
Maven

Day 51 completed 🚀

#100DaysOfAIEngineering #Java #SpringAI #AgenticAI #GenAI
