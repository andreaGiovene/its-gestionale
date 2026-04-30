-- Reset completo del seed allievi.
-- Rimuove tutti i record esistenti e i relativi riferimenti nei domini dipendenti
-- per permettere al reseed aggiornato di ricostruire il dataset da zero.

BEGIN;

-- Elimina prima i riferimenti dai domini dipendenti per evitare violazioni FK.
DELETE FROM public.tirocinio t
USING public.allievo a
WHERE t.allievo_id = a.id
  AND a.id_utente IS NULL;

DELETE FROM public.colloquio_tirocinio ct
USING public.allievo a
WHERE ct.allievo_id = a.id
  AND a.id_utente IS NULL;

DELETE FROM public.caso_critico cc
USING public.allievo a
WHERE cc.allievo_id = a.id
  AND a.id_utente IS NULL;

-- Rimuove tutti gli allievi rimasti nel seed storico.
DELETE FROM public.allievo;

COMMIT;