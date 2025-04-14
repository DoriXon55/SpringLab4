package org.dorixon.springlab4.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@Entity
@Table(name="projekt")
@NoArgsConstructor
public class Projekt {
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
