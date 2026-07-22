My payment failed twice. Money was deducted from my account
but the order is still showing as unpaid.

Prompt 1:

Analyze this ticket.

Prompt 2:

You are a customer support ticket classifier.

Analyze the following support ticket.

Return:
Category
Priority
Summary

Ticket:
"My payment failed twice. Money was deducted from my account
but the order is still showing as unpaid."

Prompt 3:

You are a customer support ticket classification system.

Allowed categories:
PAYMENT
ACCOUNT
DELIVERY
TECHNICAL
OTHER

Priority rules:
HIGH = money loss, security issue, or service unavailable
MEDIUM = feature partially working
LOW = general question

Return only JSON using this structure:

{
  "category": "",
  "priority": "",
  "summary": ""
}

Ticket:
"My payment failed twice. Money was deducted from my account
but the order is still showing as unpaid."


1.Why was Prompt 1 less predictable than Prompt 3?
-Because in the prompt1 we have not give any role or type od output we needed overall we didnt give detailed expalantion of the question and output we neede.
2.What is the purpose of a system message?
-The main purpose of a system message it consits of set rules how should it behave in the through out converstion which can inceasre the quality of output.
3.Give your own example of zero-shot and few-shot prompting.
Zero-Shot Prompting Example: 
Classify this ticket into:

PAYMENT
ACCOUNT
DELIVERY
Few-Shot Prompting Example:
Ticket:
Payment failed

Output:
PAYMENT

----------------

Ticket:
Password reset issue

Output:
ACCOUNT

----------------

Now classify:

"My money was deducted..."

4.Why is structured output important for a Java backend?
 -If ouptput is structed like JSON,So that  it can directly map into the object that are part of output
5. Suppose AI returns

{
"priority":"URGENT"
}

But Java only allows

HIGH
MEDIUM
LOW
Even though the prompt restricts the values to HIGH, MEDIUM, and LOW, the Java application should still validate the response. If the LLM returns an invalid value such as URGENT, the application should reject it, retry, or handle it according to business rules instead of assuming it is correct.
