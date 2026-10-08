package com.vignesh.ai.day53.controller;

import com.vignesh.ai.day53.service.SupportRouter;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/support")
public class SupportController {

    private final SupportRouter supportRouter;

    public SupportController(SupportRouter supportRouter) {
        this.supportRouter = supportRouter;
    }

    @GetMapping("/ask")
    public String ask(@RequestParam String question) {
        return supportRouter.route(question);
    }
}