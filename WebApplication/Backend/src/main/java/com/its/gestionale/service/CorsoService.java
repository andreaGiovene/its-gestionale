package com.its.gestionale.service;

import com.its.gestionale.entity.Corso;
import com.its.gestionale.repository.CorsoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CorsoService {

    private final CorsoRepository corsoRepository;

    public CorsoService(CorsoRepository corsoRepository) {
        this.corsoRepository = corsoRepository;
    }

    public List<Corso> findAll() {
        return corsoRepository.findAll();
    }

    public Optional<Corso> findById(Integer id) {
        return corsoRepository.findById(id);
    }

    public Corso save(Corso corso) {
        return corsoRepository.save(corso);
    }

    public void delete(Integer id) {
        corsoRepository.deleteById(id);
    }
    
}
