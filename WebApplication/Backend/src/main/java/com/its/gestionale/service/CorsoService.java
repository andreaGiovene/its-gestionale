package com.its.gestionale.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.its.gestionale.entity.Corso;
import com.its.gestionale.repository.CorsoRepository;


@Service
public class CorsoService {

    // DEPENDENCY INJECTION — Spring crea il Repository e ce lo "inietta"
    // Non dobbiamo fare "new CorsoRepository()" manualmente
    private final CorsoRepository corsoRepository;

    // Costruttore — Spring vede un solo costruttore e capisce
    // automaticamente che deve iniettare il Repository
    public CorsoService(CorsoRepository corsoRepository) {
        this.corsoRepository = corsoRepository;
    }

    // Restituisce tutti i corsi
    public List<Corso> findAll() {
        return corsoRepository.findAll();
    }

     // Cerca un corso per ID
    // Optional<Corso> = "potrebbe esserci o no" (evita NullPointerException)
    public Optional<Corso> findById(Integer id) {
        return corsoRepository.findById(id);
    }

    // Crea un nuovo corso o aggiorna uno esistente
    public Corso save(Corso corso) {
        return corsoRepository.save(corso);
    }

    // Elimina un corso per ID
    public void deleteById(Integer id) {
        corsoRepository.deleteById(id);
    }
    
   // Cerca corsi per stato
    public List<Corso> findByStato(String stato) {
        return corsoRepository.findByStato(stato);
    }

}
