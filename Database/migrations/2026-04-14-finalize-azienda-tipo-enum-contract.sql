-- Consolidamento finale contratto tipo azienda.
-- Obiettivo: assicurare dominio binario persistito (MADRINA | NON_MADRINA)
-- e allineamento con l'enum applicativo lato codice.
--
-- Nota: il tipo colonna resta character varying per compatibilita applicativa,
-- ma il vincolo rende il dominio equivalente a un enum applicativo.

DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = 'public'
          AND table_name = 'azienda'
          AND column_name = 'tipo'
    ) THEN

        -- Normalizzazione eventuali legacy values.
        UPDATE public.azienda
        SET tipo = 'NON_MADRINA'
        WHERE UPPER(COALESCE(tipo, '')) = 'NORMALE';

        UPDATE public.azienda
        SET tipo = 'NON_MADRINA'
        WHERE UPPER(COALESCE(tipo, '')) = '';

        -- Vincolo aggiornato e univoco rispetto al dominio applicativo.
        IF EXISTS (
            SELECT 1
            FROM pg_constraint
            WHERE conname = 'azienda_tipo_check'
              AND conrelid = 'public.azienda'::regclass
        ) THEN
            ALTER TABLE public.azienda DROP CONSTRAINT azienda_tipo_check;
        END IF;

        ALTER TABLE public.azienda
            ADD CONSTRAINT azienda_tipo_check
            CHECK (UPPER(tipo) IN ('MADRINA', 'NON_MADRINA'));

        -- Contratto forte: campo obbligatorio con default sicuro.
        ALTER TABLE public.azienda
            ALTER COLUMN tipo SET DEFAULT 'NON_MADRINA',
            ALTER COLUMN tipo SET NOT NULL;

    END IF;
END $$;
