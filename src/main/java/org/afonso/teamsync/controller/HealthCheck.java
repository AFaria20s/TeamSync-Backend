package org.afonso.teamsync.controller;

import lombok.RequiredArgsConstructor;
import org.afonso.teamsync.security.AuthUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/health")
@RequiredArgsConstructor
public class HealthCheck {
    private final AuthUtils authUtils;
    @GetMapping
    public String healthCheck() {
        return "Working!";
    }
}
