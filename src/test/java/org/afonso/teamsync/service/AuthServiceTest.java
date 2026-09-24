package org.afonso.teamsync.service;

import org.afonso.teamsync.dto.RegisterRequest;
import org.afonso.teamsync.dto.RegisterResponse;
import org.afonso.teamsync.entity.Manager;
import org.afonso.teamsync.entity.Team;
import org.afonso.teamsync.repository.ManagerRepository;
import org.afonso.teamsync.repository.TeamRepository;
import org.afonso.teamsync.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private TeamRepository teamRepo;

    @Mock
    private ManagerRepository managerRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private AuthService authService;

    @Test
    void registerShouldCreateTeamManagerAndToken() {
        RegisterRequest request = request("Ana Silva", "ana@example.com", "plain-password", "Team One");
        when(managerRepo.existsByEmail("ana@example.com")).thenReturn(false);
        when(teamRepo.save(any(Team.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(managerRepo.save(any(Manager.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(passwordEncoder.encode("plain-password")).thenReturn("encoded-password");
        RegisterResponse response = authService.register(request);

        ArgumentCaptor<Team> teamCaptor = ArgumentCaptor.forClass(Team.class);
        ArgumentCaptor<Manager> managerCaptor = ArgumentCaptor.forClass(Manager.class);
        verify(teamRepo).save(teamCaptor.capture());
        verify(managerRepo).save(managerCaptor.capture());

        Team savedTeam = teamCaptor.getValue();
        Manager savedManager = managerCaptor.getValue();
        assertThat(savedTeam.getName()).isEqualTo("Team One");
        assertThat(savedManager.getName()).isEqualTo("Ana Silva");
        assertThat(savedManager.getEmail()).isEqualTo("ana@example.com");
        assertThat(savedManager.getPasswordHash()).isEqualTo("encoded-password");
        assertThat(savedManager.getTeam()).isSameAs(savedTeam);
        assertThat(response.getToken()).isEqualTo("Check your email to verify your account.");
        verify(passwordEncoder).encode("plain-password");
        verify(emailService).sendVerificationEmail(
                "ana@example.com", "Ana Silva", savedManager.getVerificationToken());
    }

    @Test
    void registerShouldRejectDuplicateEmailBeforeCreatingAnyEntities() {
        RegisterRequest request = request("Ana Silva", "existing@example.com", "plain-password", "Team One");
        when(managerRepo.existsByEmail("existing@example.com")).thenReturn(true);

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Email already in use");

        verify(teamRepo, never()).save(any(Team.class));
        verify(managerRepo, never()).save(any(Manager.class));
        verifyNoInteractions(passwordEncoder, jwtService);
    }

    @Test
    void registerShouldRejectNullRequestBeforeCallingDependencies() {
        assertThatThrownBy(() -> authService.register(null))
                .isInstanceOf(NullPointerException.class);

        verifyNoInteractions(teamRepo, managerRepo, passwordEncoder, jwtService);
    }

    @Test
    void registerShouldStopWhenTeamCannotBeSaved() {
        RegisterRequest request = request("Ana Silva", "ana@example.com", "plain-password", "Team One");
        RuntimeException persistenceFailure = new RuntimeException("team persistence failed");
        when(managerRepo.existsByEmail("ana@example.com")).thenReturn(false);
        when(teamRepo.save(any(Team.class))).thenThrow(persistenceFailure);

        assertThatThrownBy(() -> authService.register(request))
                .isSameAs(persistenceFailure);

        verify(teamRepo).save(any(Team.class));
        verify(managerRepo, never()).save(any(Manager.class));
        verifyNoInteractions(passwordEncoder, jwtService);
    }

    @Test
    void registerShouldStopBeforeSavingManagerWhenPasswordEncodingFails() {
        RegisterRequest request = request("Ana Silva", "ana@example.com", "plain-password", "Team One");
        RuntimeException encodingFailure = new RuntimeException("password encoding failed");
        when(managerRepo.existsByEmail("ana@example.com")).thenReturn(false);
        when(teamRepo.save(any(Team.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(passwordEncoder.encode("plain-password")).thenThrow(encodingFailure);

        assertThatThrownBy(() -> authService.register(request))
                .isSameAs(encodingFailure);

        verify(teamRepo).save(any(Team.class));
        verify(passwordEncoder).encode("plain-password");
        verify(managerRepo, never()).save(any(Manager.class));
        verifyNoInteractions(jwtService);
    }

    @Test
    void registerShouldPassNullEmailToDuplicateCheckAndPreserveRequestValues() {
        RegisterRequest request = request("Ana Silva", null, "plain-password", "Team One");
        when(managerRepo.existsByEmail(null)).thenReturn(false);
        when(teamRepo.save(any(Team.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(managerRepo.save(any(Manager.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(passwordEncoder.encode("plain-password")).thenReturn("encoded-password");
        when(jwtService.generateToken(any(Manager.class))).thenReturn("jwt-token");

        RegisterResponse response = authService.register(request);

        ArgumentCaptor<Manager> managerCaptor = ArgumentCaptor.forClass(Manager.class);
        verify(managerRepo).existsByEmail(null);
        verify(managerRepo).save(managerCaptor.capture());
        assertThat(managerCaptor.getValue().getEmail()).isNull();
        assertThat(response.getToken()).isEqualTo("Check your email to verify your account.");
    }

    private RegisterRequest request(String managerName, String email, String password, String teamName) {
        RegisterRequest request = new RegisterRequest();
        request.setManagerName(managerName);
        request.setEmail(email);
        request.setPassword(password);
        request.setTeamName(teamName);
        return request;
    }
}
