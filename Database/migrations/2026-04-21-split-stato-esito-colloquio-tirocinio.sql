-- Split enum legacy condiviso stato_esito in due enum di dominio separati.
-- Introduce inoltre enum nativo per colloquio_tirocinio.tipo_evento.
-- Script idempotente e rieseguibile.

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_type t
        JOIN pg_namespace n ON n.oid = t.typnamespace
        WHERE t.typname = 'stato_esito_colloquio_enum'
          AND n.nspname = 'public'
    ) THEN
        CREATE TYPE public.stato_esito_colloquio_enum AS ENUM (
            'IN_ATTESA',
            'POSITIVO',
            'NEGATIVO',
            'RITIRATO',
            'NON_PRESENTATO'
        );
    END IF;

    IF NOT EXISTS (
        SELECT 1
        FROM pg_type t
        JOIN pg_namespace n ON n.oid = t.typnamespace
        WHERE t.typname = 'stato_tirocinio_enum'
          AND n.nspname = 'public'
    ) THEN
        CREATE TYPE public.stato_tirocinio_enum AS ENUM (
            'IN_CORSO',
            'CONCLUSO',
            'INTERROTTO'
        );
    END IF;

    IF NOT EXISTS (
        SELECT 1
        FROM pg_type t
        JOIN pg_namespace n ON n.oid = t.typnamespace
        WHERE t.typname = 'tipo_evento_colloquio_enum'
          AND n.nspname = 'public'
    ) THEN
        CREATE TYPE public.tipo_evento_colloquio_enum AS ENUM (
            'MATCHING_DAY',
            'FUORI_MATCHING_DAY'
        );
    END IF;

    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = 'public'
          AND table_name = 'colloquio_tirocinio'
          AND column_name = 'esito'
    ) THEN
        ALTER TABLE public.colloquio_tirocinio
            ALTER COLUMN esito DROP DEFAULT;

        IF EXISTS (
            SELECT 1
            FROM information_schema.columns
            WHERE table_schema = 'public'
              AND table_name = 'colloquio_tirocinio'
              AND column_name = 'esito'
              AND (data_type <> 'USER-DEFINED' OR udt_name <> 'stato_esito_colloquio_enum')
        ) THEN
            ALTER TABLE public.colloquio_tirocinio
                ALTER COLUMN esito TYPE public.stato_esito_colloquio_enum
                USING (
                    CASE
                        WHEN esito IS NULL THEN 'IN_ATTESA'
                        WHEN esito::text IN ('Positivo', 'Approvato', 'POSITIVO', 'APPROVATO') THEN 'POSITIVO'
                        WHEN esito::text IN ('Negativo', 'Rifiutato', 'NEGATIVO', 'RIFIUTATO') THEN 'NEGATIVO'
                        WHEN esito::text IN ('In attesa', 'IN_ATTESA', 'IN ATTESA') THEN 'IN_ATTESA'
                        WHEN esito::text IN ('Ritirato', 'RITIRATO') THEN 'RITIRATO'
                        WHEN esito::text IN ('Non presentato', 'NON_PRESENTATO', 'NON PRESENTATO') THEN 'NON_PRESENTATO'
                        ELSE 'IN_ATTESA'
                    END
                )::public.stato_esito_colloquio_enum;
        END IF;

        ALTER TABLE public.colloquio_tirocinio
            ALTER COLUMN esito SET DEFAULT 'IN_ATTESA'::public.stato_esito_colloquio_enum,
            ALTER COLUMN esito SET NOT NULL;
    END IF;

    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = 'public'
          AND table_name = 'colloquio_tirocinio'
          AND column_name = 'tipo_evento'
    ) THEN
        IF EXISTS (
            SELECT 1
            FROM information_schema.columns
            WHERE table_schema = 'public'
              AND table_name = 'colloquio_tirocinio'
              AND column_name = 'tipo_evento'
              AND (data_type <> 'USER-DEFINED' OR udt_name <> 'tipo_evento_colloquio_enum')
        ) THEN
            ALTER TABLE public.colloquio_tirocinio
                ALTER COLUMN tipo_evento TYPE public.tipo_evento_colloquio_enum
                USING (
                    CASE
                        WHEN tipo_evento IS NULL THEN NULL
                        WHEN UPPER(tipo_evento) LIKE '%MATCHING%' OR UPPER(tipo_evento) LIKE '%RECRUITING%' OR UPPER(tipo_evento) LIKE '%DAY%'
                            THEN 'MATCHING_DAY'
                        ELSE 'FUORI_MATCHING_DAY'
                    END
                )::public.tipo_evento_colloquio_enum;
        END IF;
    END IF;

    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = 'public'
          AND table_name = 'tirocinio'
          AND column_name = 'esito'
    ) THEN
        ALTER TABLE public.tirocinio
            ALTER COLUMN esito DROP DEFAULT;

        IF EXISTS (
            SELECT 1
            FROM information_schema.columns
            WHERE table_schema = 'public'
              AND table_name = 'tirocinio'
              AND column_name = 'esito'
              AND (data_type <> 'USER-DEFINED' OR udt_name <> 'stato_tirocinio_enum')
        ) THEN
            ALTER TABLE public.tirocinio
                ALTER COLUMN esito TYPE public.stato_tirocinio_enum
                USING (
                    CASE
                        WHEN esito IS NULL THEN 'IN_CORSO'
                        WHEN esito::text IN ('In corso', 'IN_CORSO', 'IN CORSO', 'In attesa') THEN 'IN_CORSO'
                        WHEN esito::text IN ('Concluso', 'CONCLUSO', 'Positivo', 'POSITIVO', 'Approvato', 'APPROVATO') THEN 'CONCLUSO'
                        WHEN esito::text IN ('Interrotto', 'INTERROTTO', 'Negativo', 'NEGATIVO', 'Rifiutato', 'RIFIUTATO') THEN 'INTERROTTO'
                        ELSE 'IN_CORSO'
                    END
                )::public.stato_tirocinio_enum;
        END IF;

        ALTER TABLE public.tirocinio
            ALTER COLUMN esito SET DEFAULT 'IN_CORSO'::public.stato_tirocinio_enum,
            ALTER COLUMN esito SET NOT NULL;
    END IF;

    IF EXISTS (
        SELECT 1
        FROM pg_type t
        JOIN pg_namespace n ON n.oid = t.typnamespace
        WHERE t.typname = 'stato_esito'
          AND n.nspname = 'public'
    ) THEN
        IF NOT EXISTS (
            SELECT 1
            FROM pg_attribute a
            JOIN pg_class c ON c.oid = a.attrelid
            JOIN pg_namespace n ON n.oid = c.relnamespace
            JOIN pg_type t ON t.oid = a.atttypid
            WHERE n.nspname = 'public'
              AND t.typname = 'stato_esito'
              AND a.attnum > 0
              AND NOT a.attisdropped
        ) THEN
            DROP TYPE public.stato_esito;
        END IF;
    END IF;

    IF EXISTS (
        SELECT 1
        FROM pg_type t
        JOIN pg_namespace n ON n.oid = t.typnamespace
        WHERE t.typname = 'esito_colloquio'
          AND n.nspname = 'public'
    ) THEN
        IF NOT EXISTS (
            SELECT 1
            FROM pg_attribute a
            JOIN pg_class c ON c.oid = a.attrelid
            JOIN pg_namespace n ON n.oid = c.relnamespace
            JOIN pg_type t ON t.oid = a.atttypid
            WHERE n.nspname = 'public'
              AND t.typname = 'esito_colloquio'
              AND a.attnum > 0
              AND NOT a.attisdropped
        ) THEN
            DROP TYPE public.esito_colloquio;
        END IF;
    END IF;

    IF EXISTS (
        SELECT 1
        FROM pg_type t
        JOIN pg_namespace n ON n.oid = t.typnamespace
        WHERE t.typname = 'esito_tirocinio'
          AND n.nspname = 'public'
    ) THEN
        IF NOT EXISTS (
            SELECT 1
            FROM pg_attribute a
            JOIN pg_class c ON c.oid = a.attrelid
            JOIN pg_namespace n ON n.oid = c.relnamespace
            JOIN pg_type t ON t.oid = a.atttypid
            WHERE n.nspname = 'public'
              AND t.typname = 'esito_tirocinio'
              AND a.attnum > 0
              AND NOT a.attisdropped
        ) THEN
            DROP TYPE public.esito_tirocinio;
        END IF;
    END IF;
END $$;
