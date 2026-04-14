-- Normalizza i valori di public.azienda.tipo al dominio binario:
-- MADRINA | NON_MADRINA
--
-- Regola:
-- - MADRINA: azienda associata ad almeno un corso tramite corso.id_azienda_madrina
-- - NON_MADRINA: azienda non associata a corsi madrini
--
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

      -- Il vincolo legacy consentiva NORMALE: lo rimuoviamo per poter migrare i dati.
      IF EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'azienda_tipo_check'
          AND conrelid = 'public.azienda'::regclass
      ) THEN
        ALTER TABLE public.azienda DROP CONSTRAINT azienda_tipo_check;
      END IF;

        UPDATE public.azienda a
        SET tipo = 'MADRINA'
        WHERE EXISTS (
            SELECT 1
            FROM public.corso c
            WHERE c.id_azienda_madrina = a.id
        )
          AND UPPER(COALESCE(a.tipo, '')) <> 'MADRINA';

        UPDATE public.azienda a
        SET tipo = 'NON_MADRINA'
        WHERE NOT EXISTS (
            SELECT 1
            FROM public.corso c
            WHERE c.id_azienda_madrina = a.id
        )
          AND UPPER(COALESCE(a.tipo, '')) <> 'NON_MADRINA';

        -- Reintroduciamo il vincolo allineato al nuovo dominio binario.
        ALTER TABLE public.azienda
          ADD CONSTRAINT azienda_tipo_check
          CHECK (UPPER(COALESCE(tipo, '')) IN ('MADRINA', 'NON_MADRINA'));

    END IF;
END $$;
