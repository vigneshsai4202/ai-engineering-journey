def clean_text(text):
    for char in ".,!?":
        text = text.replace(char, "")
    return text


def count_total_words(words):
    return len(words)


def count_unique_words(words):
    return len(set(words))


def find_longest_word(words):
    return max(words, key=len)


def filter_long_words(words):
    return [word for word in words if len(word) > 4]


text = input("Enter text: ")

cleaned_text = clean_text(text).lower()
words = cleaned_text.split()

total_words = count_total_words(words)
unique_words = count_unique_words(words)
longest_word = find_longest_word(words)
long_words = filter_long_words(words)

print("\nTotal Words:", total_words)
print("Unique Words:", unique_words)
print("Longest Word:", longest_word)
print("Words longer than 4 characters:", long_words)