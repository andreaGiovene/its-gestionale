-- Reseed allievi con il dataset aggiornato fornito manualmente.
-- Script idempotente: inserisce/aggiorna per codice_fiscale.
-- I codici fiscali sono fittizi, stabili e univoci.

BEGIN;

INSERT INTO public.allievo (nome, cognome, codice_fiscale, corso_id, note, data_di_nascita, id_utente)
SELECT s.nome,
       s.cognome,
       s.codice_fiscale,
       98,
       NULL,
       NULL,
       NULL
FROM (
    VALUES
        ('Leonardo', 'Agnese', 'AGNLEO001'),
        ('Marco', 'Arneodo', 'ARNMAR002'),
        ('Youssef', 'Badr', 'BADYOU003'),
        ('Fabio', 'Blanna', 'BLAFAB004'),
        ('Andrea', 'Brecko', 'BREAND005'),
        ('Walter', 'Cavaliere', 'CAVWAL006'),
        ('Andrea', 'Cerrato', 'CERAND007'),
        ('Andrea', 'De Giorgi', 'DEGAND008'),
        ('Alessio', 'Del Monte', 'DELALE009'),
        ('Michelangelo', 'Delfino', 'DELMIC010'),
        ('Stefano', 'Demaria', 'DEMSTE011'),
        ('Giorgio', 'Formiggini', 'FORGIO012'),
        ('Sara', 'Gelli', 'GELSAR013'),
        ('Andrea', 'Giovene', 'GIOAND014'),
        ('Ashna', 'Kaur', 'KAUASH015'),
        ('Andrea', 'Lergo', 'LERAND016'),
        ('David', 'Mancin', 'MANDAV017'),
        ('Mattia', 'Martinelli', 'MARMAT018'),
        ('Matteo', 'Mazzoni', 'MAZMAT019'),
        ('Federico', 'Molner', 'MOLFED020'),
        ('Giada', 'Perno', 'PERGIA021'),
        ('Riccardo', 'Primomo', 'PRIRIC022'),
        ('Noemi', 'Renna', 'RENNOE023'),
        ('Alessia', 'Sanfilippo', 'SANALE024'),
        ('Valentina', 'Slaviero', 'SLAVAL025'),
        ('Robert Ilie', 'Smau', 'SMAROB026'),
        ('Elia', 'Sollazzo', 'SOLELI027'),
        ('Bayo Glalganie Clevatte', 'Tchissambou', 'TCHBAY028'),
        ('Romina', 'Trazzi', 'TRAROM029'),
        ('Samuele', 'Vollero', 'VOLSAM030')
) AS s(nome, cognome, codice_fiscale)
ON CONFLICT (codice_fiscale)
DO UPDATE
SET nome = EXCLUDED.nome,
    cognome = EXCLUDED.cognome,
    corso_id = EXCLUDED.corso_id,
    data_di_nascita = EXCLUDED.data_di_nascita;

COMMIT;