-- Allinea schema DB all'entity ContattoAziendale aggiungendo il collegamento opzionale all'utente.

ALTER TABLE public.contatto_aziendale
    ADD COLUMN IF NOT EXISTS id_utente integer;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'contatto_aziendale_id_utente_key'
          AND conrelid = 'public.contatto_aziendale'::regclass
    ) THEN
        ALTER TABLE public.contatto_aziendale
            ADD CONSTRAINT contatto_aziendale_id_utente_key UNIQUE (id_utente);
    END IF;
END$$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'fk_contatto_utente'
          AND conrelid = 'public.contatto_aziendale'::regclass
    ) THEN
        ALTER TABLE public.contatto_aziendale
            ADD CONSTRAINT fk_contatto_utente
            FOREIGN KEY (id_utente)
            REFERENCES public.utente(id_utente)
            ON UPDATE CASCADE
            ON DELETE SET NULL;
    END IF;
END$$;
