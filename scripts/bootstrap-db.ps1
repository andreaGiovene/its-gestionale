param(
    [switch]$ResetVolume
)

Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'

function Write-Step {
    param([string]$Message)
    Write-Host "`n==> $Message" -ForegroundColor Cyan
}

function Get-ComposeCommand {
    try {
        docker compose version | Out-Null
        return @('docker', 'compose')
    }
    catch {
        throw "Docker Compose plugin non disponibile. Installa Docker Desktop con 'docker compose'."
    }
}

function Invoke-Compose {
    param(
        [string[]]$ComposeCmd,
        [string[]]$ComposeArgs
    )

    & $ComposeCmd[0] $ComposeCmd[1] @ComposeArgs
    if ($LASTEXITCODE -ne 0) {
        throw "Comando compose fallito: $($ComposeCmd -join ' ') $($ComposeArgs -join ' ')"
    }
}

function Wait-PostgresReady {
    param(
        [int]$MaxAttempts = 30,
        [int]$DelaySeconds = 2
    )

    for ($i = 1; $i -le $MaxAttempts; $i++) {
        try {
            docker exec postgres_db pg_isready -U postgres -d db_its_stage | Out-Null
            if ($LASTEXITCODE -eq 0) {
                Write-Host "PostgreSQL pronto." -ForegroundColor Green
                return
            }
        }
        catch {
            # Retry
        }

        Write-Host "Attendo PostgreSQL... tentativo $i/$MaxAttempts" -ForegroundColor Yellow
        Start-Sleep -Seconds $DelaySeconds
    }

    throw "PostgreSQL non pronto entro il timeout."
}

function Invoke-SqlFile {
    param([string]$FilePath)

    if (-not (Test-Path $FilePath)) {
        throw "Migration non trovata: $FilePath"
    }

    Write-Host "Eseguo migration: $(Split-Path $FilePath -Leaf)" -ForegroundColor DarkCyan
    Get-Content -Raw $FilePath | docker exec -i postgres_db psql -v ON_ERROR_STOP=1 -U postgres -d db_its_stage | Out-Null
    if ($LASTEXITCODE -ne 0) {
        throw "Errore durante migration: $FilePath"
    }
}

function Invoke-SqlScalar {
    param([string]$Sql)

    $result = docker exec postgres_db psql -U postgres -d db_its_stage -t -A -c $Sql
    if ($LASTEXITCODE -ne 0) {
        throw "Errore SQL: $Sql"
    }
    return ($result | Out-String).Trim()
}

$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$repoRoot = Split-Path -Parent $scriptDir
Set-Location $repoRoot

Write-Step "Controllo prerequisiti Docker"
docker version | Out-Null
if ($LASTEXITCODE -ne 0) {
    throw "Docker non disponibile o non avviato."
}

$composeCmd = Get-ComposeCommand
$composeFile = Join-Path $repoRoot 'compose.yaml'

if (-not (Test-Path $composeFile)) {
    throw "compose.yaml non trovato in: $repoRoot"
}

if ($ResetVolume) {
    Write-Step "Reset completo container + volume"
    Invoke-Compose -ComposeCmd $composeCmd -ComposeArgs @('-f', $composeFile, 'down', '-v', '--remove-orphans')
}

Write-Step "Avvio servizio postgres"
Invoke-Compose -ComposeCmd $composeCmd -ComposeArgs @('-f', $composeFile, 'up', '-d', 'postgres')

Write-Step "Attendo disponibilita PostgreSQL"
Wait-PostgresReady

$migrationsDir = Join-Path $repoRoot 'Database\migrations'
$migrationOrder = @(
    '2026-04-03-drop-utente-username.sql',
    '2026-04-08-add-allievo-telefono.sql',
    '2026-04-09-delete-corsi-fittizi.sql',
    '2026-04-09-seed-corsi-realistici.sql',
    '2026-04-14-add-azienda-madrina-to-corso.sql',
    '2026-04-14-add-citta-to-azienda.sql',
    '2026-04-14-add-tipo-to-azienda.sql',
    '2026-04-14-delete-aziende-fittizie.sql',
    '2026-04-14-seed-aziende-madrine-2025-2027.sql',
    '2026-04-14-finalize-azienda-tipo-native-pg-enum.sql',
    '2026-04-14-normalize-tipo-azienda-madrina-non_madrina.sql',
    '2026-04-14-finalize-azienda-tipo-enum-contract.sql',
    '2026-04-15-restore-original-ruolo-contatto-enum.sql'
)

Write-Step "Applico migration SQL in ordine"
foreach ($migration in $migrationOrder) {
    $migrationPath = Join-Path $migrationsDir $migration
    Invoke-SqlFile -FilePath $migrationPath
}

Write-Step "Verifica dati principali"
$courses2025 = [int](Invoke-SqlScalar "SELECT COUNT(*) FROM public.corso WHERE anno_accademico = '2025/2027';")
$mapped2025 = [int](Invoke-SqlScalar "SELECT COUNT(*) FROM public.corso WHERE anno_accademico = '2025/2027' AND id_azienda_madrina IS NOT NULL;")
$fakeCourses = [int](Invoke-SqlScalar "SELECT COUNT(*) FROM public.corso WHERE nome_corso IN ('Cloud Developer','Blockchain Expert','UI/UX Designer');")
$madrineCount = [int](Invoke-SqlScalar "SELECT COUNT(*) FROM public.azienda WHERE tipo::text = 'MADRINA';")

Write-Host "Corsi 2025/2027: $courses2025" -ForegroundColor Gray
Write-Host "Corsi 2025/2027 con azienda madrina: $mapped2025" -ForegroundColor Gray
Write-Host "Corsi fittizi residui: $fakeCourses" -ForegroundColor Gray
Write-Host "Aziende tipo MADRINA: $madrineCount" -ForegroundColor Gray

if ($courses2025 -lt 14) {
    throw "Verifica fallita: corsi 2025/2027 attesi >= 14, trovati $courses2025"
}
if ($mapped2025 -lt 14) {
    throw "Verifica fallita: corsi 2025/2027 con madrina attesi >= 14, trovati $mapped2025"
}
if ($fakeCourses -ne 0) {
    throw "Verifica fallita: presenti ancora corsi fittizi ($fakeCourses)."
}
if ($madrineCount -lt 14) {
    throw "Verifica fallita: aziende MADRINA attese >= 14, trovate $madrineCount"
}

Write-Step "Check API backend (se servizio attivo)"
$apiEndpoints = @(
    'http://localhost:8080/api/corsi',
    'http://localhost:8080/api/allievi',
    'http://localhost:8080/api/aziende?page=0&size=10'
)

foreach ($endpoint in $apiEndpoints) {
    try {
        $statusCode = (Invoke-WebRequest -UseBasicParsing -TimeoutSec 5 -Uri $endpoint).StatusCode
        Write-Host "API OK $statusCode - $endpoint" -ForegroundColor Green
    }
    catch {
        Write-Host "API non raggiungibile ora: $endpoint" -ForegroundColor Yellow
    }
}

Write-Host "`nBootstrap DB completato con successo." -ForegroundColor Green
Write-Host "Esegui: cd WebApplication/Backend ; java -jar target/gestionale-0.0.1-SNAPSHOT.jar" -ForegroundColor Cyan
Write-Host "Poi:    cd WebApplication/Frontend/gestionale-frontend ; npm start" -ForegroundColor Cyan
