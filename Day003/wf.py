sentence = input("Enter a sentence: ")

# Convert to lowercase
sentence = sentence.lower()

# Remove punctuation
for ch in ",.!?":
    sentence = sentence.replace(ch, "")

# Split into words
words = sentence.split()

# Dictionary to store frequency
frequency = {}

# Count frequency
for word in words:
    if word in frequency:
        frequency[word] += 1
    else:
        frequency[word] = 1

# Print frequency
print("\nWord Frequency:\n")

for word, count in frequency.items():
    print(f"{word} : {count}")