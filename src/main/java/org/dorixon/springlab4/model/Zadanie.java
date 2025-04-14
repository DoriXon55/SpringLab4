package org.dorixon.springlab4.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name="zadanie")
public class Zadanie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="zadanie_id")
    private Integer zadanieId;

    @ManyToOne
    @JoinColumn(name="projekt_id")
    @JsonIgnoreProperties({"zadania"})
    private Projekt projekt;

    @Column(nullable = false, length = 50)
    private String nazwa;

    @Column(nullable = false)
    private Integer kolejnosc;

    @Column(nullable = false, length = 1000)
    private String opis;

    @CreatedDate
    @Column(name="dataczas_dodania", nullable = false, updatable = false)
    private LocalDateTime dataczasDodania; // Make sure column name matches field purpose


}
