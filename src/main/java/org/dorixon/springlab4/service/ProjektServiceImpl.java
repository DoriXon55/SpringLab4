package org.dorixon.springlab4.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.dorixon.springlab4.model.Projekt;
import org.dorixon.springlab4.model.Student;
import org.dorixon.springlab4.model.Zadanie;
import org.dorixon.springlab4.repository.ProjektRepository;
import org.dorixon.springlab4.repository.StudentRepository;
import org.dorixon.springlab4.repository.ZadanieRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@Service
@AllArgsConstructor
@Transactional
public class ProjektServiceImpl implements ProjektService{
    private ProjektRepository projektRepository;
    private StudentRepository studentRepository;
    private ZadanieRepository zadanieRepository;

    @Override
    public Optional<Projekt> getProjekt(Integer projektId) {
        return projektRepository.findById(projektId);
    }

    @Override
    public Projekt setProjekt(Projekt projekt) {
        // Clear IDs for new entities
        if (projekt.getProjektId() != null && projekt.getProjektId() == 0) {
            projekt.setProjektId(null);
        }

        // Always set creation date
        if (projekt.getDataCzasUtworzenia() == null) {
            projekt.setDataCzasUtworzenia(LocalDateTime.now());
        }

        // Handle student entities
        if (projekt.getStudenci() != null) {
            Set<Student> managedStudents = new HashSet<>();
            for (Student student : projekt.getStudenci()) {
                // Clear ID if it's 0
                if (student.getStudentId() != null && student.getStudentId() == 0) {
                    student.setStudentId(null);
                }

                Optional<Student> existingStudent = studentRepository.findByNrIndeksu(student.getNrIndeksu());
                if (existingStudent.isPresent()) {
                    managedStudents.add(existingStudent.get());
                } else {
                    managedStudents.add(student);
                }
            }
            projekt.setStudenci(managedStudents);
        }

        // Handle zadania entities
        if (projekt.getZadania() != null) {
            for (Zadanie zadanie : projekt.getZadania()) {
                if (zadanie.getZadanieId() != null && zadanie.getZadanieId() == 0) {
                    zadanie.setZadanieId(null);
                }
                zadanie.setProjekt(projekt);
            }
        }

        return projektRepository.save(projekt);
    }

    @Override
    public void deleteProjekt(Integer projektId) {
        for (Zadanie zadanie : zadanieRepository.findZadaniaProjektu(projektId)) {
            zadanieRepository.delete(zadanie);
        }
        projektRepository.deleteById(projektId);
    }

    @Override
    public Page<Projekt> getProjekty(Pageable pageable) {
        return projektRepository.findAll(pageable);
    }

    @Override
    public Page<Projekt> searchByNazwa(String nazwa, Pageable pageable) {
        return projektRepository.findByNazwaContainingIgnoreCase(nazwa, pageable);
    }
}
