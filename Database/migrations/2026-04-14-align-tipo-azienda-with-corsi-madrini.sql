-- Allinea il campo public.azienda.tipo alle relazioni reali con i corsi madrini.
-- Regola:
--   - tipo = 'MADRINA' se l'azienda e associata ad almeno un corso tramite corso.id_azienda_madrina
--   - tipo = 'NORMALE' negli altri casi
-- Script idempotente e rieseguibile.

DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = 'public'
          AND table_name = 'azienda'
          AND column_name = 'tipo'
    )
    AND EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = 'public'
          AND table_name = 'corso'
          AND column_name = 'id_azienda_madrina'
    ) THEN

        UPDATE public.azienda a
        SET tipo = 'MADRINA'
        WHERE EXISTS (
            SELECT 1
            FROM public.corso c
            WHERE c.id_azienda_madrina = a.id
        )
          AND UPPER(COALESCE(a.tipo, '')) <> 'MADRINA';

        UPDATE public.azienda a
        SET tipo = 'NORMALE'
        WHERE NOT EXISTS (
            SELECT 1
            FROM public.corso c
            WHERE c.id_azienda_madrina = a.id
        )
          AND UPPER(COALESCE(a.tipo, '')) <> 'NORMALE';

    END IF;
END $$;
