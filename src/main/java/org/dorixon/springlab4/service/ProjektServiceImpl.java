package org.dorixon.springlab4.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.dorixon.springlab4.model.Projekt;
import org.dorixon.springlab4.model.Zadanie;
import org.dorixon.springlab4.repository.ProjektRepository;
import org.dorixon.springlab4.repository.ZadanieRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@AllArgsConstructor
@Transactional
public class ProjektServiceImpl implements ProjektService{
    private ProjektRepository projektRepository;
    private ZadanieRepository zadanieRepository;

    @Override
    public Optional<Projekt> getProjekt(Integer projektId) {
        return projektRepository.findById(projektId);
    }

    @Override
    public Projekt setProjekt(Projekt projekt) {
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
