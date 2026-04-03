# BF-002 - Verifica completamento struttura frontend Angular

Data verifica: 2026-04-03

## Esito criteri di accettazione

1. Progetto Angular avviabile in locale senza errori
- Stato: PARZIALMENTE OK
- Evidenze:
  - Build frontend completata con successo (`npm run build`).
  - Presente warning budget bundle iniziale (715.39 kB > 500 kB) da trattare come miglioramento tecnico.

2. Struttura cartelle coerente con approccio Core/Shared/Feature
- Stato: OK
- Evidenze:
  - `src/app/core` presente con guard/interceptor/services.
  - `src/app/shared` presente con componenti layout e modelli condivisi.
  - `src/app/features` presente con aree `auth`, `dashboard`, `corsi`, `aziende`.

3. Routing base funzionante
- Stato: PARZIALMENTE OK
- Evidenze:
  - Routing principale con login + area autenticata + children su dashboard/corsi/aziende.
  - Criticita: il menu layout espone rotte non ancora registrate (`/allievi`, `/colloqui`, `/tirocini`), con possibile redirect inatteso via wildcard.

4. Presenza di almeno un componente condiviso di layout
- Stato: OK
- Evidenze:
  - `LayoutComponent` in `src/app/shared/components/layout` con shell riusabile e `router-outlet` interno.

5. README aggiornato con la nuova struttura frontend
- Stato: PARZIALMENTE OK
- Evidenze:
  - README frontend aggiornato con sezioni Core/Features/Shared.
  - Disallineamento documentale: viene citata la feature `allievi` come disponibile nella struttura, ma non esiste ancora routing/componente feature dedicato nel frontend corrente.

## Verifica coerenza con backend (anti-disallineamento)

Allineamenti confermati:
- Frontend `CorsoService` usa base URL `/api/corsi`, coerente con `CorsoController` backend.
- Frontend `AziendaService` usa base URL `/api/aziende`, coerente con `AziendaController` backend.
- Modelli frontend `Corso`/`Azienda` coerenti con i campi DTO backend attuali.

Gap da monitorare:
- Backend espone gia API `allievi` ma frontend non ha ancora feature routing/pagine allievi, nonostante voce menu presente nel layout.

## Piano d'azione completamento BF-002

Priorita P1 (subito)
1. Allineare navigazione e routing frontend:
   - Opzione A (rapida): nascondere dal menu voci non implementate (`allievi`, `colloqui`, `tirocini`).
   - Opzione B (preferibile): introdurre route placeholder "In lavorazione" per evitare redirect silenziosi.
2. Aggiornare README frontend in modo aderente allo stato reale delle feature disponibili.
3. Aggiungere check automatico di smoke test routing (almeno navigazione login -> dashboard -> corsi/aziende).

Priorita P2 (breve termine)
1. Introdurre lazy loading per feature area (`corsi`, `aziende`, `allievi`) per rafforzare la scalabilita architetturale.
2. Centralizzare URL API in configurazione ambiente (`environment.ts`) per evitare hardcode e futuri drift ambientali.
3. Definire contratto FE/BE versionato per endpoint e DTO (tabella mapping in docs).

Priorita P3 (hardening)
1. Ridurre warning bundle budget (ottimizzazione import Material, splitting, analisi chunk).
2. Stabilizzare strategia test frontend (Vitest/Karma) e integrare nel flusso CI locale.
3. Aggiungere checklist di coerenza FE/BE per ogni nuova feature verticale.

## Decisione consigliata

Per chiudere BF-002 in modo pulito: completare P1 prima di aprire le feature successive, cosi da mantenere coerenza funzionale e documentale tra frontend e backend.