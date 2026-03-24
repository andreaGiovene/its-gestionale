# ITS Gestionale Tirocini

Applicazione Spring Boot per la gestione dei corsi e degli allievi di una scuola ITS.

## 📋 Specifiche Progetto

### Informazioni Generali
- **Nome Progetto**: gestionale
- **Versione**: 0.0.1-SNAPSHOT
- **Descrizione**: ITS Gestionale Tirocini - Sistema di gestione corsi e allievi
- **Organizzazione**: com.its.gestionale

### Stack Tecnologico

| Componente | Versione | Descrizione |
|-----------|----------|------------|
| **Java** | 21 | Linguaggio di programmazione |
| **Spring Boot** | 3.5.11 | Framework principale |
| **Spring Data JPA** | Latest | Persistenza dati e ORM |
| **Spring Security** | Latest | Autenticazione e autorizzazione |
| **Spring Web** | Latest | REST API e controller |
| **PostgreSQL** | 16 | Database relazionale |
| **Lombok** | Latest | Generazione automatica di getters/setters |
| **Maven** | Latest | Build tool e gestione dipendenze |

### Configurazione Ambiente

| Proprietà | Valore |
|-----------|--------|
| **Porta Server** | 8080 |
| **Database** | PostgreSQL 16 |
| **DDL Auto** | update (crea/aggiorna tabelle automaticamente) |
| **SQL Logging** | Abilitato (forma completa) |
| **Security** | Disabilitata temporaneamente (da riabilitare prima della produzione) |

### Database

- **Nome Database**: db_its_stage
- **Utente**: admin
- **Password**: admin
- **Host**: localhost:5432 (locale) / postgres:5432 (Docker)

## 🏗️ Architettura

### Struttura del Progetto

```
backend/
├── src/
│   ├── main/
│   │   ├── java/com/its/gestionale/
│   │   │   ├── GestionaleApplication.java        # Classe main
│   │   │   ├── controller/                       # REST API
│   │   │   │   ├── AllievoController.java
│   │   │   │   └── CorsoController.java
│   │   │   ├── dto/                              # Data Transfer Objects
│   │   │   │   └── AllievoDTO.java
│   │   │   ├── entity/                           # Entità JPA
│   │   │   │   ├── Allievo.java
│   │   │   │   └── Corso.java
│   │   │   ├── repository/                       # Data Access Layer
│   │   │   │   ├── AllievoRepository.java
│   │   │   │   └── CorsoRepository.java
│   │   │   └── service/                          # Business Logic
│   │   │       ├── AllievoService.java
│   │   │       └── CorsoService.java
│   │   └── resources/
│   │       ├── application.properties            # Configurazione
│   │       └── static/                           # Risorse statiche
│   └── test/
│       └── java/com/its/gestionale/
│           └── GestionaleApplicationTests.java
├── pom.xml                                       # Configurazione Maven
└── mvnw / mvnw.cmd                              # Maven Wrapper
```

### Pattern Architetturale

L'applicazione segue il pattern **MVC (Model-View-Controller)** con una struttura in **layer**:

1. **Controller Layer** (`controller/`)
   - Gestisce le richieste HTTP REST
   - Mappa gli endpoint API
   - Delega la logica ai servizi

2. **Service Layer** (`service/`)
   - Contiene la logica di business
   - Orchestrazione tra controller e repository
   - Validazioni di business

3. **Repository Layer** (`repository/`)
   - Accesso ai dati tramite Spring Data JPA
   - Query al database
   - Implementa il pattern DAO (Data Access Object)

4. **Entity Layer** (`entity/`)
   - Modelli dati mappati al database
   - Definisce le tabelle e le relazioni
   - Annotazioni JPA

5. **DTO Layer** (`dto/`)
   - Oggetti di trasferimento dati
   - Separazione tra API esterna e modello interno

## 🐳 Docker Compose

Il progetto include un file `compose.yaml` per l'orchestrazione dei servizi:

### Servizi

**PostgreSQL 16**
- Container: `postgres_db`
- Porta: `5432`
- Utente: `admin`
- Password: `admin`
- Database: `db_its_stage`
- Volume persistente: `postgres_data`

### Avviare i Servizi

```bash
docker-compose up -d
```

### Fermare i Servizi

```bash
docker-compose down
```

## 🚀 Guida all'Installazione e Esecuzione

### Prerequisiti

- **Java 21** o superiore
- **Maven 3.6+** (o usare Maven Wrapper incluso)
- **Docker e Docker Compose** (per il database)
- **Git**

### Step 1: Clonare il Repository

