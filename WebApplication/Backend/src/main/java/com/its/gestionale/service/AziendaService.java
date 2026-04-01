package com.its.gestionale.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.its.gestionale.dto.AziendaDTO;
import com.its.gestionale.entity.Azienda;
import com.its.gestionale.exception.AziendaNotFoundException;
import com.its.gestionale.repository.AziendaRepository;

@Service
public class AziendaService {

    private final AziendaRepository aziendaRepository;

    public AziendaService(AziendaRepository aziendaRepository) {
        this.aziendaRepository = aziendaRepository;
    }

    @Transactional(readOnly = true)
    public List<AziendaDTO> findAll() {
        List<Azienda> aziende = aziendaRepository.findAll();
        List<AziendaDTO> result = new ArrayList<>();

        for (Azienda azienda : aziende) {
            AziendaDTO dto = toDto(azienda);
            result.add(dto);
        }

        return result;
    }

    @Transactional(readOnly = true)
    public AziendaDTO findById(Integer id) {
        Azienda azienda = aziendaRepository.findById(id)
                .orElseThrow(() -> new AziendaNotFoundException(id));
        return toDto(azienda);
    }

    public AziendaDTO create(AziendaDTO dto) {
        Azienda azienda = toEntity(dto);
        Azienda salvata = aziendaRepository.save(azienda);
        return toDto(salvata);
    }

    public AziendaDTO update(Integer id, AziendaDTO dto) {
        Azienda esistente = aziendaRepository.findById(id)
                .orElseThrow(() -> new AziendaNotFoundException(id));

        esistente.setRagioneSociale(dto.getRagioneSociale());
        esistente.setPartitaIva(dto.getPartitaIva());
        esistente.setTelefono(dto.getTelefono());
        esistente.setEmail(dto.getEmail());
        esistente.setIndirizzo(dto.getIndirizzo());
        esistente.setCap(dto.getCap());

        Azienda aggiornata = aziendaRepository.save(esistente);
        return toDto(aggiornata);
    }

    public void deleteById(Integer id) {
        if (!aziendaRepository.existsById(id)) {
            throw new AziendaNotFoundException(id);
        }
        aziendaRepository.deleteById(id);
    }

    private AziendaDTO toDto(Azienda azienda) {
        AziendaDTO dto = new AziendaDTO();
        dto.setId(azienda.getId());
        dto.setRagioneSociale(azienda.getRagioneSociale());
        dto.setPartitaIva(azienda.getPartitaIva());
        dto.setTelefono(azienda.getTelefono());
        dto.setEmail(azienda.getEmail());
        dto.setIndirizzo(azienda.getIndirizzo());
        dto.setCap(azienda.getCap());
        return dto;
    }

    private Azienda toEntity(AziendaDTO dto) {
        Azienda azienda = new Azienda();
        azienda.setRagioneSociale(dto.getRagioneSociale());
        azienda.setPartitaIva(dto.getPartitaIva());
        azienda.setTelefono(dto.getTelefono());
        azienda.setEmail(dto.getEmail());
        azienda.setIndirizzo(dto.getIndirizzo());
        azienda.setCap(dto.getCap());
        return azienda;
    }
}
