ALTER TABLE public.azienda
    ADD COLUMN IF NOT EXISTS citta character varying(100);

CREATE INDEX IF NOT EXISTS idx_azienda_citta_lower
    ON public.azienda (LOWER(citta));