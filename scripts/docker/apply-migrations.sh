#!/usr/bin/env bash
set -euo pipefail

echo "[initdb] Applying SQL migrations..."

migrations=(
  "/docker-entrypoint-migrations/2026-04-03-drop-utente-username.sql"
  "/docker-entrypoint-migrations/2026-04-08-add-allievo-telefono.sql"
  "/docker-entrypoint-migrations/2026-04-09-delete-corsi-fittizi.sql"
  "/docker-entrypoint-migrations/2026-04-09-seed-corsi-realistici.sql"
  "/docker-entrypoint-migrations/2026-04-14-add-azienda-madrina-to-corso.sql"
  "/docker-entrypoint-migrations/2026-04-14-add-citta-to-azienda.sql"
  "/docker-entrypoint-migrations/2026-04-14-add-tipo-to-azienda.sql"
  "/docker-entrypoint-migrations/2026-04-14-align-tipo-azienda-with-corsi-madrini.sql"
  "/docker-entrypoint-migrations/2026-04-14-delete-aziende-fittizie.sql"
  "/docker-entrypoint-migrations/2026-04-14-seed-aziende-madrine-2025-2027.sql"
  "/docker-entrypoint-migrations/2026-04-14-finalize-azienda-tipo-native-pg-enum.sql"
  "/docker-entrypoint-migrations/2026-04-14-normalize-tipo-azienda-madrina-non_madrina.sql"
  "/docker-entrypoint-migrations/2026-04-14-finalize-azienda-tipo-enum-contract.sql"
  "/docker-entrypoint-migrations/2026-04-15-restore-original-ruolo-contatto-enum.sql"
  "/docker-entrypoint-migrations/2026-04-15-deduplicate-allievi-unique-cf.sql"
  "/docker-entrypoint-migrations/2026-04-17-add-id-utente-to-contatto-aziendale.sql"
  "/docker-entrypoint-migrations/2026-04-21-split-stato-esito-colloquio-tirocinio.sql"
)

for migration in "${migrations[@]}"; do
  if [[ ! -f "$migration" ]]; then
    echo "[initdb] Missing migration: $migration" >&2
    exit 1
  fi

  echo "[initdb] -> $(basename "$migration")"
  psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" -f "$migration"
done

echo "[initdb] Migrations applied successfully."
