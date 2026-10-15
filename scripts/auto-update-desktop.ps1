# Rebuilds Onyx's desktop app (the one on the desktop shortcut) from this repo.
#
# Git runs this by itself after a commit or a merge that changed game code (scripts/hooks).
# Run it by hand with  powershell -ExecutionPolicy Bypass -File scripts\auto-update-desktop.ps1 -Force
#
#   -Force    build even if no game code changed
#   -DryRun   say what it would do, build nothing
#
# It never starts the game. If the game is open it does not touch it, and tries again at the next commit.
# Progress and errors go to build\auto-update.log. A failed build leaves the old app in place.
param([switch]$Force, [switch]$DryRun)

$repo = Split-Path -Parent $PSScriptRoot
$buildDir = Join-Path $repo 'build'
New-Item -ItemType Directory -Force $buildDir | Out-Null
$log = Join-Path $buildDir 'auto-update.log'
$lock = Join-Path $buildDir 'auto-update.lock'

function Log($text) {
    Add-Content -Path $log -Value ("{0}  {1}" -f (Get-Date -Format 'yyyy-MM-dd HH:mm:ss'), $text)
}

# keep the log short
if ((Test-Path $log) -and ((Get-Item $log).Length -gt 200KB)) { Remove-Item $log }

if (Get-Process -Name 'OnyxPixelDungeon' -ErrorAction SilentlyContinue) {
    Log 'the game is open, so the desktop app was not replaced. It updates at the next commit, or run this with -Force after closing the game.'
    exit 0
}

if (Test-Path $lock) {
    $age = (Get-Date) - (Get-Item $lock).LastWriteTime
    if ($age.TotalMinutes -lt 30) { Log 'another update is already running, skipped'; exit 0 }
    Remove-Item $lock -Force
}

if ($DryRun) { Log 'dry run, would build and replace the desktop app'; exit 0 }

Set-Content -Path $lock -Value $PID
try {
    Log ('building from ' + (& git -C $repo rev-parse --short HEAD) + ' on ' + (& git -C $repo rev-parse --abbrev-ref HEAD))
    $out = & powershell -NoProfile -ExecutionPolicy Bypass -File (Join-Path $PSScriptRoot 'make-exe.ps1') 2>&1
    if ($LASTEXITCODE -ne 0) {
        Log ('FAILED, the old desktop app is unchanged. ' + (($out | Select-Object -Last 6) -join ' | '))
        exit 1
    }
    Log 'desktop app updated'
} catch {
    Log ('FAILED, ' + $_.Exception.Message)
    exit 1
} finally {
    Remove-Item $lock -Force -ErrorAction SilentlyContinue
}
