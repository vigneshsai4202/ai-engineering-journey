package com.vignesh.ai.day44_spring_ai_rag_observability.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Service;

@Service
public class RagMetricsService {

    private final Counter ragRequests;

    private final Counter ragErrors;

    private final Timer retrievalTimer;

    private final Timer llmTimer;

    public RagMetricsService(
            MeterRegistry meterRegistry) {

        ragRequests =
                Counter.builder("rag.requests")
                        .description("Total RAG requests")
                        .register(meterRegistry);

        ragErrors =
                Counter.builder("rag.errors")
                        .description("Total RAG errors")
                        .register(meterRegistry);

        retrievalTimer =
                Timer.builder("rag.retrieval.latency")
                        .description(
                                "RAG retrieval latency"
                        )
                        .register(meterRegistry);

        llmTimer =
                Timer.builder("rag.llm.latency")
                        .description(
                                "RAG LLM latency"
                        )
                        .register(meterRegistry);
    }

    public void request() {
        ragRequests.increment();
    }

    public void error() {
        ragErrors.increment();
    }

    public Timer.Sample startTimer() {
        return Timer.start();
    }

    public void recordRetrieval(
            Timer.Sample sample) {

        sample.stop(retrievalTimer);
    }

    public void recordLlm(
            Timer.Sample sample) {

        sample.stop(llmTimer);
    }
}