package org.afonso.teamsync.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "team")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "team_id", nullable = false)
    private UUID id;

    @Size(max = 200)
    @NotNull
    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Size(max = 20)
    @Column(name = "acronym", length = 20)
    private String acronym;

    @Size(max = 20)
    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "founded_year")
    private Integer foundedYear;

    @Size(max = 100)
    @Column(name = "license", length = 100)
    private String license;

    @Size(max = 200)
    @Column(name = "location", length = 200)
    private String location;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
    }
}