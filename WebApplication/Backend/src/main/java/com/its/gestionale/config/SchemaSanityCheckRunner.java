package com.its.gestionale.config;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Verifica minima di coerenza schema DB all'avvio applicazione.
 *
 * <p>Obiettivo: intercettare mismatch schema/entity (es. colonne mancanti)
 * prima che si manifestino come HTTP 500 runtime su endpoint applicativi.
 */
@Component
public class SchemaSanityCheckRunner implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(SchemaSanityCheckRunner.class);

    private final JdbcTemplate jdbcTemplate;

    @Value("${app.schema-sanity-check.enabled:true}")
    private boolean enabled;

    public SchemaSanityCheckRunner(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (!enabled) {
            log.info("Schema sanity check disabilitato (app.schema-sanity-check.enabled=false)");
            return;
        }

        List<String> issues = new ArrayList<>();

        // Colonne critiche per evitare errori runtime sui contatti aziendali.
        requireColumn("public", "contatto_aziendale", "id_utente", issues);
        requireColumn("public", "contatto_aziendale", "ruolo", issues);
        requireColumn("public", "azienda", "tipo", issues);

        // Enum critici usati da entity e DTO.
        requireEnumValue("public", "ruolo_contatto_enum", "TITOLARE", issues);
        requireEnumValue("public", "ruolo_contatto_enum", "DIRETTORE", issues);
        requireEnumValue("public", "ruolo_contatto_enum", "RESPONSABILE_HR", issues);
        requireEnumValue("public", "ruolo_contatto_enum", "RESPONSABILE_TIROCINI", issues);
        requireEnumValue("public", "ruolo_contatto_enum", "TUTOR_AZIENDALE", issues);
        requireEnumValue("public", "tipo_azienda_enum", "MADRINA", issues);
        requireEnumValue("public", "tipo_azienda_enum", "NON_MADRINA", issues);

        if (!issues.isEmpty()) {
            String message = "Schema sanity check fallito:\n - "
                    + String.join("\n - ", issues)
                    + "\nAzioni consigliate: eseguire ./scripts/bootstrap-db.ps1 -ResetVolume "
                    + "oppure applicare le migration mancanti in Database/migrations.";

            throw new IllegalStateException(message);
        }

        log.info("Schema sanity check completato con esito positivo.");
    }

    private void requireColumn(String schema, String table, String column, List<String> issues) {
        Integer count = jdbcTemplate.queryForObject(
                """
                select count(*)
                from information_schema.columns
                where table_schema = ?
                  and table_name = ?
                  and column_name = ?
                """,
                Integer.class,
                schema,
                table,
                column
        );

        if (count == null || count == 0) {
            issues.add("Colonna mancante: " + schema + "." + table + "." + column);
        }
    }

    private void requireEnumValue(String schema, String enumName, String enumValue, List<String> issues) {
        Integer count = jdbcTemplate.queryForObject(
                """
                select count(*)
                from pg_enum e
                join pg_type t on t.oid = e.enumtypid
                join pg_namespace n on n.oid = t.typnamespace
                where n.nspname = ?
                  and t.typname = ?
                  and e.enumlabel = ?
                """,
                Integer.class,
                schema,
                enumName,
                enumValue
        );

        if (count == null || count == 0) {
            issues.add("Valore enum mancante: " + schema + "." + enumName + " = '" + enumValue + "'");
        }
    }
}
