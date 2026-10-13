# Launches Onyx's Pixel Dungeon, updating it first if a newer release is on GitHub.
#
# The installer puts this next to the game and points the desktop shortcut at it.
# If the update check fails (offline, GitHub down), the game starts anyway.
# Written for Windows PowerShell 5.1, which comes with Windows 10 and 11.

$ProgressPreference = "SilentlyContinue"
[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12

$Here    = Split-Path -Parent $MyInvocation.MyCommand.Path
$App     = Join-Path $Here "app"
$Jar     = Join-Path $App "OnyxPixelDungeon.jar"
$Info    = Join-Path $App "onyx-version.txt"
$Repo    = "OnyxRo/onyx-pixel-dungeon"
$Exe     = Join-Path $Here "Onyx Pixel Dungeon.exe"

# onyx-version.txt holds two lines, the game version and the Shattered base it runs on
$Installed = @("none", "none")
if (Test-Path $Info) { $Installed = @(Get-Content $Info) + @("none", "none") }

try {
    Write-Host "Checking for updates..."
    $Release = Invoke-RestMethod -Uri "https://api.github.com/repos/$Repo/releases/latest" `
        -Headers @{ "User-Agent" = "onyx-pixel-dungeon-updater" } -TimeoutSec 8
    $Manifest = $Release.assets | Where-Object { $_.name -eq "update.json" } | Select-Object -First 1

    if ($Manifest) {
        $Update = Invoke-RestMethod -Uri $Manifest.browser_download_url `
            -Headers @{ "User-Agent" = "onyx-pixel-dungeon-updater" } -TimeoutSec 8

        if ($Update.version -ne $Installed[0]) {
            if ($Update.base -ne $Installed[1] -and $Installed[1] -ne "none") {
                Write-Host ""
                Write-Host "Version $($Update.version) needs a newer runtime than this install has." -ForegroundColor Yellow
                Write-Host "Download the latest installer from https://github.com/$Repo/releases/latest and run Install.bat."
                Write-Host "Starting the version you have for now..."
                Start-Sleep -Seconds 6
            } else {
                $Asset = $Release.assets | Where-Object { $_.name -eq $Update.jar } | Select-Object -First 1
                if ($Asset) {
                    Write-Host "Updating to version $($Update.version)..."
                    $Tmp = Join-Path $env:TEMP "onyxpd-update.jar"
                    Invoke-WebRequest -Uri $Asset.browser_download_url -OutFile $Tmp -UseBasicParsing -TimeoutSec 120
                    $Hash = (Get-FileHash -Algorithm SHA256 $Tmp).Hash
                    if ($Hash -eq $Update.sha256.ToUpper()) {
                        Move-Item -Force $Tmp $Jar
                        Set-Content -Path $Info -Value @($Update.version, $Update.base) -Encoding Ascii
                        Write-Host "Updated." -ForegroundColor Green
                    } else {
                        Remove-Item -Force $Tmp
                        Write-Host "The update didn't download correctly, starting the current version."
                    }
                }
            }
        }
    }
} catch {
    Write-Host "Couldn't check for updates, starting the current version."
}

if (-not $env:ONYXPD_NO_LAUNCH) {
    Start-Process -FilePath $Exe -WorkingDirectory $Here
}
