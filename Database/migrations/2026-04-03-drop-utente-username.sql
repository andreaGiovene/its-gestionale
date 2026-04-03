-- Migrazione manuale per rimuovere username dal dominio Utente.
-- Da eseguire sugli ambienti che hanno gia' la colonna utente.username.

ALTER TABLE utente
    DROP COLUMN IF EXISTS username;
