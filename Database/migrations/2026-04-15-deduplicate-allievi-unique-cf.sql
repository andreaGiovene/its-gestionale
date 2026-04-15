-- Rimuove i duplicati nella tabella allievo e aggiunge un vincolo UNIQUE
-- sul codice_fiscale per prevenire inserimenti duplicati in futuro.
--
-- Strategia: per ogni gruppo di duplicati (stesso codice_fiscale), si conserva
-- il record con id minore e si eliminano tutti gli altri, a condizione che
-- i duplicati non abbiano FK dipendenti (tirocini, colloqui).
-- Script idempotente e rieseguibile.

BEGIN;

-- Sgancia i duplicati dai tirocini (sposta il riferimento al record originale).
UPDATE public.tirocinio t
SET allievo_id = keeper.id
FROM (
    SELECT DISTINCT ON (codice_fiscale) id, codice_fiscale
    FROM public.allievo
    ORDER BY codice_fiscale, id ASC
) AS keeper
JOIN public.allievo dup ON dup.codice_fiscale = keeper.codice_fiscale AND dup.id <> keeper.id
WHERE t.allievo_id = dup.id;

-- Sgancia i duplicati dai colloqui (sposta il riferimento al record originale).
UPDATE public.colloquio_tirocinio ct
SET allievo_id = keeper.id
FROM (
    SELECT DISTINCT ON (codice_fiscale) id, codice_fiscale
    FROM public.allievo
    ORDER BY codice_fiscale, id ASC
) AS keeper
JOIN public.allievo dup ON dup.codice_fiscale = keeper.codice_fiscale AND dup.id <> keeper.id
WHERE ct.allievo_id = dup.id;

-- Elimina i duplicati: per ogni codice_fiscale conserva solo il record con id minore.
DELETE FROM public.allievo
WHERE id IN (
    SELECT id
    FROM (
        SELECT id,
               ROW_NUMBER() OVER (PARTITION BY codice_fiscale ORDER BY id ASC) AS rn
        FROM public.allievo
        WHERE codice_fiscale IS NOT NULL
    ) ranked
    WHERE rn > 1
);

-- Aggiunge il vincolo UNIQUE su codice_fiscale (idempotente).
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint
        WHERE conname = 'uq_allievo_codice_fiscale'
          AND conrelid = 'public.allievo'::regclass
    ) THEN
        ALTER TABLE public.allievo
            ADD CONSTRAINT uq_allievo_codice_fiscale UNIQUE (codice_fiscale);
    END IF;
END $$;

COMMIT;
