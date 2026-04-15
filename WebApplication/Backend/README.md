# Backend - ITS Gestionale Tirocini

Applicazione Spring Boot responsabile di API, logica di dominio e integrazione con PostgreSQL.

## Stack tecnico

- Java 21
- Spring Boot 3.5.11
- Spring Data JPA / Hibernate
- Spring Security (config semplificata per sviluppo)
- PostgreSQL 16
- Maven

## Struttura filesystem aggiornata

```text
Backend/
├── pom.xml
├── mvnw
├── mvnw.cmd
├── src/
│   ├── main/
│   │   ├── java/com/its/gestionale/
│   │   │   ├── GestionaleApplication.java
│   │   │   ├── config/
│   │   │   │   ├── CorsConfig.java
│   │   │   │   └── SecurityConfig.java
│   │   │   ├── controller/
│   │   │   │   ├── AllievoController.java
│   │   │   │   ├── AziendaController.java
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── CorsoController.java
│   │   │   │   ├── SystemController.java
│   │   │   │   └── UtenteController.java
│   │   │   ├── dto/
│   │   │   │   ├── AllievoDTO.java
│   │   │   │   ├── AziendaDTO.java
│   │   │   │   ├── CorsoDTO.java
│   │   │   │   ├── UtenteDTO.java
│   │   │   │   └── auth/
│   │   │   │       ├── LoginRequest.java
│   │   │   │       ├── LoginResponse.java
│   │   │   │       └── MeResponse.java
│   │   │   ├── exception/
│   │   │   │   ├── ApiErrorResponse.java
│   │   │   │   ├── ApiExceptionHandler.java
│   │   │   │   ├── AziendaNotFoundException.java
│   │   │   │   ├── CorsoNotFoundException.java
│   │   │   │   └── GlobalExceptionHandler.java
│   │   │   ├── entity/
│   │   │   │   ├── Allievo.java
│   │   │   │   ├── Azienda.java
│   │   │   │   ├── CasoCritico.java
│   │   │   │   ├── ColloquioTirocinio.java
│   │   │   │   ├── ContattoAziendale.java
│   │   │   │   ├── Corso.java
│   │   │   │   ├── DocumentoTirocinio.java
│   │   │   │   ├── Monitoraggio.java
│   │   │   │   ├── Responsabile.java
│   │   │   │   ├── Ruolo.java
│   │   │   │   ├── Tirocinio.java
│   │   │   │   ├── Utente.java
│   │   │   │   └── enums/
│   │   │   │       ├── RuoloContattoAziendale.java
│   │   │   │       ├── StatoEsito.java
│   │   │   │       ├── TipoAzienda.java
│   │   │   │       ├── TipoDocumento.java
│   │   │   │       └── TipoResponsabileITS.java
│   │   │   ├── repository/
│   │   │   │   ├── AllievoRepository.java
│   │   │   │   ├── AziendaRepository.java
│   │   │   │   ├── CasoCriticoRepository.java
│   │   │   │   ├── ColloquioTirocinioRepository.java
│   │   │   │   ├── ContattoAzienddaleRepository.java
│   │   │   │   ├── CorsoRepository.java
│   │   │   │   ├── DocumentoTirocinioRepository.java
│   │   │   │   ├── MonitoraggioRepository.java
│   │   │   │   ├── ResponsabileRepository.java
│   │   │   │   ├── RuoloRepository.java
│   │   │   │   ├── TirocinioRepository.java
│   │   │   │   └── UtenteRepository.java
│   │   │   ├── repository/
│   │   │   │   └── specification/
│   │   │   │       └── AziendaSpecifications.java
│   │   │   │
│   │   │   └── service/
│   │   │       ├── AllievoService.java
│   │   │       ├── AziendaService.java
│   │   │       ├── AuthService.java
│   │   │       ├── CorsoService.java
│   │   │       └── UtenteService.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-dev.properties
│   │       └── application-prod.properties
│   └── test/
│       └── java/com/its/gestionale/
│           └── GestionaleApplicationTests.java
└── target/
```

## Configurazione runtime

### Profilo dev

- DB: jdbc:postgresql://localhost:5432/db_its_stage
- User/password: admin/admin
- DDL: none
- CORS: localhost:4200

