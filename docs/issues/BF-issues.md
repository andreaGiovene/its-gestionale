# 📋 Backlog Completo — Gestionale ITS

> **Status Progetto**: 71% completato (34/48 issue) ✅  
> **Ultimo aggiornamento**: 23 Aprile 2026  
> **Versione**: 1.0

---

## 🎯 Roadmap per Sezioni Strategiche

### [S1 — Setup & Architettura](#s1--setup--architettura-) ✅ **100% COMPLETO**

## S1 — Setup & Architettura

**Obiettivo**: Struttura base del progetto con autenticazione  
**Status**: ✅ 100% COMPLETO (7/7)

| BF-002 | Creazione progetto Angular con struttura moduli (core, shared, feature modules) | Alta | setup | — | ✅ |
| BF-003 | Configurazione Spring Security con autenticazione JWT | Alta | auth | RNF-02 | ✅ |
| BF-004 | API POST /auth/login e GET /auth/me | Alta | auth | RNF-02 | ✅ |
| BF-005 | Pagina login Angular con form e gestione token JWT in localStorage | Alta | auth | RNF-02 | ✅ |
| BF-006 | AuthGuard Angular per protezione route per ruolo | Alta | auth | RNF-03 | ✅ |
| BF-007 | Configurazione CORS, profile dev/prod Spring Boot | Media | setup | — | ✅ |
BF-005	Pagina login Angular con form e gestione token JWT in localStorage	Alta	auth	RNF-02
BF-006	AuthGuard Angular per protezione route per ruolo	Alta	auth	RNF-03
BF-007	Configurazione CORS, profile dev/prod Spring Boot	Media	setup	

| BF-008 | Entity JPA e Repository per Corso, Allievo | Alta | backend | Sez.4 | ✅ |
| BF-009 | API GET/POST/PUT/DELETE /api/corsi | Alta | backend | RF-01 | ✅ |
| BF-010 | API GET /api/corsi/{id}/allievi e CRUD /api/allievi | Alta | backend | RF-02,RF-03 | ✅ |
| BF-011 | API GET /api/allievi?stageAssegnato=false (allievi senza stage) | Alta | backend | RF-04 | ⏳ |
| BF-012 | API POST /api/import/allievi per upload file Excel | Media | backend | RF-05 | ⏳ |
| BF-014 | Pagina Angular lista corsi con ricerca e filtri | Alta | frontend | RF-01 | ✅ |
BF-012	API POST /api/import/allievi per upload file Excel	Media	backend	RF-05
BF-013	Layout Angular: sidebar navigazione, header, breadcrumb	Alta	frontend	
BF-014	Pagina Angular lista corsi con ricerca e filtri	Alta	frontend	RF-01

| BF-013 | Layout Angular: sidebar navigazione, header, breadcrumb | Alta | frontend | — | ✅ |
| BF-015 | Entity JPA e Repository per Azienda, ContattoAziendale | Alta | backend | Sez.4 | ✅ |
| BF-016 | API CRUD /api/aziende e /api/aziende/{id}/contatti | Alta | backend | RF-06,RF-07 | ✅ |
| BF-017 | API GET /api/aziende con ricerca e filtri (ragione sociale, tipo) | Alta | backend | RF-08 | ✅ |
| BF-018 | API POST /api/import/aziende per upload Excel con deduplicazione | Media | backend | RF-09 | ⏳ |
| BF-019 | Pagina Angular dettaglio corso con elenco allievi | Alta | frontend | RF-02 | ✅ |
| BF-020 | Pagina Angular lista allievi con ricerca cross-corso | Alta | frontend | RF-03 | ✅ |
| BF-021 | Pagina Angular lista aziende con ricerca e filtri | Alta | frontend | RF-08 | ✅ |
| BF-022 | Pagina Angular dettaglio azienda con contatti | Alta | frontend | RF-07 | ✅ |
BF-022	Pagina Angular dettaglio azienda con contatti	Alta	frontend	RF-07

S4 — API Colloqui + FE Colloqui
Issue ID	Titolo Issue (GitHub)	Priority	Label	RF
| BF-023 | Entity JPA e Repository per ColloquioTirocinio | Alta | backend | Sez.4 | ✅ |
| BF-024 | API CRUD /api/colloqui con filtri per allievo e azienda | Alta | backend | RF-10,RF-11,RF-12 | ✅ |
| BF-025 | Pagina Angular registrazione colloquio (form con select azienda, esito, note) | Alta | frontend | RF-10 | ✅ |
| BF-026 | Pagina Angular storico colloqui per allievo | Alta | frontend | RF-11 | ✅ |
| BF-027 | Pagina Angular colloqui per azienda | Media | frontend | RF-12 | ✅ |

