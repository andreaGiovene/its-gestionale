ALTER TABLE public.corso
    ADD COLUMN IF NOT EXISTS id_azienda_madrina integer;

CREATE INDEX IF NOT EXISTS idx_corso_id_azienda_madrina
    ON public.corso (id_azienda_madrina);

ALTER TABLE public.corso
    ADD CONSTRAINT fk_corso_azienda_madrina
    FOREIGN KEY (id_azienda_madrina)
    REFERENCES public.azienda (id)
    ON UPDATE CASCADE
    ON DELETE SET NULL;
