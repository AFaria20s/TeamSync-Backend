package org.afonso.teamsync.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "address_id", nullable = false)
    private UUID id;

    @Size(max = 200)
    @Column(name = "street", length = 200)
    private String street;

    @Size(max = 100)
    @Column(name = "city", length = 100)
    private String city;

    @Size(max = 100)
    @Column(name = "district", length = 100)
    private String district;

    @Size(max = 20)
    @Column(name = "postal_code", length = 20)
    private String postalCode;

    @Size(max = 100)
    @ColumnDefault("'Portugal'")
    @Column(name = "country", length = 100)
    private String country;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;


}