S5 — API Tirocini, Documenti, Monitoraggi
Issue ID	Titolo Issue (GitHub)	Priority	Label	RF
BF-028	Entity JPA e Repository per Tirocinio, DocumentoTirocinio, Monitoraggio	Alta	backend	Sez.4
| BF-028 | Entity JPA e Repository per Tirocinio, DocumentoTirocinio, Monitoraggio | Alta | backend | Sez.4 | ✅ |
| BF-029 | API CRUD /api/tirocini con filtri per allievo e azienda | Alta | backend | Sez.3.4 | ✅ |
| BF-030 | API CRUD /api/tirocini/{id}/documenti per gestione documenti | Alta | backend | RF-13,RF-14 | ✅ |
| BF-031 | API CRUD /api/monitoraggi e GET /api/monitoraggi/pianifica con ottimizzazione CAP | Alta | backend | RF-16,RF-17 | ✅ |
| BF-032 | Integrazione servizio SMTP per invio email (Spring Mail) | Alta | backend | RF-19,RF-20 | ⏳ |
| BF-033 | API POST /api/email/monitoraggio per invio email automatica azienda | Alta | backend | RF-18,RF-20 | ⏳ |
| BF-034 | API GET /api/email/storico per storico comunicazioni | Media | backend | RF-22 | ⏳ |
S6 — FE Tirocini, Documenti, Monitoraggi, Dashboard
Issue ID	Titolo Issue (GitHub)	Priority	Label	RF
BF-035	Pagina Angular gestione tirocinio: form con dati, tipo, esito, contratto	Alta	frontend	Sez.3.4
BF-036	Componente Angular semaforo documenti (verde/rosso per ogni documento)	Alta	frontend	RF-13,RF-14
| BF-035 | Pagina Angular gestione tirocinio: form con dati, tipo, esito, contratto | Alta | frontend | Sez.3.4 | ✅ |
| BF-036 | Componente Angular semaforo documenti (verde/rosso per ogni documento) | Alta | frontend | RF-13,RF-14 | ⏳ |
| BF-037 | Pagina Angular pianificazione monitoraggio (selezione aziende, data, percorso ottimizzato) | Alta | frontend | RF-17 | ⏳ |
| BF-038 | Pagina Angular dashboard KPI con grafici (ng2-charts / Chart.js) | Alta | frontend | RF-23,RF-24 | ⏳ |
| BF-039 | Home page post-login con box di riepilogo configurabili | Alta | frontend | RF-23 | ⏳ |
Issue ID	Titolo Issue (GitHub)	Priority	Label	RF
BF-040	Implementazione gestione ruoli e permessi nel FE (menu e azioni condizionali)	Alta	auth	RNF-03
BF-041	Pagina Angular invio email ad hoc ad aziende/allievi	Media	frontend	RF-21
BF-042	Responsive layout per tablet (Angular breakpoints)	Media	frontend	RNF-04
| BF-040 | Implementazione gestione ruoli e permessi nel FE (menu e azioni condizionali) | Alta | auth | RNF-03 | ⏳ |
| BF-041 | Pagina Angular invio email ad hoc ad aziende/allievi | Media | frontend | RF-21 | ⏳ |
| BF-042 | Responsive layout per tablet (Angular breakpoints) | Media | frontend | RNF-04 | ⏳ |
| BF-043 | Upload documento digitale da frontend con API multipart | Media | frontend | RF-15 | ⏳ |
BF-044	Configurazione Swagger/OpenAPI per documentazione API	Alta	docs	
BF-045	Test unitari service layer Spring Boot (JUnit, Mockito) >70% coverage	Alta	test	
BF-046	Test E2E Angular con Cypress per flussi principali	Media	test	
BF-047	Deploy su ambiente di staging e collaudo finale	Alta	devops	
| BF-044 | Configurazione Swagger/OpenAPI per documentazione API | Alta | docs | — | ⏳ |
| BF-045 | Test unitari service layer Spring Boot (JUnit, Mockito) >70% coverage | Alta | test | — | ⏳ |
| BF-046 | Test E2E Angular con Cypress per flussi principali | Media | test | — | ⏳ |
| BF-047 | Deploy su ambiente di staging e collaudo finale | Alta | devops | — | ⏳ |
| BF-048 | Bug fixing e ottimizzazione performance API (query N+1) | Alta | bug | — | ⏳ |
