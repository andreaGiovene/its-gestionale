# Implementazioni del 2026-04-10: uniformità UI per pulsanti e tabelle

## Obiettivo
Rendere coerente l'esperienza utente tra componenti Bootstrap-like e Angular Material, riducendo le differenze visive tra pulsanti, tabelle e azioni nelle liste.

## Interventi principali

### 1) Pulsanti globali uniformati
- Introdotti token condivisi in `src/styles.scss` per i colori azione, il radius e l'altezza minima dei pulsanti.
- Allineate le varianti Bootstrap-like (`.btn-primary`, `.btn-secondary`, `.btn-info`, `.btn-danger`, `.btn-outline-secondary`) a uno stile comune.
- Aggiornati anche i pulsanti Angular Material principali per aderire agli stessi token visivi.
- Uniformati stati `hover`, `focus-visible` e `disabled`.

### 2) Rimozione degli override locali sui detail
- Eliminati gli stili locali dei pulsanti nei detail di:
  - aziende
  - allievi
  - corsi
- Rimasti solo gli aggiustamenti strutturali minimi, come la larghezza minima dei pulsanti nelle aree azione.

### 3) Uniformità delle tabelle
- Aggiunto uno stile tabellare globale condiviso in `src/styles.scss`.
- Standardizzati:
  - bordo e radius della tabella
  - intestazione con sfondo dedicato
  - padding delle celle
  - hover delle righe
  - allineamento verticale del contenuto

### 4) Spaziatura coerente tra i bottoni nelle tabelle
- Definita una spaziatura uniforme tra pulsanti adiacenti nelle celle azione.
- Rimosse le vecchie regole locali `margin-right` dalle liste di:
  - aziende
  - allievi
  - corsi
- Il risultato è una distanza coerente tra pulsanti senza duplicazioni di stile per schermata.

## File toccati
- WebApplication/Frontend/gestionale-frontend/src/styles.scss
- WebApplication/Frontend/gestionale-frontend/src/app/features/aziende/azienda-detail/azienda-detail.scss
- WebApplication/Frontend/gestionale-frontend/src/app/features/allievi/allievo-detail/allievo-detail.scss
- WebApplication/Frontend/gestionale-frontend/src/app/features/corsi/corso-detail/corso-detail.scss
- WebApplication/Frontend/gestionale-frontend/src/app/features/tirocini/tirocini-placeholder/tirocini-placeholder.scss
- WebApplication/Frontend/gestionale-frontend/src/app/features/colloqui/colloqui-placeholder/colloqui-placeholder.scss
- WebApplication/Frontend/gestionale-frontend/src/app/features/aziende/aziende-list/aziende-list.scss
- WebApplication/Frontend/gestionale-frontend/src/app/features/allievi/allievi-list/allievi-list.scss
- WebApplication/Frontend/gestionale-frontend/src/app/features/corsi/corsi-list/corsi-list.scss

## Verifica
- Build frontend eseguita con successo in configurazione development.
- Nessun errore rilevato nei file aggiornati.

## Nota operativa
- La scelta di centralizzare i token e le regole base in `src/styles.scss` evita divergenze future tra componenti Material e componenti con classi Bootstrap-like.