package com.vignesh.ai.day39_spring_ai_query_rewriting.conroller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vignesh.ai.day39_spring_ai_query_rewriting.service.QueryRewriter;

@RestController
public class QueryController {

    private final QueryRewriter queryRewriter;

    public QueryController(QueryRewriter queryRewriter) {
        this.queryRewriter = queryRewriter;
    }

    @GetMapping("/rewrite")
    public String rewrite(
            @RequestParam String question,
            @RequestParam(defaultValue = "user1") String conversationId) {

        return queryRewriter.rewrite(question, conversationId);
    }
    
}