### Sicurezza in sviluppo

Il backend usa una modalita semplificata:
- login con email/password su POST /auth/login
- token di risposta leggero
- GET /auth/me con Authorization Bearer

Questa scelta riduce complessita e tempi di sviluppo.

## Parti di codice non autoesplicative (spiegazione)

### AuthService

Logica centrale di autenticazione in modalita dev-light:
- login valida utente, stato attivo e password
- aggiorna timestamp di accesso
- restituisce un token volutamente semplice per velocizzare il ciclo UI/API

Perche e stato fatto cosi:
- il frontend resta stabile (continua a leggere un campo token)
- si evita overhead di signing/verifica token in fase di sviluppo funzionale

### AuthController

Il controller espone due endpoint:
- POST /auth/login
- GET /auth/me

La parte delicata e /auth/me: legge Authorization header e delega parsing/validazione al service.
Questa separazione mantiene il controller magro e aumenta testabilita della logica.

### SecurityConfig

Configurazione intenzionalmente minimale:
- csrf/httpBasic/formLogin disattivati
- policy stateless
- permitAll su tutte le request

Razionale:
- ridurre attrito tecnico durante sviluppo dominio
- evitare coupling forte con framework security mentre i flussi sono in evoluzione

### Corso entity

Punti critici:
- mapping campo nome su nome_corso (allineamento schema DB reale)
- stato modellato come String per compatibilita con valori legacy nel DB
- relazione one-to-many con allievi marcata JsonIgnore per evitare serializzazione ricorsiva

### AllievoService

Metodi di lettura marcati transactional read-only.
Motivo: mappatura DTO che accede a relazioni lazy senza errori runtime durante serializzazione.

## Build e avvio

```bash
mvnw.cmd clean compile -DskipTests
mvnw.cmd clean package -DskipTests
java -jar target/gestionale-0.0.1-SNAPSHOT.jar
```

## Smoke test backend

```bash
curl http://localhost:8080/health
curl -X POST http://localhost:8080/auth/login -H "Content-Type: application/json" -d "{\"email\":\"admin@scuola.it\",\"password\":\"hash_secure_123\"}"
```

## API REST aziende

Endpoint implementati secondo naming REST standard con ricerca avanzata:

- GET /api/aziende: lista paginata con filtri opzionali (tipo, ragioneSociale, corsoId)
- GET /api/aziende/{id}: dettaglio azienda
- POST /api/aziende: creazione azienda
- PUT /api/aziende/{id}: aggiornamento azienda esistente
- DELETE /api/aziende/{id}: eliminazione azienda

### TipoAzienda enum

Campo enum obbligatorio che classifica l'azienda nel tirocinio:
- **MADRINA**: azienda partner principale (sede tirocinio)
- **NON_MADRINA**: azienda partner secondaria/complementare

Mapping tecnico:
- Java entity: TipoAzienda enum con @Enumerated(EnumType.STRING)
- PostgreSQL: enum nativo tipo_azienda_enum
- JPA mapping: @JdbcTypeCode(SqlTypes.NAMED_ENUM) per compatibilita Hibernate

### DTO e mapping

Il controller non espone direttamente le entity JPA ma usa AziendaDTO.

Campi AziendaDTO:
- id
- ragioneSociale (obbligatorio, max 100 char)
- partitaIva (obbligatorio, max 20 char)
- tipoAzienda (obbligatorio, enum MADRINA|NON_MADRINA)
- telefono, email, indirizzo, cap, citta (facoltativi con vincoli lunghezza)

Validazioni:
- @NotBlank su campi obbligatori
- @NotNull su tipoAzienda
- @Size per vincoli lunghezza

### AziendaSpecifications (ricerca avanzata)

Pattern Criteria API per ricerca composabile:
- ragioneSocialeContains(string): match parziale case-insensitive
- hasCorsoMadrina(corsoId): aziende madrine per corso specifico
- hasTipoAzienda(tipo): filtro per MADRINA|NON_MADRINA

Tutti i filtri sono opzionali (null = nessun filtro applicato).

