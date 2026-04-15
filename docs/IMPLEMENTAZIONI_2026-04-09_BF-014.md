# Implementazioni del 2026-04-09 (BF-014)

## Commit di riferimento
- `583dedc` - feat: add filtering functionality to CorsiList component with UI enhancements

## Obiettivo
Implementare nella pagina lista corsi una UX di filtro/ricerca completa, chiara e testabile manualmente, mantenendo il caricamento dati da `GET /api/corsi`.

## File modificati nell'ultimo commit
- WebApplication/Frontend/gestionale-frontend/src/app/features/corsi/corsi-list/corsi-list.ts
- WebApplication/Frontend/gestionale-frontend/src/app/features/corsi/corsi-list/corsi-list.html
- WebApplication/Frontend/gestionale-frontend/src/app/features/corsi/corsi-list/corsi-list.scss
- WebApplication/Frontend/gestionale-frontend/tsconfig.app.json

## Implementazioni introdotte

### 1) Logica filtri lato componente (`corsi-list.ts`)
- Pattern a due array:
  - `allCorsi` come sorgente completa dati (source of truth)
  - `filteredCorsi` come lista visualizzata
- Aggiunti stati filtro:
  - `selectedNomeCorso`
  - `searchTerm` (ricerca testuale nome)
  - `selectedStato`
  - `selectedAnnoAccademico`
- Aggiunti getter per valori unici e ordinati:
  - `nomiCorsiDisponibili`
  - `statiDisponibili`
  - `anniDisponibili`
- Implementato `applyFilters()` con composizione filtri in AND:
  - nome da dropdown (match esatto)
  - nome da testo libero (contains, case-insensitive)
  - stato (match esatto)
  - anno accademico (match esatto)
- Implementato `resetFilters()` con azzeramento stato filtri e refresh lista.

### 2) UI filtri (`corsi-list.html`)
- Aggiunto pannello filtri in pagina con controlli dedicati:
  - `Nome corso` (dropdown completo)
  - `Ricerca testuale nome` (input + pulsante Cerca + Enter)
  - `Stato` (dropdown)
  - `Anno accademico` (dropdown)
  - `Reset filtri` (pulsante)
- Lista tabellare legata a `filteredCorsi`.
- Empty state differenziato:
  - nessun dato disponibile
  - nessun risultato con filtri attivi

### 3) Allineamento e responsive (`corsi-list.scss`)
- Definito layout a griglia per i filtri con breakpoints responsive.
- Uniformata altezza dei controlli e allineamento pulsanti.
- Sistemata la barra di ricerca testuale (`search-row`) per input + bottone Cerca.
- Migliorata coerenza visiva del pannello filtri.

### 4) Configurazione TypeScript app (`tsconfig.app.json`)
- Normalizzata configurazione `compilerOptions` in linea con il setup Angular/TS del progetto.

## Criteri di accettazione coperti
- Dati caricati tramite API backend (`GET /api/corsi`).
- Ricerca per nome corso disponibile e operativa.
- Filtri stato/anno accademico funzionanti e combinabili.
- Presenza pulsante reset filtri.
- UI filtri chiara e usabile (desktop/mobile).
- Funzionalita testabile manualmente in pagina `/corsi`.

## Note operative
- I filtri vengono applicati lato client, scelta coerente con volume dati ridotto atteso.
- La struttura a due array evita nuove chiamate API ad ogni modifica filtro.
