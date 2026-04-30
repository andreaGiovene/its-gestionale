-- Conversione colonna tipo tirocinio a enum
-- Crea l'enum tipo_tirocinio_enum se non esiste
DO $$ 
BEGIN 
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'tipo_tirocinio_enum') THEN 
        CREATE TYPE tipo_tirocinio_enum AS ENUM ('STAGE', 'ALTO_APPRENDISTATO');
    END IF; 
END $$;

-- Altera la colonna tipo nella tabella tirocinio
ALTER TABLE public.tirocinio
ALTER COLUMN tipo SET DATA TYPE tipo_tirocinio_enum USING tipo::tipo_tirocinio_enum;

-- Aggiungi NOT NULL se necessario (solo se tutti i tirocini ne hanno uno)
-- ALTER TABLE public.tirocinio ALTER COLUMN tipo SET NOT NULL;
