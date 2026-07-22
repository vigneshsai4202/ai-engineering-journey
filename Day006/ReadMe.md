# Day 6 - LLM Engineering Fundamentals Part 2

## Training vs Inference
-Training is the process of making an model to perform an action based on which it is tranined
-Inference is the performing an action based on the traning of an model and patterns reconized by the trained model

## Pre-training vs Fine-tuning
-Pre -traning is the intial stage of traning where it learns form the huge amount of the data.
-fine tuning is the extra step that done of pre-trained model and it is done  specfic task purpose

## How an LLM Generates Text
-LLM uses the Tranformer architure
-Eg:-"JAVA IS A PROGRAMMING"
-Converts the sentence into tokens ,these tokens are converted into the vectors based on the values it will generate the next probable tokens and it selct the token and added to the context and finally response is given.

## Next Token Prediction
-Next Token prediction is based on the context give to an LLM 

## Model Parameters
-Model parameters and irctly linked to trained data as the trained data changes the parametrs values also changes .example of parameters are weights and bias

## Stateless LLM vs Chat Memory
-Stateless LLM is dosent remebr prevous requests Chat memory is managed by the application where it stores prevoius coversation and sends revleant context back to LLM.

## Why LLMs Hallucinate
-Beacuse it will not verify for trusted sources.

## Chat Model vs Embedding Model
-Chat model gives the output as human readable text where embedded model gives the vectors(num) as output

## Hosted Model vs Local Model
-hosted Model runs on a cloud provider remotely where as local model is the model that runs in our pc.

## Choosing the Right Model
-based on the requirment of needs,metrics like accuracy,testing an model before going to choose,cost,latency

## My Key Takeaway
LLM generates responses through next-token prediction, while memory and fact verification are separate concerns that the application must handle.