Utilizzo in AziendaService.search():
```java
Specification<Azienda> specification = 
  AziendaSpecifications.ragioneSocialeContains(ragioneSociale)
    .and(AziendaSpecifications.hasCorsoMadrina(corsoId))
    .and(AziendaSpecifications.hasTipoAzienda(tipo));

return aziendaRepository.findAll(specification, pageable).map(AziendaDTO::fromEntity);
```

### Esempio payload POST/PUT

```json
{
	"ragioneSociale": "Acme SpA - Italia",
	"partitaIva": "12345678901",
	"telefono": "+39 02 1234567",
	"email": "hr@acme.it",
	"indirizzo": "Via Roma 1",
	"cap": "20100",
	"citta": "Milano",
	"tipoAzienda": "MADRINA"
}
```

## API REST corsi

Endpoint implementati secondo naming REST standard:

- GET /api/corsi: lista di tutti i corsi
- GET /api/corsi/{id}: dettaglio corso
- POST /api/corsi: creazione corso
- PUT /api/corsi/{id}: aggiornamento corso esistente
- DELETE /api/corsi/{id}: eliminazione corso

### DTO e mapping

Il controller non espone direttamente le entity JPA ma usa CorsoDTO.

Campi CorsoDTO:
- id
- nome (obbligatorio)
- annoAccademico
- stato (obbligatorio)
- allieviCount (campo derivato, predisposto per integrazione futura con modulo allievi)

Il mapping Entity <-> DTO e centralizzato in CorsoService, che contiene anche tutta la logica applicativa CRUD.

### Esempio payload POST/PUT

```json
{
	"nome": "Corso Java Backend",
	"annoAccademico": "2025/2026",
	"stato": "In corso"
}
```

### Gestione errori

E presente una gestione eccezioni globale con @RestControllerAdvice:

- 404 Corso non trovato:
	- eccezione: CorsoNotFoundException
	- casi: GET/PUT/DELETE su id inesistente
- 400 Validazione input:
	- eccezione: MethodArgumentNotValidException
	- casi: payload non conforme ai vincoli (@NotBlank, @Size)
- 500 Errore imprevisto:
	- fallback centralizzato

Formato risposta errore (ApiErrorResponse):
- timestamp
- status
- error
- message
- path
- validationErrors (mappa campo -> messaggio, solo per 400 di validazione)

Esempio 404:

```json
{
	"timestamp": "2026-03-31T11:30:00Z",
	"status": 404,
	"error": "Not Found",
	"message": "Corso con id 999 non trovato",
	"path": "/api/corsi/999",
	"validationErrors": null
}
```

Esempio 400 (validazione):

```json
{
	"timestamp": "2026-03-31T11:31:00Z",
	"status": 400,
	"error": "Bad Request",
	"message": "Input non valido",
	"path": "/api/corsi",
	"validationErrors": {
		"nome": "Il nome del corso e obbligatorio"
	}
}
```

## Architettura Frontend - Autenticazione e Autorizzazione

### Componenti core

**AuthService** (centralizzato):
- persistenza token JWT in localStorage
- gestione stato currentUser (MeResponse)
- metodi: login(), me(), logout(), isAuthenticated(), hasRole()

**authGuard** (CanActivateFn):
- protezione rotte: verifica authentication
- controllo autorizzativo: route.data['roles'] vs currentUser.ruolo
- redirect: /login se unauthenticated, /dashboard se unauthorized

**App bootstrap** (ngOnInit):
- al caricamento pagina, se token persiste → chiama me()
- popola currentUser per accesso guard
- fallback logout se token invalido

### Pattern utilizzo

Configurazione route con ruoli:
```typescript
{
  path: 'aziende',
  component: AziendeLis,
  canActivate: [authGuard],
  data: { roles: ['AMMINISTRATORE', 'JOB_PLACEMENT', 'DIDATTICA'] }
}
```

Verifica permesso in componente:
```typescript
hasRole(role: string): boolean {
  return this.authService.hasRole(role);
}
```

## Ambiti di miglioramento futuri

- reintroduzione autenticazione robusta per produzione
- centralizzazione error handling
- test automatici su repository/service/controller
- pipeline CI con quality gate
- integrazione role-based authorization completa su tutte le rotte
- completamento test smoke e e2e per API core
