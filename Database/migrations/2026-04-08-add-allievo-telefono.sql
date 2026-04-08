-- Allinea lo schema DB all'entita Allievo usata dal backend.
-- Necessario per evitare errori 500 sugli endpoint /api/allievi e /api/corsi.

ALTER TABLE allievo
    ADD COLUMN IF NOT EXISTS telefono VARCHAR(20);
