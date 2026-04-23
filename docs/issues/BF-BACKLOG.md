# 📋 Issue da Completare

## S2 — API Corsi & Allievi

| ID | Titolo | Priority | Label | RF | Note |
|---|---|---|---|---|---|
| BF-011 | API GET /api/allievi?stageAssegnato=false (allievi senza stage) | Alta | backend | RF-04 | ⏳ Da fare |
| BF-012 | API POST /api/import/allievi per upload file Excel | Media | backend | RF-05 | ⏳ Da fare |

## S3 — API Aziende + FE Allievi/Aziende

| ID | Titolo | Priority | Label | RF | Note |
|---|---|---|---|---|---|
| BF-018 | API POST /api/import/aziende per upload Excel con deduplicazione | Media | backend | RF-09 | ⏳ Da fare |

## S5 — API Tirocini, Documenti, Monitoraggi

| ID | Titolo | Priority | Label | RF | Note |
|---|---|---|---|---|---|
| BF-032 | Integrazione servizio SMTP per invio email (Spring Mail) | Alta | backend | RF-19,RF-20 | ⏳ Da fare |
| BF-033 | API POST /api/email/monitoraggio per invio email automatica azienda | Alta | backend | RF-18,RF-20 | ⏳ Da fare |
| BF-034 | API GET /api/email/storico per storico comunicazioni | Media | backend | RF-22 | ⏳ Da fare |

## S6 — FE Tirocini, Documenti, Monitoraggi, Dashboard

| ID | Titolo | Priority | Label | RF | Note |
|---|---|---|---|---|---|
| BF-036 | Componente Angular semaforo documenti (verde/rosso per ogni documento) | Alta | frontend | RF-13,RF-14 | ⏳ Da fare |
| BF-037 | Pagina Angular pianificazione monitoraggio (selezione aziende, data, percorso ottimizzato) | Alta | frontend | RF-17 | ⏳ Da fare |
| BF-038 | Pagina Angular dashboard KPI con grafici (ng2-charts / Chart.js) | Alta | frontend | RF-23,RF-24 | ⏳ Da fare |
| BF-039 | Home page post-login con box di riepilogo configurabili | Alta | frontend | RF-23 | ⏳ Da fare |

## S7 — Rifinitura UX e funzioni avanzate

| ID | Titolo | Priority | Label | RF | Note |
|---|---|---|---|---|---|
| BF-040 | Implementazione gestione ruoli e permessi nel FE (menu e azioni condizionali) | Alta | auth | RNF-03 | ⏳ Da fare |
| BF-041 | Pagina Angular invio email ad hoc ad aziende/allievi | Media | frontend | RF-21 | ⏳ Da fare |
| BF-042 | Responsive layout per tablet (Angular breakpoints) | Media | frontend | RNF-04 | ⏳ Da fare |
| BF-043 | Upload documento digitale da frontend con API multipart | Media | frontend | RF-15 | ⏳ Da fare |

## S8 — Testing & Deploy

| ID | Titolo | Priority | Label | RF | Note |
|---|---|---|---|---|---|
| BF-044 | Configurazione Swagger/OpenAPI per documentazione API | Alta | docs | - | ⏳ Da fare |
| BF-045 | Test unitari service layer Spring Boot (JUnit, Mockito) >70% coverage | Alta | test | - | ⏳ Da fare |
| BF-046 | Test E2E Angular con Cypress per flussi principali | Media | test | - | ⏳ Da fare |
| BF-047 | Deploy su ambiente di staging e collaudo finale | Alta | devops | - | ⏳ Da fare |
| BF-048 | Bug fixing e ottimizzazione performance API (query N+1) | Alta | bug | - | ⏳ Da fare |

---

**Totale da fare: 14 issue**

### Priorità suggerita per il prossimo sprint:
1. **Alta priorità**: BF-040 (gestione ruoli FE), BF-032 (SMTP), BF-038 (Dashboard)
2. **Media priorità**: BF-011 (allievi senza stage), BF-036 (semaforo documenti)
3. **Testing & Documentation**: BF-044 (Swagger), BF-045 (Test unitari)
