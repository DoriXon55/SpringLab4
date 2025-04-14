package org.dorixon.springlab4.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@Entity
@Table(name="projekt")
@AllArgsConstructor
@NoArgsConstructor
public class Projekt {


    public Projekt(Integer projektId, String nazwa, String opis, LocalDateTime dataCzasUtworzenia, LocalDate dataOddania)
    {
        this.projektId = projektId;
        this.nazwa = nazwa;
        this.opis = opis;
        this.dataczas_utworzenia = dataCzasUtworzenia;
        this.data_oddania = dataOddania.atStartOfDay();
    }


    @Id
    @GeneratedValue
    @Column(name="projekt_id")
    private Integer projektId;

    @Column(nullable = false, length = 50)
    private String nazwa;

    @Column(nullable = false, length = 1000)
    private String opis;

    @CreatedDate
    @Column(name="dataczas_utworzenia", nullable = false, updatable = false)
    private LocalDateTime dataczas_utworzenia = LocalDateTime.now();


    @LastModifiedDate
    @Column(name="dataczas_modyfikacji", insertable = false)
    private LocalDateTime data_oddania;


    @OneToMany(mappedBy = "projekt")
    @JsonIgnoreProperties({"projekt"})
    private List<Zadanie> zadania;

    @ManyToMany
    @JoinTable(name="projekt_student",
    joinColumns = {@JoinColumn(name="projekt_id")},
    inverseJoinColumns = {@JoinColumn(name="student_id")})
    @JsonIgnoreProperties("projekty")
    private Set<Student> studenci;





}
