# Day 4 - Text Analyzer

## About

Built a simple Text Analyzer using Python functions.

The program takes text as input and finds the total words, unique words, longest word, and words longer than 4 characters.

## Topics I Learned

- Functions
- Parameters and Arguments
- Return Values
- Default Arguments
- `*args` and `**kwargs`
- Lambda Functions
- List Comprehension

## Concepts Used

I divided the program into separate functions so each function handles one task.

Used `set()` to count unique words.

```python
len(set(words))
```

Used `max()` with `key=len` to find the longest word.

```python
max(words, key=len)
```

Used list comprehension to filter words.

```python
[word for word in words if len(word) > 4]
```

## Sample Output

```text
Total Words: 10
Unique Words: 7
Longest Word: changing
Words longer than 4 characters: ['changing', 'world', 'growing']
```

## What I Learned

Coming from Java, functions were easy to understand because they are similar to methods.

The main thing I learned today was writing cleaner Python code using separate functions and list comprehensions.

## Future Improvements

- Handle empty input
- Read text from files
- Remove more special characters

**Day 4 of my 100 Days of AI Engineering journey.**

## 🔗 Connect with Me

GitHub: https://github.com/vigneshsai4202

X: https://x.com/SAIVIGN34633771

##  AI Engineering Journey

This project is part of my **100 Days of AI Engineering** challenge, where I'm documenting my learning journey by building one project every day.

⭐ If you find this repository useful, feel free to star it.