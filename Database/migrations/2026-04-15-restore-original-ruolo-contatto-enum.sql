-- Ripristina i valori business originari di ruolo_contatto_enum.
DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM pg_type t
        WHERE t.typnamespace = 'public'::regnamespace
          AND t.typname = 'ruolo_contatto_enum'
    ) THEN
        -- Mapping da eventuali label legacy/temporanee ai valori standard applicativi.
        IF EXISTS (
            SELECT 1 FROM pg_enum e JOIN pg_type t ON t.oid = e.enumtypid
            WHERE t.typname = 'ruolo_contatto_enum' AND e.enumlabel = 'HR'
        ) THEN
            ALTER TYPE public.ruolo_contatto_enum RENAME VALUE 'HR' TO 'TITOLARE';
        END IF;

        IF EXISTS (
            SELECT 1 FROM pg_enum e JOIN pg_type t ON t.oid = e.enumtypid
            WHERE t.typname = 'ruolo_contatto_enum' AND e.enumlabel = 'Tutor IT'
        ) THEN
            ALTER TYPE public.ruolo_contatto_enum RENAME VALUE 'Tutor IT' TO 'DIRETTORE';
        END IF;

        IF EXISTS (
            SELECT 1 FROM pg_enum e JOIN pg_type t ON t.oid = e.enumtypid
            WHERE t.typname = 'ruolo_contatto_enum' AND e.enumlabel = 'Manager'
        ) THEN
            ALTER TYPE public.ruolo_contatto_enum RENAME VALUE 'Manager' TO 'RESPONSABILE_HR';
        END IF;

        IF EXISTS (
            SELECT 1 FROM pg_enum e JOIN pg_type t ON t.oid = e.enumtypid
            WHERE t.typname = 'ruolo_contatto_enum' AND e.enumlabel = 'AI Specialist'
        ) THEN
            ALTER TYPE public.ruolo_contatto_enum RENAME VALUE 'AI Specialist' TO 'RESPONSABILE_TIROCINI';
        END IF;

        IF EXISTS (
            SELECT 1 FROM pg_enum e JOIN pg_type t ON t.oid = e.enumtypid
            WHERE t.typname = 'ruolo_contatto_enum' AND e.enumlabel = 'Data Analyst'
        ) THEN
            ALTER TYPE public.ruolo_contatto_enum RENAME VALUE 'Data Analyst' TO 'TUTOR_AZIENDALE';
        END IF;

        -- Se in ambienti intermedi erano già presenti gli underscore, riallinea comunque.
        IF EXISTS (
            SELECT 1 FROM pg_enum e JOIN pg_type t ON t.oid = e.enumtypid
            WHERE t.typname = 'ruolo_contatto_enum' AND e.enumlabel = 'TUTOR_IT'
        ) THEN
            ALTER TYPE public.ruolo_contatto_enum RENAME VALUE 'TUTOR_IT' TO 'DIRETTORE';
        END IF;

        IF EXISTS (
            SELECT 1 FROM pg_enum e JOIN pg_type t ON t.oid = e.enumtypid
            WHERE t.typname = 'ruolo_contatto_enum' AND e.enumlabel = 'AI_SPECIALIST'
        ) THEN
            ALTER TYPE public.ruolo_contatto_enum RENAME VALUE 'AI_SPECIALIST' TO 'RESPONSABILE_TIROCINI';
        END IF;

        IF EXISTS (
            SELECT 1 FROM pg_enum e JOIN pg_type t ON t.oid = e.enumtypid
            WHERE t.typname = 'ruolo_contatto_enum' AND e.enumlabel = 'DATA_ANALYST'
        ) THEN
            ALTER TYPE public.ruolo_contatto_enum RENAME VALUE 'DATA_ANALYST' TO 'TUTOR_AZIENDALE';
        END IF;
    END IF;
END $$;
