package org.afonso.teamsync.service;

import org.afonso.exceptions.ResourceNotFoundException;
import org.afonso.teamsync.dto.AthleteRequest;
import org.afonso.teamsync.entity.Athlete;
import org.afonso.teamsync.entity.Team;
import org.afonso.teamsync.repository.AthleteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AthleteServiceTest {

    @Mock
    private AthleteRepository athleteRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AthleteService athleteService;

    @Test
    void createShouldMapRequestEncodePasswordAndAssignTeam() {
        UUID teamId = UUID.randomUUID();
        AthleteRequest request = new AthleteRequest();
        request.setName("Ana Silva");
        request.setBirthDay(LocalDate.of(2000, 5, 12));
        request.setEmail("ana@example.com");
        request.setPassword("plain-password");
        request.setPhone("910000000");
        request.setLicense("LIC-123");
        request.setNationality("Portugal");

        when(passwordEncoder.encode("plain-password")).thenReturn("encoded-password");
        when(athleteRepo.save(any(Athlete.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Athlete result = athleteService.create(request, teamId);

        ArgumentCaptor<Athlete> athleteCaptor = ArgumentCaptor.forClass(Athlete.class);
        verify(athleteRepo).save(athleteCaptor.capture());
        Athlete savedAthlete = athleteCaptor.getValue();

        assertThat(result).isSameAs(savedAthlete);
        assertThat(savedAthlete.getName()).isEqualTo("Ana Silva");
        assertThat(savedAthlete.getBirthDay()).isEqualTo(LocalDate.of(2000, 5, 12));
        assertThat(savedAthlete.getEmail()).isEqualTo("ana@example.com");
        assertThat(savedAthlete.getPasswordHash()).isEqualTo("encoded-password");
        assertThat(savedAthlete.getPhone()).isEqualTo("910000000");
        assertThat(savedAthlete.getLicense()).isEqualTo("LIC-123");
        assertThat(savedAthlete.getNationality()).isEqualTo("Portugal");
        assertThat(savedAthlete.getTeam().getId()).isEqualTo(teamId);
    }

    @Test
    void getByIdShouldThrowWhenAthleteDoesNotBelongToTeam() {
        UUID athleteId = UUID.randomUUID();
        UUID teamId = UUID.randomUUID();
        when(athleteRepo.findByIdAndTeam_Id(athleteId, teamId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> athleteService.getById(athleteId, teamId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Athlete not found or does not belong to your team");
    }

    @Test
    void getAllFromTeamShouldReturnOnlyAthletesFromRequestedTeam() {
        UUID teamId = UUID.randomUUID();
        List<Athlete> athletes = List.of(new Athlete(), new Athlete());
        when(athleteRepo.findAllByTeam_Id(teamId)).thenReturn(athletes);

        List<Athlete> result = athleteService.getAllFromTeam(teamId);

        assertThat(result).isSameAs(athletes);
        verify(athleteRepo).findAllByTeam_Id(teamId);
    }

    @Test
    void getByIdShouldReturnAthleteWhenItBelongsToTeam() {
        UUID athleteId = UUID.randomUUID();
        UUID teamId = UUID.randomUUID();
        Athlete athlete = new Athlete();
        athlete.setId(athleteId);
        when(athleteRepo.findByIdAndTeam_Id(athleteId, teamId)).thenReturn(Optional.of(athlete));

        Athlete result = athleteService.getById(athleteId, teamId);

        assertThat(result).isSameAs(athlete);
        verify(athleteRepo).findByIdAndTeam_Id(athleteId, teamId);
    }

    @Test
    void updateShouldAllowOptionalFieldsToBeNullWhenRequestOmitsThem() {
        UUID athleteId = UUID.randomUUID();
        UUID teamId = UUID.randomUUID();
        Athlete athlete = existingAthlete(athleteId, teamId);
        AthleteRequest request = new AthleteRequest();
        request.setName("Updated name");

        when(athleteRepo.findByIdAndTeam_Id(athleteId, teamId)).thenReturn(Optional.of(athlete));
        when(athleteRepo.save(athlete)).thenReturn(athlete);

        Athlete result = athleteService.update(athleteId, request, teamId);

        assertThat(result).isSameAs(athlete);
        assertThat(athlete.getName()).isEqualTo("Updated name");
        assertThat(athlete.getBirthDay()).isNull();
        assertThat(athlete.getPhone()).isNull();
        assertThat(athlete.getLicense()).isNull();
        assertThat(athlete.getNationality()).isNull();
        assertThat(athlete.getEmail()).isEqualTo("old@example.com");
        assertThat(athlete.getPasswordHash()).isEqualTo("encoded-old-password");
        verify(passwordEncoder, never()).encode(any());
        verify(athleteRepo).save(athlete);
    }

    @Test
    void updateShouldChangeAllProvidedValuesAndEncodeNewPassword() {
        UUID athleteId = UUID.randomUUID();
        UUID teamId = UUID.randomUUID();
        Athlete athlete = existingAthlete(athleteId, teamId);
        AthleteRequest request = new AthleteRequest();
        request.setName("New name");
        request.setBirthDay(LocalDate.of(2001, 2, 3));
        request.setEmail("new@example.com");
        request.setPassword("new-password");
        request.setPhone("920000000");
        request.setLicense("LIC-999");
        request.setNationality("Spain");

        when(athleteRepo.findByIdAndTeam_Id(athleteId, teamId)).thenReturn(Optional.of(athlete));
        when(passwordEncoder.encode("new-password")).thenReturn("encoded-new-password");
        when(athleteRepo.save(athlete)).thenReturn(athlete);

        athleteService.update(athleteId, request, teamId);

        assertThat(athlete.getName()).isEqualTo("New name");
        assertThat(athlete.getBirthDay()).isEqualTo(LocalDate.of(2001, 2, 3));
        assertThat(athlete.getEmail()).isEqualTo("new@example.com");
        assertThat(athlete.getPasswordHash()).isEqualTo("encoded-new-password");
        assertThat(athlete.getPhone()).isEqualTo("920000000");
        assertThat(athlete.getLicense()).isEqualTo("LIC-999");
        assertThat(athlete.getNationality()).isEqualTo("Spain");
        verify(passwordEncoder).encode("new-password");
        verify(athleteRepo).save(athlete);
    }

    @Test
    void updateShouldThrowAndNotSaveWhenAthleteDoesNotBelongToTeam() {
        UUID athleteId = UUID.randomUUID();
        UUID teamId = UUID.randomUUID();
        when(athleteRepo.findByIdAndTeam_Id(athleteId, teamId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> athleteService.update(athleteId, new AthleteRequest(), teamId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Athlete not found or does not belong to your team");

        verify(athleteRepo, never()).save(any(Athlete.class));
    }

    @Test
    void deleteShouldDeleteAthleteFromRequestedTeam() {
        UUID athleteId = UUID.randomUUID();
        UUID teamId = UUID.randomUUID();
        Athlete athlete = existingAthlete(athleteId, teamId);
        when(athleteRepo.findByIdAndTeam_Id(athleteId, teamId)).thenReturn(Optional.of(athlete));

        athleteService.delete(athleteId, teamId);

        verify(athleteRepo).delete(athlete);
    }

    @Test
    void deleteShouldThrowAndNotDeleteWhenAthleteDoesNotBelongToTeam() {
        UUID athleteId = UUID.randomUUID();
        UUID teamId = UUID.randomUUID();
        when(athleteRepo.findByIdAndTeam_Id(athleteId, teamId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> athleteService.delete(athleteId, teamId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Athlete not found or does not belong to your team");

        verify(athleteRepo, never()).delete(any(Athlete.class));
    }

    private Athlete existingAthlete(UUID athleteId, UUID teamId) {
        Athlete athlete = new Athlete();
        athlete.setId(athleteId);
        athlete.setName("Old name");
        athlete.setBirthDay(LocalDate.of(1999, 1, 1));
        athlete.setEmail("old@example.com");
        athlete.setPasswordHash("encoded-old-password");
        athlete.setPhone("910000000");
        athlete.setLicense("LIC-123");
        athlete.setNationality("Portugal");

        Team team = new Team();
        team.setId(teamId);
        athlete.setTeam(team);
        return athlete;
    }
}
