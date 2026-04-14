-- Rimuove le aziende fittizie dal dataset iniziale.
-- Elimina i record con id tra 1 e 15 gestendo prima i riferimenti FK.

-- Se presente la colonna introdotta dalla migration delle aziende madrine,
-- azzera il riferimento per evitare violazioni FK in fase di delete.
DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = 'public'
          AND table_name = 'corso'
          AND column_name = 'id_azienda_madrina'
    ) THEN
        UPDATE public.corso
        SET id_azienda_madrina = NULL
        WHERE id_azienda_madrina BETWEEN 1 AND 15;
    END IF;
END $$;

-- Elimina dipendenze dal tirocinio prima del delete aziende.
DELETE FROM public.documento_tirocinio
WHERE tirocinio_id IN (
    SELECT id
    FROM public.tirocinio
    WHERE azienda_id BETWEEN 1 AND 15
);

DELETE FROM public.monitoraggio
WHERE tirocinio_id IN (
    SELECT id
    FROM public.tirocinio
    WHERE azienda_id BETWEEN 1 AND 15
);

DELETE FROM public.tirocinio
WHERE azienda_id BETWEEN 1 AND 15;

-- Contatti aziendali collegati alle aziende fittizie.
DELETE FROM public.contatto_aziendale
WHERE azienda_id BETWEEN 1 AND 15;

-- Colloqui: la FK è ON DELETE CASCADE, ma la delete esplicita
-- mantiene la migration chiara e idempotente.
DELETE FROM public.colloquio_tirocinio
WHERE azienda_id BETWEEN 1 AND 15;

-- Delete finale aziende fittizie.
DELETE FROM public.azienda
WHERE id BETWEEN 1 AND 15;
