# ITS Gestionale Tirocini

Monorepo per la gestione di corsi, allievi e tirocini ITS.

## Panoramica

Il progetto e composto da:
- Backend Spring Boot con API REST, autenticazione JWT e accesso PostgreSQL
- Frontend Angular per login e dashboard
- Database PostgreSQL avviabile via Docker Compose

## Stack Tecnologico

- Java 21
- Spring Boot 3.5.11
- Spring Data JPA / Hibernate
- Spring Security + JWT
- PostgreSQL 16
- Maven
- Angular (workspace in WebApplication/Frontend/gestionale-frontend)
- Docker Compose

## Struttura Repository

- WebApplication/Backend: applicazione Spring Boot
- WebApplication/Frontend/gestionale-frontend: applicazione Angular
- compose.yaml: servizi locali (PostgreSQL)
- Database, PowerBI, PowerApps, docs: materiali di supporto

## Avvio Rapido

### 1) Avvia il database

```bash
docker compose up -d
```

### 2) Avvia il backend

```bash
cd WebApplication/Backend
mvnw.cmd spring-boot:run
```

Backend disponibile su http://localhost:8080

### 3) Avvia il frontend

```bash
cd WebApplication/Frontend/gestionale-frontend
npm install
npm start
```

Frontend disponibile su http://localhost:4200

## Configurazione Backend (profilo dev)

File: WebApplication/Backend/src/main/resources/application-dev.properties

Valori correnti principali:
- Database: jdbc:postgresql://localhost:5432/db_its_stage
- Username/password: admin/admin
- Hibernate DDL: none
- SQL log: false
- CORS: http://localhost:4200,http://127.0.0.1:4200
- JWT expiration: 480 minuti

Nota sicurezza:
- app.jwt.secret e impostato per sviluppo locale; cambiarlo prima di ambienti condivisi o produzione.

## Entita e Schema

Le entita principali sono allineate con PostgreSQL (12 tabelle):
- allievo
- azienda
- caso_critico
- colloquio_tirocinio
- contatto_aziendale
- corso
- documento_tirocinio
- monitoraggio
- responsabile
- ruolo
- tirocinio
- utente

Sono presenti enum applicativi per i tipi del dominio (esito, tipo documento, ruolo contatto, tipo responsabile).

## Endpoint Principali

### Sistema
- GET /
- GET /health

### Auth
- POST /auth/login
- GET /auth/me

### API gestionali
- /api/corsi
- /api/allievi
- /api/utenti

## Build e Verifica

Compilazione backend:

```bash
cd WebApplication/Backend
mvnw.cmd compile -DskipTests
```

Package jar:

```bash
cd WebApplication/Backend
mvnw.cmd package -DskipTests
```

## Troubleshooting

### Backend non parte da jar

Verifica:
- di essere nella cartella corretta del backend
- che il database PostgreSQL sia raggiungibile
- che la porta 8080 sia libera

Comando utile:

```bash
cd WebApplication/Backend
java -jar target/gestionale-0.0.1-SNAPSHOT.jar
```

### Errore di connessione DB all'avvio

Controlla che il container PostgreSQL sia running:

```bash
docker compose ps
docker compose logs postgres
```

## Note Git

Branch di lavoro principale: andrea-dev

Remoti tipici:
- origin: repository condiviso
- backup: repository personale