```bash
git clone <repository-url>
cd its-gestionale
```

### Step 2: Avviare il Database

```bash
docker-compose up -d
```

Verificare che il container PostgreSQL sia in esecuzione:
```bash
docker-compose ps
```

### Step 3: Compilare il Progetto

```bash
cd backend
./mvnw clean install
```

Oppure su Windows:
```bash
mvnw.cmd clean install
```

### Step 4: Avviare l'Applicazione

```bash
./mvnw spring-boot:run
```

Oppure su Windows:
```bash
mvnw.cmd spring-boot:run
```

L'applicazione sarà disponibile a: `http://localhost:8080`

### Step 5: Verificare il Funzionamento

```bash
curl http://localhost:8080/api/corsi
```

## 📚 Endpoint API

### Corsi API

| Metodo | Endpoint | Descrizione |
|--------|----------|------------|
| GET | `/api/corsi` | Ottieni tutti i corsi |
| GET | `/api/corsi/{id}` | Ottieni corso per ID |
| POST | `/api/corsi` | Crea un nuovo corso |
| PUT | `/api/corsi/{id}` | Aggiorna un corso |
| DELETE | `/api/corsi/{id}` | Elimina un corso |
| GET | `/api/corsi/stato/{stato}` | Ottieni corsi per stato |

### Allievi API

| Metodo | Endpoint | Descrizione |
|--------|----------|------------|
| GET | `/api/allievi` | Ottieni tutti gli allievi |
| GET | `/api/allievi/{id}` | Ottieni allievo per ID |
| POST | `/api/allievi` | Crea un nuovo allievo |
| PUT | `/api/allievi/{id}` | Aggiorna un allievo |
| DELETE | `/api/allievi/{id}` | Elimina un allievo |

## 🔐 Sicurezza

**⚠️ IMPORTANTE**: Spring Security è attualmente **disabilitata** per i test di sviluppo.

### Prima della Produzione

1. Riabilitare Spring Security nel `application.properties`:
   ```properties
   # Rimuovere questa riga:
   # spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
   ```

2. Implementare:
   - Configurazione di autenticazione e autorizzazione
   - JWT o OAuth 2.0 per gestire i token
   - HTTPS/TLS per le comunicazioni
   - CORS policies appropriate

## 🛠️ Sviluppo

### Compilare il Progetto

```bash
./mvnw clean compile
```

### Eseguire i Test

```bash
./mvnw test
```

### Generare Package JAR

```bash
./mvnw package
```

Il file JAR sarà disponibile in `backend/target/gestionale-0.0.1-SNAPSHOT.jar`

### Eseguire il JAR

```bash
java -jar backend/target/gestionale-0.0.1-SNAPSHOT.jar
```

## 📝 Configurazione

Il file di configurazione principale è `backend/src/main/resources/application.properties`:

```properties
# Nome dell'applicazione
spring.application.name=gestionale

# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/db_its_stage
spring.datasource.username=admin
spring.datasource.password=admin

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Server
server.port=8080
```

### Variabili Ambiente Supportate

- `SPRING_DATASOURCE_URL`: URL del database
- `SPRING_DATASOURCE_USERNAME`: Username database
- `SPRING_DATASOURCE_PASSWORD`: Password database
- `SERVER_PORT`: Porta del server

## 🧪 Testing

### Test Unitari

```bash
./mvnw test
```

### Test di Integrazione

```bash
./mvnw verify
```

## 📦 Build Docker (Futuro)

Per containerizzare l'applicazione:

```bash
docker build -t its-gestionale:latest .
docker run -p 8080:8080 --network its-gestionale_default its-gestionale:latest
```

## 🐛 Troubleshooting

### Problema: Connessione al Database Rifiutata

**Soluzione**: Verificare che il container PostgreSQL sia in esecuzione:
```bash
docker-compose ps
docker-compose logs postgres
```

### Problema: Porta 8080 già in uso

**Soluzione**: Modificare la porta in `application.properties`:
```properties
server.port=8081
```

### Problema: Maven Non Trovato

**Soluzione**: Usare il Maven Wrapper incluso:
```bash
./mvnw clean install
```

## 📖 Documentazione Aggiuntiva

- [Spring Boot Official Documentation](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [Docker Documentation](https://docs.docker.com/)

## 📄 Licenza

Progetto educativo per ITS.

## 👥 Autori

- **Andrea Giovene** - Sviluppatore principale

## 📞 Contatti e Support

Per domande o segnalare bug, contattare il team di sviluppo.

---

**Ultima modifica**: Marzo 2026
**Stato**: In Sviluppo 🚧
