ALTER TABLE public.corso
    ADD COLUMN IF NOT EXISTS id_azienda_madrina integer;

CREATE INDEX IF NOT EXISTS idx_corso_id_azienda_madrina
    ON public.corso (id_azienda_madrina);

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'fk_corso_azienda_madrina'
          AND conrelid = 'public.corso'::regclass
    ) THEN
        ALTER TABLE public.corso
            ADD CONSTRAINT fk_corso_azienda_madrina
            FOREIGN KEY (id_azienda_madrina)
            REFERENCES public.azienda (id)
            ON UPDATE CASCADE
            ON DELETE SET NULL;
    END IF;
END $$;
