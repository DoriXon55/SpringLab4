package org.dorixon.springlab4.controller;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.dorixon.springlab4.model.Projekt;
import org.dorixon.springlab4.service.ProjektService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import io.swagger.v3.oas.annotations.tags.Tag;


import java.net.URI;


@RestController
@RequestMapping("/api")
@Tag(name = "Projekt")

@AllArgsConstructor
public class ProjektRestController {


    private ProjektService projektService;

    @GetMapping("/projekty/{projektId}")
    public ResponseEntity<Projekt> getProjekt(@PathVariable("projektId") Integer projektId)
    {
        return ResponseEntity.of(projektService.getProjekt(projektId));
    }

    @PostMapping(path = "/projekty")
    public ResponseEntity<Void> createProjekt(@Valid @RequestBody Projekt projekt) {
        // Clear IDs to force creation of new entities
        projekt.setProjektId(null);

        if (projekt.getStudenci() != null) {
            projekt.getStudenci().forEach(s -> {
                s.setStudentId(null); // Use correct field name after updating Student.java
            });
        }

        if (projekt.getZadania() != null) {
            projekt.getZadania().forEach(z -> {
                z.setZadanieId(null);
                z.setProjekt(projekt);
            });
        }

        Projekt createdProjekt = projektService.setProjekt(projekt);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{projektId}")
                .buildAndExpand(createdProjekt.getProjektId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/projekty/{projektyId}")
    public ResponseEntity<Void> updateProjekt(@Valid @RequestBody Projekt projekt, @PathVariable("projektyId") Integer projektyId)
    {
        return projektService.getProjekt(projektyId).map(
                p -> {
                    projektService.setProjekt(projekt);
                    return new ResponseEntity<Void>(HttpStatus.OK);
                }
        ).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/projekty/{projektId}")
    public ResponseEntity<Void> deleteProjekt(@PathVariable("projektId") Integer projektId)
    {
        return projektService.getProjekt(projektId).map(
                p -> {
                    projektService.deleteProjekt(projektId);
                    return new ResponseEntity<Void>(HttpStatus.OK);
                }
        ).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/projekty")
    public Page<Projekt> getProjekty(Pageable pageable)
    {
        return projektService.getProjekty(pageable);
    }

    @GetMapping(value = "/projekty", params = "nazwa")
    Page<Projekt> getProjektByNazwa(@RequestParam(name="nazwa") String nazwa, Pageable pageable)
    {
        return projektService.searchByNazwa(nazwa, pageable);
    }


}
