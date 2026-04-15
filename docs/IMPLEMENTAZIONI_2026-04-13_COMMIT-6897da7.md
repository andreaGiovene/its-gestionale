# Implementazioni 2026-04-13 - Commit 6897da7

## Riferimento commit
- Hash: 6897da74cc9ab9df6eb9329939f99af13d9236b5
- Data/Ora: Mon Apr 13 12:40:53 2026 (+0200)
- Autore: andreaG-student-its24
- Messaggio: Refactor not-found exceptions and document services

## Obiettivo del cambiamento
Uniformare la gestione dei casi "non trovato" per Allievo e Utente, eliminando la logica HTTP hardcoded nei service e centralizzando la traduzione in risposta REST nel global handler.

## Impatto complessivo
- File modificati: 5
- Inserimenti: 68
- Rimozioni: 20
- Ambito: Backend (service + exception handling)

## Modifiche introdotte

### 1) Nuove eccezioni di dominio dedicate
Sono state introdotte due RuntimeException specifiche:
- AllievoNotFoundException
- UtenteNotFoundException

Dettagli:
- AllievoNotFoundException accetta id e costruisce messaggio contestualizzato.
- UtenteNotFoundException supporta due costruttori:
  - per id
  - per username

Beneficio:
- semantica di dominio più chiara
- migliore riuso della logica di errore
- riduzione della duplicazione dei messaggi nei service

### 2) Estensione del GlobalExceptionHandler
Nel gestore globale sono stati aggiunti due handler dedicati:
- handleAllievoNotFound
- handleUtenteNotFound

Comportamento:
- intercettano rispettivamente AllievoNotFoundException e UtenteNotFoundException
- restituiscono HTTP 404 con payload ApiErrorResponse coerente con lo standard applicativo

Beneficio:
- risposta errore uniforme su tutto il backend
- separazione pulita tra logica applicativa e protocollo HTTP

### 3) Refactor di AllievoService
Nei metodi principali di ricerca/aggiornamento/eliminazione è stata sostituita la creazione manuale di ResponseStatusException(404) con il lancio di AllievoNotFoundException.

Metodi coinvolti:
- findById
- update
- deleteById

Beneficio:
- service più focalizzato sul dominio
- codice più leggibile e meno ripetitivo

### 4) Refactor di UtenteService
Nel service utente è stato applicato lo stesso pattern, sostituendo i 404 manuali con UtenteNotFoundException.

Metodi coinvolti:
- update
- disattivaUtente

Beneficio:
- coerenza con AllievoService
- allineamento alla gestione centralizzata delle eccezioni

## Comportamento prima vs dopo

Prima:
- i service lanciavano direttamente ResponseStatusException con HttpStatus.NOT_FOUND
- gestione dell'errore distribuita e duplicata in più punti

Dopo:
- i service lanciano eccezioni di dominio specifiche
- il GlobalExceptionHandler converte centralmente in HTTP 404
- payload di errore più standardizzato e manutenzione semplificata

## File toccati
- WebApplication/Backend/src/main/java/com/its/gestionale/exception/AllievoNotFoundException.java
- WebApplication/Backend/src/main/java/com/its/gestionale/exception/GlobalExceptionHandler.java
- WebApplication/Backend/src/main/java/com/its/gestionale/exception/UtenteNotFoundException.java
- WebApplication/Backend/src/main/java/com/its/gestionale/service/AllievoService.java
- WebApplication/Backend/src/main/java/com/its/gestionale/service/UtenteService.java

## Valutazione tecnica
Il commit migliora la qualità architetturale perché:
- riduce l'accoppiamento dei service con il layer HTTP
- introduce eccezioni tipizzate più espressive
- prepara il terreno per estendere facilmente la gestione errori (ad esempio logging strutturato, codici applicativi, mapping uniforme per nuovi domini)

## Possibili follow-up
- aggiungere test unitari sui nuovi handler di eccezione
- verificare copertura test sui flussi not-found nei service coinvolti
- valutare migrazione dello stesso pattern anche sugli altri service ancora basati su ResponseStatusException per i 404
