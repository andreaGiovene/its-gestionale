# ITS Gestionale Tirocini

Monorepo applicativo per la gestione del ciclo corsi-allievi-tirocini ITS.

## Obiettivo del repository

Il repository centralizza:
- applicazione web (backend + frontend)
- dataset e dump SQL di lavoro
- artefatti di analisi BI
- materiale di supporto operativo

## Architettura ad alto livello

- Frontend Angular: interfaccia utente e navigazione per aree funzionali
- Backend Spring Boot: API REST, logica applicativa, persistenza JPA
- Database PostgreSQL: storage relazionale del dominio formativo
- Docker Compose: bootstrap locale del servizio database

## Macro-aree del progetto

- WebApplication: codice sorgente applicativo
- Database: dump SQL e contenuti di inizializzazione
- PowerBI: artefatti di reporting
- PowerApps: workspace integrazione low-code
- docs: documentazione tecnica e issue notes

## Mappa documentazione

- README root: visione complessiva repository
- WebApplication/README.md: guida applicativa full stack
- WebApplication/Backend/README.md: struttura backend aggiornata e operativita runtime
- WebApplication/Frontend/gestionale-frontend/README.md: guida frontend aggiornata
- Database/README.md: gestione dump e ripristino
- docs/APPLICATION_LIFECYCLE.md: ciclo di vita completo dell'applicazione
- docs/IMPLEMENTAZIONI_2026-04-14_REFACTOR-ENUM-CLEANUP.md: refactoring aziende e enum nativo PostgreSQL

## Avvio rapido locale

### 1. Database

```powershell
./scripts/bootstrap-db.ps1
```

Il bootstrap applica in ordine:
- dump iniziale (`Database/dump-its.sql`)
- migration SQL (`Database/migrations/*.sql`)

Nota operativa:
- `docker compose up -d` avvia solo il container; con volume gia esistente non riallinea automaticamente lo schema.
- Se devi forzare un reset completo del DB locale: `./scripts/bootstrap-db.ps1 -ResetVolume`.

### 2. Backend

```bash
cd WebApplication/Backend
mvnw.cmd clean package -DskipTests
java -jar target/gestionale-0.0.1-SNAPSHOT.jar
```

Backend su:
- http://localhost:8080
- http://localhost:8080/health

### 3. Frontend

```bash
cd WebApplication/Frontend/gestionale-frontend
npm install
npm start
```

Frontend su:
- http://localhost:4200

## Modello di autenticazione in sviluppo

Per favorire leggerezza e velocita di sviluppo:
- e stata rimossa l'implementazione JWT completa
- login continua a usare email/password
- il token restituito e semplificato (valore identificativo utente)
- /auth/me legge l'header Authorization in formato Bearer

### Frontend authentication (Angular)

Implementazione completa di autenticazione e autorizzazione:
- **AuthService**: centralizza token (localStorage), currentUser state, metodi hasRole()
- **authGuard**: CanActivateFn per protezione rotte con controllo ruoli via route.data['roles']
- **App bootstrap**: ngOnInit ripristina currentUser se token persiste (scenario F5)
- Login flow: login().pipe(switchMap(() => me())) per popolare currentUser post-login

Nota: in produzione e consigliata una strategia token robusta (JWT o sessione server-side con rotazione e policy).


Per dettagli architetturali e processo operativo, consultare docs/APPLICATION_LIFECYCLE.md.
