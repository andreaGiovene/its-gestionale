# Frontend - Gestionale ITS

Applicazione Angular per autenticazione, dashboard e gestione funzionale del dominio ITS.

## Stack frontend

- Angular CLI 21.2.3
- TypeScript
- SCSS
- HttpClient + interceptor per header Authorization

## Struttura principale

- src/app/core: guard, interceptor, servizi base
- src/app/features: moduli funzionali (auth, dashboard, corsi, allievi, aziende)
- src/app/shared: componenti e modelli condivisi
- src/styles.scss: stile globale

## Architettura frontend (focus tecnico)

### Core/AuthService

Responsabilita:
- login verso backend
- persistenza token in localStorage
- fetch profilo utente corrente

Nota progettuale:
- il servizio nasconde i dettagli del trasporto HTTP ai componenti
- i componenti consumano solo metodi ad alto livello

### Core/auth.interceptor

Responsabilita:
- intercettare richieste HTTP
- aggiungere Authorization Bearer quando esiste token

Vantaggio:
- nessuna duplicazione header nei vari service di feature

### Core/auth.guard

Responsabilita:
- protezione route private
- redirect a /login quando manca il token

Limitazione nota:
- la verifica e client-side, quindi in produzione va rafforzata con validazione server-side robusta

## Prerequisiti

- Node.js LTS
- npm
- Backend attivo su http://localhost:8080

## Avvio sviluppo

```bash
npm install
npm start
```

Frontend disponibile su http://localhost:4200

## Build

```bash
npm run build
```

Output in dist/.

## Test

```bash
npm test
```

Per test manuali funzionali consultare TEST_CHECKLIST.md.

## Contratto API Auth (stato attuale)

### Login

- Endpoint: POST http://localhost:8080/auth/login
- Request body:

```json
{
	"email": "admin@scuola.it",
	"password": "hash_secure_123"
}
```

- Response body:

```json
{
	"token": "admin@scuola.it",
	"tokenType": "Bearer",
	"expiresInSeconds": 0
}
```

### Profilo utente

- Endpoint: GET http://localhost:8080/auth/me
- Header: Authorization: Bearer <token>

## Sezioni da controllare quando qualcosa non funziona

- Login non naviga a dashboard:
	- verificare token in localStorage (chiave auth_token)
	- verificare risposta 200 di POST /auth/login
- Chiamate API non autorizzate:
	- verificare che interceptor sia registrato in app config
	- verificare formato header Authorization
- Loop sul guard:
	- verificare che token venga salvato dopo login
	- verificare route path /login e route protette

## Note sviluppo

- L'auth e semplificata per accelerare sviluppo e test integrati.
- Quando si reintroduce un token robusto, aggiornare solo il servizio auth e l'interceptor mantenendo invariati i componenti di dominio.
