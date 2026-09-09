package org.afonso.teamsync.controller;

import lombok.RequiredArgsConstructor;
import org.afonso.teamsync.dto.LoginRequest;
import org.afonso.teamsync.dto.LoginResponse;
import org.afonso.teamsync.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        // check email and password
        // launch exception if invalid
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // load user and generate token
        UserDetails user = userDetailsService.loadUserByUsername(request.getEmail());
        String token = jwtService.generateToken(user);

        return new LoginResponse(token);
    }
}