# Installs Onyx's Pixel Dungeon on Windows.
#
# Downloads the official Shattered Pixel Dungeon Windows build (for its launcher and
# bundled Java runtime) from GitHub, then sets it up to run Onyx's Pixel Dungeon.
# Written for Windows PowerShell 5.1, which comes with Windows 10 and 11.

$ErrorActionPreference = "Stop"
$ProgressPreference = "SilentlyContinue"   # the progress bar makes downloads very slow in PowerShell 5.1
[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12

$Files      = Split-Path -Parent $MyInvocation.MyCommand.Path
$Here       = Split-Path -Parent $Files
$GameName   = "Onyx's Pixel Dungeon"
$ExeName    = "Onyx Pixel Dungeon"
$BaseUrl    = "__BASE_URL__"
$BaseSha256 = "__BASE_SHA256__"
$Target     = Join-Path $Here $GameName

Write-Host ""
Write-Host "Installing $GameName" -ForegroundColor Green
Write-Host ""

if (Test-Path $Target) {
    Write-Host "Removing the previous install (your saves are kept, they live in %APPDATA%)..."
    Remove-Item -Recurse -Force $Target
}

$Zip = Join-Path $env:TEMP "onyxpd-base.zip"
Write-Host "Downloading the game runtime from GitHub (about 100 MB)..."
Invoke-WebRequest -Uri $BaseUrl -OutFile $Zip -UseBasicParsing

$Hash = (Get-FileHash -Algorithm SHA256 $Zip).Hash
if ($Hash -ne $BaseSha256) {
    throw "The downloaded file did not match the expected checksum. Please try again."
}

Write-Host "Unpacking..."
Expand-Archive -Path $Zip -DestinationPath $Target -Force
Remove-Item -Force $Zip

$App = Join-Path $Target "app"

# keep the official libraries, swap in this game ahead of them on the classpath
$OldCfg = Join-Path $App "Shattered Pixel Dungeon.cfg"
$Libs = Get-Content $OldCfg | Where-Object { $_ -like "app.classpath=*" }
Remove-Item -Force $OldCfg

Copy-Item (Join-Path $Files "OnyxPixelDungeon.jar") (Join-Path $App "OnyxPixelDungeon.jar")

$Cfg = @("[Application]", "app.classpath=`$APPDIR\OnyxPixelDungeon.jar") + $Libs + @(
    "app.mainclass=com.shatteredpixel.shatteredpixeldungeon.desktop.DesktopLauncher",
    "",
    "[JavaOptions]",
    "java-options=-Djpackage.app-version=__VERSION__",
    "java-options=-XX:+IgnoreUnrecognizedVMOptions")
Set-Content -Path (Join-Path $App "$ExeName.cfg") -Value $Cfg -Encoding Ascii

Rename-Item (Join-Path $Target "Shattered Pixel Dungeon.exe") "$ExeName.exe"
Rename-Item (Join-Path $Target "Shattered Pixel Dungeon.ico") "$ExeName.ico"
Copy-Item (Join-Path $Files "LICENSE.txt") $Target

# desktop shortcut
$Shell = New-Object -ComObject WScript.Shell
$Link = $Shell.CreateShortcut((Join-Path ([Environment]::GetFolderPath("Desktop")) "$GameName.lnk"))
$Link.TargetPath = Join-Path $Target "$ExeName.exe"
$Link.WorkingDirectory = $Target
$Link.IconLocation = Join-Path $Target "$ExeName.ico"
$Link.Save()

Write-Host ""
Write-Host "Done. There's a shortcut on your desktop, or open the '$GameName' folder here." -ForegroundColor Green
Write-Host ""
