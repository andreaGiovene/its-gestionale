# Database

Macro-area dati con schema relazionale PostgreSQL utilizzato dal backend Spring Boot.

## Contenuti

- dump.sql: dump principale del database applicativo

## Contesto runtime

- DB: PostgreSQL
- Nome database: db_its_stage
- Utente: admin
- Container locale standard: postgres_db

## Ripristino dati

### Ripristino in PostgreSQL locale

```bash
psql -U admin -d db_its_stage -f dump.sql
```

### Ripristino in container Docker

```bash
docker exec -i postgres_db psql -U admin -d db_its_stage < dump.sql
```

## Catalogo tabelle

Lo schema applicativo e composto da 12 tabelle principali:

1. allievo
2. azienda
3. caso_critico
4. colloquio_tirocinio
5. contatto_aziendale
6. corso
7. documento_tirocinio
8. monitoraggio
9. responsabile
10. ruolo
11. tirocinio
12. utente

## Relazioni tra tabelle (modello logico)

### Blocco utenti e ruoli

- ruolo (1) -> (N) utente
	- FK: utente.id_ruolo -> ruolo.id_ruolo

### Blocco anagrafica didattica

- corso (1) -> (N) allievo
	- FK: allievo.corso_id -> corso.id
- utente (1) -> (1) allievo
	- FK: allievo.id_utente -> utente.id_utente
	- vincolo unico su id_utente lato allievo

### Blocco aziende e contatti

- azienda (1) -> (N) contatto_aziendale
	- FK: contatto_aziendale.azienda_id -> azienda.id
- utente (1) -> (1) contatto_aziendale
	- FK: contatto_aziendale.id_utente -> utente.id_utente

### Blocco tirocini

- allievo (1) -> (N) tirocinio
	- FK: tirocinio.allievo_id -> allievo.id
- azienda (1) -> (N) tirocinio
	- FK: tirocinio.azienda_id -> azienda.id
- tirocinio (1) -> (N) documento_tirocinio
	- FK: documento_tirocinio.tirocinio_id -> tirocinio.id
- tirocinio (1) -> (N) monitoraggio
	- FK: monitoraggio.tirocinio_id -> tirocinio.id

### Blocco eventi e criticita

- allievo (1) -> (N) colloquio_tirocinio
	- FK: colloquio_tirocinio.allievo_id -> allievo.id
- azienda (1) -> (N) colloquio_tirocinio
	- FK: colloquio_tirocinio.azienda_id -> azienda.id
- allievo (1) -> (N) caso_critico
	- FK: caso_critico.allievo_id -> allievo.id

### Blocco responsabili

- utente (1) -> (1) responsabile
	- FK: responsabile.id_utente -> utente.id_utente
	- vincolo unico su id_utente lato responsabile

Nota su monitoraggio:
- colonna monitoraggio.responsabile e presente come intero, ma attualmente non e mappata come FK esplicita nel backend.

## Mapping database -> entita backend

Questa sezione descrive come lo schema SQL e stato implementato in JPA nel backend.

### Tabella ruolo

- Entita: WebApplication/Backend/src/main/java/com/its/gestionale/entity/Ruolo.java
- PK: id_ruolo -> idRuolo
- Relazione inversa: @OneToMany(mappedBy = "ruolo") verso Utente

### Tabella utente

- Entita: WebApplication/Backend/src/main/java/com/its/gestionale/entity/Utente.java
- PK: id_utente -> idUtente
- FK: id_ruolo con @ManyToOne verso Ruolo
- Campi audit: creato_il, aggiornato_il, ultimo_accesso

### Tabella corso

- Entita: WebApplication/Backend/src/main/java/com/its/gestionale/entity/Corso.java
- PK: id
- Mapping colonna nome_corso -> campo nome
- Mapping stato come String per compatibilita con valori presenti nel dump (es. "In corso", "Concluso")
- Relazione inversa: @OneToMany verso Allievo

