# Day 5 - Understanding LLMs

## What I thought an LLM was before
Before this day i Know about LLM is it just do the tasks which we were given it can be anything like text generation,sumarization. 

## What an LLM actually does
By going to various resouces and watching the tutorials today i Got to know that LLM's are  langauage models which are subset of NLP .This LLM has capblilities of text summarization,genration,

## What is a Token?
-Tokens are small units of text created by a tokenizer from the original text.

## What is an Embedding?
-Each token in convereted to high dimensional vector of numbers where words with similar meaning cluster together.


## What is a Transformer?
-Transformers are neural network architures that makes LLM work possible

-It consits of  various process like Tokenization,Embeddings,Attention Blocks,Multi-Layer Perceptron (MLP) Blocks,Prediction
-Attention Blocks- Finds relationships and relevance between tokens in the context.
-MLP block-processes and transforms each token's representation using a neural network.
-Prediction- After series of above process the context rich vector is runned through an unembedding matrix and a softmax function to convert the numbers into an probablity distrubtion of the next word

## What is Context?
-Context is the information currently available to the model.

## What does Temperature do?
-Temperature is the parameter that used in predicting the next word .Based on these values the next word prediction or genration may be change like it may be more genric,creative

## Model vs AI Application
-Model is the the trained computaional system that can be used in any AI application

## Why I think LangChain4j exists
-As LangChain4j is the opensource library that can help the devleopers to integrate the LLM in the java based applications