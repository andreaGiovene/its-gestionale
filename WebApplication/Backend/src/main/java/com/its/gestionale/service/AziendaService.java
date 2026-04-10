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

    /**
     * Restituisce tutte le aziende convertendo le entity persistite in DTO.
     *
     * La conversione avviene qui, nel service, per evitare di esporre direttamente
     * il modello JPA ai layer superiori e mantenere separata la logica di persistenza
     * da quella di rappresentazione.
     */
    @Transactional(readOnly = true)
    public List<AziendaDTO> findAll() {
        List<Azienda> aziende = aziendaRepository.findAll();
        List<AziendaDTO> result = new ArrayList<>();

        // Il mapping esplicito rende chiari i campi trasferiti e impedisce di
        // propagare accidentalmente attributi non esposti dall'API.
        for (Azienda azienda : aziende) {
            result.add(AziendaDTO.fromEntity(azienda));
        }

        return result;
    }

    /**
     * Recupera una singola azienda per ID.
     *
     * Se il record non esiste, il servizio traduce l'assenza in una eccezione
     * di dominio specifica, così il controller può restituire una risposta HTTP
     * coerente senza conoscere i dettagli del repository.
     */
    @Transactional(readOnly = true)
    public AziendaDTO findById(Integer id) {
    return aziendaRepository.findById(id)
            .map(AziendaDTO::fromEntity)
            .orElseThrow(() -> new AziendaNotFoundException(id));
    }

    /**
     * Crea una nuova azienda partendo dai dati in ingresso.
     *
     * Prima si converte il DTO in entity, poi si usa save() per ottenere il record
     * persistito con eventuali valori generati dal database. Il risultato viene
     * riconvertito in DTO per mantenere l'output uniforme rispetto agli altri metodi.
     */
    public AziendaDTO create(AziendaDTO dto) {
        Azienda azienda = toEntity(dto);
        Azienda salvata = aziendaRepository.save(azienda);

        return AziendaDTO.fromEntity(salvata);
    }

    /**
     * Aggiorna una azienda esistente sovrascrivendo i campi modificabili.
     *
     * Il caricamento iniziale serve a verificare l'esistenza del record e a lavorare
     * su un'entity gestita da JPA; in questo modo il repository può applicare correttamente
     * le modifiche al momento del save().
     */
    public AziendaDTO update(Integer id, AziendaDTO dto) {
        Azienda esistente = aziendaRepository.findById(id)
                .orElseThrow(() -> new AziendaNotFoundException(id));

        // Aggiorniamo solo i campi consentiti, lasciando invariato l'identificativo.
        esistente.setRagioneSociale(dto.getRagioneSociale());
        esistente.setPartitaIva(dto.getPartitaIva());
        esistente.setTelefono(dto.getTelefono());
        esistente.setEmail(dto.getEmail());
        esistente.setIndirizzo(dto.getIndirizzo());
        esistente.setCap(dto.getCap());

        Azienda aggiornata = aziendaRepository.save(esistente);

        return AziendaDTO.fromEntity(aggiornata);
    }

    /**
     * Elimina una azienda dopo aver verificato che esista davvero.
     *
     * La verifica preventiva evita di eseguire una delete silenziosa su un ID inesistente
     * e consente di restituire un errore esplicito e consistente.
     */
    public void deleteById(Integer id) {
        if (!aziendaRepository.existsById(id)) {
            throw new AziendaNotFoundException(id);
        }

        aziendaRepository.deleteById(id);
    }

    /**
     * Converte un DTO in entity JPA.
     *
     * Questo metodo centralizza il mapping in ingresso, così create() e altri eventuali
     * punti di costruzione dell'entity restano allineati e non duplicano la stessa logica.
     */
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