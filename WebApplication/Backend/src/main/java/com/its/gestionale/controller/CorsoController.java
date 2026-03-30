package com.its.gestionale.controller;

import com.its.gestionale.entity.Corso;
import com.its.gestionale.service.CorsoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/corsi")
@CrossOrigin
public class CorsoController {

    private final CorsoService corsoService;

    public CorsoController(CorsoService corsoService) {
        this.corsoService = corsoService;
    }

    // GET ALL
    @GetMapping
    public List<Corso> getAll() {
        return corsoService.findAll();
    }

    // GET BY ID
    @GetMapping("/{id}")
    public Corso getById(@PathVariable Integer id) {
        return corsoService.findById(id)
                .orElseThrow(() -> new RuntimeException("Corso non trovato"));
    }

    // CREATE
    @PostMapping
    public Corso create(@RequestBody Corso corso) {
        return corsoService.save(corso);
    }

    // UPDATE
    @PutMapping("/{id}")
    public Corso update(@PathVariable Integer id, @RequestBody Corso corso) {
        corso.setId(id);
        return corsoService.save(corso);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        corsoService.delete(id);
    }
    
}
