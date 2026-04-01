# WebApplication

Macro-area applicativa che contiene backend e frontend del gestionale ITS.

## Struttura

- Backend: API REST, persistenza, logica applicativa
- Frontend/gestionale-frontend: UI Angular

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