### Tabella allievo

- Entita: WebApplication/Backend/src/main/java/com/its/gestionale/entity/Allievo.java
- PK: id
- FK corso_id con @ManyToOne verso Corso
- FK id_utente con @OneToOne verso Utente (unique)
- Relazioni inverse verso Tirocinio, CasoCritico, ColloquioTirocinio

### Tabella azienda

- Entita: WebApplication/Backend/src/main/java/com/its/gestionale/entity/Azienda.java
- PK: id
- Relazioni inverse: contatti (@OneToMany verso ContattoAziendale), tirocini (@OneToMany verso Tirocinio)

### Tabella tirocinio

- Entita: WebApplication/Backend/src/main/java/com/its/gestionale/entity/Tirocinio.java
- PK: id
- FK allievo_id -> @ManyToOne Allievo
- FK azienda_id -> @ManyToOne Azienda
- Enum stato esito: @Enumerated(EnumType.STRING) su campo esito (StatoEsito)
- Relazioni inverse: documenti, monitoraggi

### Tabella documento_tirocinio

- Entita: WebApplication/Backend/src/main/java/com/its/gestionale/entity/DocumentoTirocinio.java
- PK: id
- FK tirocinio_id -> @ManyToOne Tirocinio
- Enum tipo documento: @Enumerated(EnumType.STRING) (TipoDocumento)

### Tabella monitoraggio

- Entita: WebApplication/Backend/src/main/java/com/its/gestionale/entity/Monitoraggio.java
- PK: id
- FK tirocinio_id -> @ManyToOne Tirocinio
- Campo responsabile mappato come Integer responsabileId (non relazione JPA al momento)

### Tabella caso_critico

- Entita: WebApplication/Backend/src/main/java/com/its/gestionale/entity/CasoCritico.java
- PK: id
- FK allievo_id -> @ManyToOne Allievo
- Flag stato: risolto (boolean)

### Tabella colloquio_tirocinio

- Entita: WebApplication/Backend/src/main/java/com/its/gestionale/entity/ColloquioTirocinio.java
- PK: id
- FK allievo_id -> @ManyToOne Allievo
- FK azienda_id -> @ManyToOne Azienda
- Enum esito: @Enumerated(EnumType.STRING) (StatoEsito)

### Tabella responsabile

- Entita: WebApplication/Backend/src/main/java/com/its/gestionale/entity/Responsabile.java
- PK: id
- FK id_utente -> @OneToOne Utente
- Enum tipo: @Enumerated(EnumType.STRING) (TipoResponsabile)

### Tabella contatto_aziendale

- Entita: WebApplication/Backend/src/main/java/com/its/gestionale/entity/ContattoAziendale.java
- PK: id
- FK azienda_id -> @ManyToOne Azienda
- FK id_utente -> @OneToOne Utente
- Enum ruolo: @Enumerated(EnumType.STRING) (RuoloContatto)

## Enum di dominio mappati

Definiti in WebApplication/Backend/src/main/java/com/its/gestionale/entity/enums:

- StatoEsito
- TipoResponsabile
- RuoloContatto
- TipoDocumento

Gli enum sono persistiti come stringa (EnumType.STRING) nelle entita che li utilizzano.

## Coerenza schema/backend: note importanti

- Hibernate ddl-auto e impostato a none in dev, quindi il backend non altera automaticamente lo schema.
- Ogni variazione SQL deve essere accompagnata da:
	- aggiornamento entity/repository
	- ricompilazione backend
	- aggiornamento dump.sql

## Checklist quando si aggiunge o modifica una tabella

1. Aggiornare dump.sql
2. Creare/aggiornare entity con @Table/@Column corretti
3. Mappare relazioni con cardinalita coerenti
4. Aggiornare repository e query derivata
5. Verificare endpoint che consumano DTO correlati
6. Eseguire smoke test API principali
