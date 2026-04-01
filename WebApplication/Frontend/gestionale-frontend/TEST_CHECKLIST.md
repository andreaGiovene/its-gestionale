# Frontend Test Checklist - Login & Dashboard

## Prerequisiti
- Frontend: http://localhost:4200
- Backend: http://localhost:8080 (modo test)
- Credenziali demo: `test.login@its.local` / `password123`

---

## FASE 1: Pagina Login Visibile ✓ / ✗

**Step 1** - Accedi a http://localhost:4200/login

**Verifica:**
- [ ] Pagina carica con titolo "Accesso"
- [ ] Vedi il sottotitolo "Inserisci le tue credenziali per continuare."
- [ ] 2 campo input visibili: Email e Password
- [ ] 1 bottone "Entra"
- [ ] Nessun errore in browser console (F12)

**Se rosso:** Controlla browser console per errori JavaScript

---

## FASE 2: Login Credenziali ✓ / ✗

**Step 2** - Inserisci credenziali:
- Email: `test.login@its.local`
- Password: `password123`

**Step 3** - Clicca bottone "Entra"

**Verifica:**
- [ ] Bottone mostra spinner durante il caricamento
- [ ] Entro 2-3 secondi, redirect automatico a http://localhost:4200/dashboard
- [ ] URL cambia da `/login` a `/dashboard`

**Se rosso:** 
- Controlla backend è in esecuzione (porta 8080)
- Controlla console browser per errori CORS o 401/500
- Verifica che l'utente esista nel DB

---

## FASE 3: Token Salvato in localStorage ✓ / ✗

**Step 4** - Apri browser console: F12 → Tab "Application" (Chrome) / "Storage" (Firefox)

**Verifica:**
- [ ] Seleziona "Local Storage" → http://localhost:4200
- [ ] Cerca chiave **`auth_token`**
- [ ] Valore deve essere un JWT token (lunghissima stringa iniziante con `eyJh...`)

**Se rosso:** 
- auth.service.ts non sta salvando il token
- Controlla che login request ritorni field `token` nel body

---

## FASE 4: Dashboard Visibile ✓ / ✗

**Step 5** - Una volta reindirizzato a `/dashboard`

**Verifica:**
- [ ] Vedi layout con sidebar a sinistra (menu ITS Gestionale)
- [ ] Sidebar ha voci: Dashboard, Corsi, Allievi, Aziende, Colloqui, Tirocini
- [ ] Toolbar in alto con scritta "ITS — Sistema Gestione Tirocini"
- [ ] Contenuto principale visibile (dashboard component)
- [ ] Nessun errore in browser console

**Se rosso:** 
- Controlla che Dashboard component sia stato creato
- Verifica che authGuard non blocchi l'accesso

---

## FASE 5: Logout & Token Removal ✓ / ✗

**Step 6** - (Opzionale) Implementa un pulsante logout nella navbar

**Verifica:**
- [ ] Clicca logout
- [ ] Redirect a `/login`
- [ ] Vai su DevTools → Local Storage
- [ ] Chiave `auth_token` è **rimossa**

**Se rosso:** 
- auth.service.logout() non sta pulendo il localStorage

---

## Risultato Finale

| Test | Pass | Fail | Note |
|------|------|------|------|
| Pagina login visibile | ☑ | ☐ | |
| Login redirect | ☑ | ☐ | |
| Token salvato | ☑ | ☐ | |
| Dashboard visibile | ☑ | ☐ | |
| Logout (opz.) | ☑ | ☐ | |

---

## Comandi Utili (Browser Console)

```javascript
// Visualizza token salvato
localStorage.getItem('auth_token')

// Cancella token manualmente
localStorage.removeItem('auth_token')

// Forza logout e reload
localStorage.clear(); window.location.reload()

// Verifica errori HTTP
// Apri DevTools → Network tab, e ripeti login
```

---

## Contact
Se ci sono errori, allegare:
1. Screenshot dell'errore console (F12)
2. Risposta HTTP della request `/auth/login` (Network tab)
3. Stato del backend (è in ascolto su 8080?)
