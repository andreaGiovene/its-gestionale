# 📖 Guida al Sistema di Tracking delle Issue

Questa cartella contiene l'organizzazione e il tracking di tutte le issue del progetto **Gestionale ITS**.

## 📁 Struttura dei File

### `BF-issues.md`
📋 **File principale e di riferimento**
- Elenco completo di tutte le 48 issue organizzate per sezione
- Contiene la roadmap integrale del progetto
- Organizzato per 8 sezioni strategiche (S1-S8)
- **Utile per**: Consultazione generale, pianificazione a livello di progetto

### `BF-COMPLETED.md`
✅ **Issue completate**
- Issue già finite e testate
- 34 issue completate (71% del totale)
- Organizzate per sezione di appartenenza
- Contiene note su particolarità di implementazione
- **Utile per**: Verificare cosa è stato già fatto, evitare duplicazioni

### `BF-IN-PROGRESS.md`
🚀 **Issue attualmente in sviluppo**
- Issue che qualcuno sta attualmente implementando
- Contiene assegnazione a developer e percentuale di progresso
- Aggiornare quando si inizia una nuova issue
- **Utile per**: Coordinamento team, evitare conflitti su stessa issue

### `BF-BACKLOG.md`
📋 **Issue da completare**
- Issue ancora da fare (14 rimanenti)
- 29% del totale del progetto
- Organizzate per priorità (Alta, Media)
- Include suggerimenti per il prossimo sprint
- **Utile per**: Pianificazione sprint, selezione task

## 🔄 Workflow di Gestione

### Quando inizi una nuova issue:

```
BF-BACKLOG.md → BF-IN-PROGRESS.md → BF-COMPLETED.md
```

1. **Seleziona dalla lista di BF-BACKLOG.md**
2. **Sposta a BF-IN-PROGRESS.md** e aggiungi:
   - Nome del developer assegnato
   - Data inizio
   - Link al branch Git (opzionale)
3. **Aggiorna la riga** con percentuale di progresso
4. **Una volta completata**:
   - Aggiungi note di implementazione
   - Sposta a BF-COMPLETED.md
   - Crea tag/milestone su GitHub

## 📊 Statistiche Progetto

| Stato | Count | % |
|---|---|---|
| ✅ Completate | 34 | **71%** |
| 🚀 In corso | 0 | 0% |
| ⏳ Da fare | 14 | **29%** |
| **TOTALE** | **48** | **100%** |

## 🎯 Sezioni Strategiche

1. **S1 — Setup & Architettura** (7 issue)
   - ✅ Completate: 7/7
   - Stato: **COMPLETO**

2. **S2 — API Corsi & Allievi** (6 issue)
   - ✅ Completate: 4/6
   - ⏳ Da fare: 2/6 (import Excel)

3. **S3 — API Aziende + FE Allievi/Aziende** (8 issue)
   - ✅ Completate: 8/8
   - Stato: **COMPLETO**

4. **S4 — API Colloqui + FE Colloqui** (5 issue)
   - ✅ Completate: 5/5
   - Stato: **COMPLETO**

5. **S5 — API Tirocini, Documenti, Monitoraggi** (7 issue)
   - ✅ Completate: 4/7
   - ⏳ Da fare: 3/7 (SMTP, email)

6. **S6 — FE Tirocini, Documenti, Monitoraggi, Dashboard** (5 issue)
   - ✅ Completate: 1/5
   - ⏳ Da fare: 4/5 (dashboard, monitoring UI)

7. **S7 — Rifinitura UX e funzioni avanzate** (4 issue)
   - ✅ Completate: 0/4
   - ⏳ Da fare: 4/4 (ruoli FE, email, responsive)

8. **S8 — Testing & Deploy** (5 issue)
   - ✅ Completate: 0/5
   - ⏳ Da fare: 5/5 (testing, deploy)

## 🎨 Label dei File

- 📋 `BF-issues.md` — Referenza principale
- ✅ `BF-COMPLETED.md` — Done
- 🚀 `BF-IN-PROGRESS.md` — WIP
- ⏳ `BF-BACKLOG.md` — Todo

## 💡 Note Importanti

### Priority Levels
- **Alta**: Bloccante, deve essere fatta
- **Media**: Importante ma può attendere

### Label Categories
- `setup` — Configurazione iniziale
- `auth` — Autenticazione e autorizzazione
- `backend` — API e logica server
- `frontend` — UI e componenti Angular
- `docs` — Documentazione
- `test` — Test e QA
- `devops` — Deploy e infrastruttura
- `bug` — Bug fixing

### RF (Requisiti Funzionali)
Referenza ai requisiti specifici del capitolato progettuale

---

**Ultimo aggiornamento**: 23 Aprile 2026
**Versione**: 1.0
