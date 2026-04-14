-- Converte public.azienda.tipo da varchar a enum nativo PostgreSQL.
-- Dominio enum: MADRINA | NON_MADRINA
-- Script idempotente e rieseguibile.

DO $$
BEGIN
    -- Crea il tipo enum se non esiste.
    IF NOT EXISTS (
        SELECT 1
        FROM pg_type t
        JOIN pg_namespace n ON n.oid = t.typnamespace
        WHERE t.typname = 'tipo_azienda_enum'
          AND n.nspname = 'public'
    ) THEN
        CREATE TYPE public.tipo_azienda_enum AS ENUM ('MADRINA', 'NON_MADRINA');
    END IF;

    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = 'public'
          AND table_name = 'azienda'
          AND column_name = 'tipo'
    ) THEN

        -- Allinea eventuali valori legacy prima della conversione tipo.
        UPDATE public.azienda
        SET tipo = 'NON_MADRINA'
        WHERE UPPER(COALESCE(tipo, '')) IN ('', 'NORMALE');

        -- Rimuove il check legacy: da qui in poi il vincolo è l'enum nativo.
        IF EXISTS (
            SELECT 1
            FROM pg_constraint
            WHERE conname = 'azienda_tipo_check'
              AND conrelid = 'public.azienda'::regclass
        ) THEN
            ALTER TABLE public.azienda DROP CONSTRAINT azienda_tipo_check;
        END IF;

        -- Converte la colonna solo se non è già enum nativo.
        IF EXISTS (
            SELECT 1
            FROM information_schema.columns
            WHERE table_schema = 'public'
              AND table_name = 'azienda'
              AND column_name = 'tipo'
              AND data_type <> 'USER-DEFINED'
        ) OR EXISTS (
            SELECT 1
            FROM information_schema.columns
            WHERE table_schema = 'public'
              AND table_name = 'azienda'
              AND column_name = 'tipo'
              AND data_type = 'USER-DEFINED'
              AND udt_name <> 'tipo_azienda_enum'
        ) THEN
            ALTER TABLE public.azienda
                ALTER COLUMN tipo DROP DEFAULT;

            ALTER TABLE public.azienda
                ALTER COLUMN tipo TYPE public.tipo_azienda_enum
                USING UPPER(tipo)::public.tipo_azienda_enum;
        END IF;

        ALTER TABLE public.azienda
            ALTER COLUMN tipo SET DEFAULT 'NON_MADRINA'::public.tipo_azienda_enum,
            ALTER COLUMN tipo SET NOT NULL;

    END IF;
END $$;
