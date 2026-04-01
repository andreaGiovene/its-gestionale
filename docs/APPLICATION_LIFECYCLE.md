# Application Lifecycle - ITS Gestionale Tirocini

## 1. Executive summary

L'applicazione supporta l'intero processo di gestione ITS su corsi, allievi e tirocini tramite architettura web full stack.

Obiettivi principali:
- centralizzare dati e processi didattici
- ridurre attrito operativo tra funzioni interne
- mantenere time-to-change basso durante sviluppo

## 2. Visione architetturale

Componenti core:
- Frontend Angular: presentazione, navigazione, invocazione API
- Backend Spring Boot: dominio, orchestrazione, persistenza
- PostgreSQL: persistenza relazionale
- Docker Compose: provisioning locale database

Pattern adottati:
- layered architecture (controller, service, repository)
- DTO boundary per API
- mapping JPA entita-relazioni

## 3. Lifecycle per fase

### 3.1 Ideazione e requisiti

Input tipici:
- needs business da area didattica e job placement
- issue funzionali e tecniche in docs/issues
- vincoli normativi e operativi

Output:
- backlog priorizzato
- definizione macro-funzionalita
- criteri di accettazione

### 3.2 Progettazione tecnica

Attivita:
- modellazione entita e relazioni
- definizione endpoint REST e contratti DTO
- strategia configurazione ambienti (dev/prod)

Output:
- schema dati aggiornato
- API contracts
- piano di migrazione dati se necessario

### 3.3 Sviluppo backend

Flusso:
1. aggiornamento entity/repository/service/controller
2. aggiornamento configurazioni runtime
3. compilazione locale e smoke test endpoint

Comandi standard:

```bash
mvnw.cmd clean compile -DskipTests
mvnw.cmd clean package -DskipTests
```

### 3.4 Sviluppo frontend

Flusso:
1. implementazione feature in src/app/features
2. integrazione service/interceptor core
3. verifica UX e integrazione con API

Comandi standard:

```bash
npm install
npm start
npm test
```

### 3.5 Integrazione e test end-to-end

Checklist minima:
- backend health up
- login funzionante
- chiamate API principali in esito 200
- frontend raggiungibile e navigabile
- CORS correttamente configurato

Strumenti:
- curl o Invoke-WebRequest
- Postman o Thunder Client
- log applicativi backend

### 3.6 Release readiness

Gate raccomandati:
- build backend e frontend verdi
- allineamento schema DB e mapping JPA
- verifica regressioni flussi core
- aggiornamento documentazione

### 3.7 Deploy e run

Dev runtime:
- database via docker compose
- backend da jar o plugin spring boot
- frontend via ng serve

Prod runtime (target):
- artifact versionati
- configurazione env-driven
- monitoraggio log ed error rate

### 3.8 Operazioni e manutenzione

Attivita ricorrenti:
- gestione incident e root cause analysis
- hardening progressivo sicurezza
- ottimizzazione query e prestazioni
- pulizia tecnica e riduzione debito

## 4. Ciclo di vita della richiesta applicativa

### 4.1 Flusso utente autenticazione

1. frontend invia login email/password
2. backend valida utente e stato attivo
3. backend restituisce token leggero dev-friendly
4. frontend invia Authorization Bearer nelle chiamate protette
5. backend risolve identita utente da header su endpoint auth/me

Nota: questo modello e scelto per leggerezza sviluppo. Per produzione e necessario modello robusto.

### 4.2 Flusso dominio (esempio elenco allievi)

1. GET frontend verso API allievi
2. controller delega a service
3. service recupera entity da repository
4. service mappa entity su DTO
5. risposta JSON al frontend

## 5. Governance tecnica

Ruoli tipici:
- Tech Lead: direzione tecnica, standard e quality gate
- Backend Dev: dominio, API, persistenza
- Frontend Dev: UX, componenti, integrazione
- Data/BI: reporting e analisi KPI

Decision policy:
- breaking change solo con nota migrazione
- documentazione aggiornata nello stesso branch
- test smoke obbligatori prima del merge

## 6. Security posture (stato corrente e roadmap)

Stato corrente:
- sicurezza semplificata per sviluppo rapido
- policy CORS attive su ambienti locali

Roadmap raccomandata:
- autenticazione robusta (JWT firmato o sessione server-side)
- policy autorizzative per ruoli
- rotazione secret e auditing accessi
- hardening HTTP headers e protezioni OWASP base

## 7. Osservabilita e troubleshooting

Metriche minime suggerite:
- disponibilita endpoint health
- tempi medi API core
- tasso errori 4xx/5xx
- errori connessione database

Playbook rapido:
1. verificare stato DB container
2. verificare health backend
3. testare login e endpoint core
4. analizzare stacktrace lato service/repository

## 8. Piano evolutivo

Evoluzioni prioritarie consigliate:
- suite test automatica per endpoint critici
- contract testing API frontend-backend
- CI pipeline con quality checks
- documentazione API formale (OpenAPI)
- strategia release con changelog strutturato

## 9. Riferimenti interni

- README root: visione globale repository
- WebApplication/Backend/README.md: struttura backend aggiornata
- WebApplication/Frontend/gestionale-frontend/README.md: guida frontend
- docs/issues: storico issue e analisi puntuali
