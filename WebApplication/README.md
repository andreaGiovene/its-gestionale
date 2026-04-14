# WebApplication

Macro-area applicativa che contiene backend e frontend del gestionale ITS.

## Architettura

- **Backend Spring Boot**: API REST RESTful con ricerca avanzata (Criteria API), logica di dominio, persistenza JPA/Hibernate
- **Frontend Angular**: UI reattiva, autenticazione client-side, guard di routing con RBAC (role-based access control)
- **PostgreSQL**: persistenza relazionale con enum nativo (tipo_azienda_enum) e migrazione versionata

## Struttura

- Backend: API REST, persistenza, logica applicativa
- Frontend/gestionale-frontend: UI Angular con componenti reattivi e guards
- Database: dump SQL e migrazioni

## Flusso locale consigliato

1. Avviare PostgreSQL da root repository:

```bash
docker compose up -d
```

2. Avviare backend:

```bash
cd Backend
mvnw.cmd clean package -DskipTests
java -jar target/gestionale-0.0.1-SNAPSHOT.jar
```

3. Avviare frontend:

```bash
cd Frontend/gestionale-frontend
npm install
npm start
```

## Endpoints base

- Backend health: http://localhost:8080/health
- Frontend: http://localhost:4200

## Documentazione specifica

- Backend: Backend/README.md
- Frontend: Frontend/gestionale-frontend/README.md
