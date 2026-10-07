package com.vignesh.ai.day52.agent;

import org.springframework.stereotype.Service;

@Service
public class ManagerAgent {

    private final JobAnalystAgent jobAnalystAgent;
    private final SkillGapAgent skillGapAgent;
    private final CareerAdvisorAgent careerAdvisorAgent;

    public ManagerAgent(
            JobAnalystAgent jobAnalystAgent,
            SkillGapAgent skillGapAgent,
            CareerAdvisorAgent careerAdvisorAgent) {

        this.jobAnalystAgent = jobAnalystAgent;
        this.skillGapAgent = skillGapAgent;
        this.careerAdvisorAgent = careerAdvisorAgent;
    }

    public String analyze(String jobDescription, String candidateSkills) {

        // Agent 1
        String jobAnalysis =
                jobAnalystAgent.analyze(jobDescription);

        // Agent 2
        String skillGap =
                skillGapAgent.analyze(jobAnalysis, candidateSkills);

        // Agent 3
        String careerPlan =
                careerAdvisorAgent.createPlan(skillGap);

        return """
                ==============================
                AI JOB ANALYSIS REPORT
                ==============================

                JOB ANALYSIS
                %s

                ==============================
                SKILL GAP ANALYSIS
                ==============================

                %s

                ==============================
                CAREER ACTION PLAN
                ==============================

                %s
                """.formatted(
                jobAnalysis,
                skillGap,
                careerPlan
        );
    }
}