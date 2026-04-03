# [BF-007] - Configurazione CORS e profili dev/prod

## Obiettivo
Rendere la configurazione CORS gestibile per ambiente e introdurre profili Spring Boot separati per sviluppo e produzione.

## Cambiamenti implementati

### 1) CORS centralizzato
- Rimossa la configurazione CORS dai singoli controller.
- Introdotta configurazione globale tramite WebMvcConfigurer su path /api/**.
- CORS ora configurato via proprieta invece che hardcoded.

File aggiornati:
- WebApplication/Backend/src/main/java/com/its/gestionale/config/CorsConfig.java
- WebApplication/Backend/src/main/java/com/its/gestionale/controller/AllievoController.java
- WebApplication/Backend/src/main/java/com/its/gestionale/controller/CorsoController.java
- WebApplication/Backend/src/main/java/com/its/gestionale/controller/UtenteController.java

### 2) Profili Spring Boot
Creati i file di configurazione separati:
- application-dev.properties
- application-prod.properties

E aggiornato application.properties con:
- spring.profiles.default=dev
- proprieta CORS comuni (metodi, header, credenziali, max-age)

File aggiornati:
- WebApplication/Backend/src/main/resources/application.properties
- WebApplication/Backend/src/main/resources/application-dev.properties
- WebApplication/Backend/src/main/resources/application-prod.properties

## Configurazione CORS per ambiente

### Dev
- Origini consentite:
  - http://localhost:4200
  - http://127.0.0.1:4200
- Metodi: GET, POST, PUT, DELETE, OPTIONS
- Header: Authorization, Content-Type, Accept, Origin, X-Requested-With
- Credenziali: abilitate

### Prod
- Origine consentita letta da variabile:
  - APP_CORS_ALLOWED_ORIGINS
- Fallback di default:
  - https://app.example.com
- Metodi e header allineati alla policy comune

## Motivazione tecnica
- Evitata configurazione troppo permissiva con origins = *.
- Migliorata manutenibilita: una sola fonte CORS globale.
- Ridotto rischio in produzione grazie a allowlist di origini.
- Profili separati per database, logging e comportamento runtime.

## Come attivare i profili

### Default (dev)
- Nessuna variabile richiesta: usa spring.profiles.default=dev.

### Prod
- Impostare variabile ambiente:
  - SPRING_PROFILES_ACTIVE=prod
- Impostare origine frontend:
  - APP_CORS_ALLOWED_ORIGINS=https://tuo-frontend.example.com
- Impostare credenziali database (consigliato):
  - Nota: `SPRING_DATASOURCE_USERNAME` indica l'utente del database, non il campo `username` del dominio applicativo, che è stato eliminato.
  - SPRING_DATASOURCE_URL
  - SPRING_DATASOURCE_USERNAME
  - SPRING_DATASOURCE_PASSWORD

## Note operative
- Con allowCredentials=true non usare origine *.
- Se in sviluppo cambia porta frontend, aggiornare app.cors.allowed-origins nel profilo dev.
- In produzione sostituire il fallback https://app.example.com con dominio reale.

## Verifiche consigliate
- Avvio backend con profilo dev.
- Chiamata API da frontend Angular locale.
- Test preflight OPTIONS verso endpoint /api/*.
- Avvio backend con profilo prod in ambiente controllato con dominio reale in allowlist.
