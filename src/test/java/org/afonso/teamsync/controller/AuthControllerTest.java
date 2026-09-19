package org.afonso.teamsync.controller;

import org.afonso.teamsync.dto.RegisterRequest;
import org.afonso.teamsync.dto.RegisterResponse;
import org.afonso.teamsync.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private org.afonso.teamsync.security.JwtService jwtService;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    void registerShouldReturnCreatedResponse() {
        RegisterRequest request = new RegisterRequest();
        RegisterResponse response = new RegisterResponse("jwt-token");
        when(authService.register(request)).thenReturn(response);

        ResponseEntity<RegisterResponse> result = authController.register(request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getBody()).isSameAs(response);
        verify(authService).register(request);
    }
}
