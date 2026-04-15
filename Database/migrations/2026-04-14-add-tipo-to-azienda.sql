DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_type t
        JOIN pg_namespace n ON n.oid = t.typnamespace
        WHERE t.typname = 'tipo_azienda_enum'
          AND n.nspname = 'public'
    ) THEN
        CREATE TYPE public.tipo_azienda_enum AS ENUM ('MADRINA', 'NON_MADRINA');
    END IF;

    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = 'public'
          AND table_name = 'azienda'
          AND column_name = 'tipo'
    ) THEN
        ALTER TABLE public.azienda
            ADD COLUMN tipo public.tipo_azienda_enum NOT NULL DEFAULT 'NON_MADRINA'::public.tipo_azienda_enum;
    ELSE
        UPDATE public.azienda
        SET tipo = COALESCE(tipo, 'NON_MADRINA'::public.tipo_azienda_enum);

        ALTER TABLE public.azienda
            ALTER COLUMN tipo SET DEFAULT 'NON_MADRINA'::public.tipo_azienda_enum,
            ALTER COLUMN tipo SET NOT NULL;
    END IF;
END $$;