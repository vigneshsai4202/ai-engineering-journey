package com.vignesh.ai.day53.service;

import com.vignesh.ai.day53.agent.*;
import org.springframework.stereotype.Service;

@Service
public class SupportRouter {

    private final DecisionAgent decisionAgent;
    private final TechnicalSupportAgent technicalSupportAgent;
    private final BillingSupportAgent billingSupportAgent;
    private final GeneralSupportAgent generalSupportAgent;

    public SupportRouter(
            DecisionAgent decisionAgent,
            TechnicalSupportAgent technicalSupportAgent,
            BillingSupportAgent billingSupportAgent,
            GeneralSupportAgent generalSupportAgent) {

        this.decisionAgent = decisionAgent;
        this.technicalSupportAgent = technicalSupportAgent;
        this.billingSupportAgent = billingSupportAgent;
        this.generalSupportAgent = generalSupportAgent;
    }

    public String route(String question) {

        String decision = decisionAgent.decide(question);

        return switch (decision) {

            case "TECHNICAL" ->
                    technicalSupportAgent.respond(question);

            case "BILLING" ->
                    billingSupportAgent.respond(question);

            case "GENERAL" ->
                    generalSupportAgent.respond(question);

            default ->
                    "Sorry, I couldn't determine the appropriate support category.";
        };
    }
}