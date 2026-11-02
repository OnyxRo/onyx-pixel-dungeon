# Builds a double-clickable Windows app for the game and puts a shortcut on the desktop.
#
#   powershell -ExecutionPolicy Bypass -File scripts\make-exe.ps1
#
# The app is a folder with OnyxPixelDungeon.exe and its own copy of Java, made by jpackage
# (which comes with the JDK). It uses the same save folder as the game run from Gradle.
# Close the app before running this again, or Windows will not let the old copy be replaced.

$ErrorActionPreference = 'Stop'
$repo = Split-Path -Parent $PSScriptRoot
$dest = Join-Path ([Environment]::GetFolderPath('MyDocuments')) 'OnyxPixelDungeon-app'
$name = 'OnyxPixelDungeon'

Push-Location $repo
try {
    & .\gradlew.bat desktop:release --console=plain -q
    if ($LASTEXITCODE -ne 0) { throw 'the game did not build' }

    $jar = Get-ChildItem desktop\build\libs\desktop-*.jar | Sort-Object LastWriteTime -Descending | Select-Object -First 1
    $stage = Join-Path $env:TEMP 'onyxpd-jpackage-input'
    if (Test-Path $stage) { Remove-Item -Recurse -Force $stage }
    New-Item -ItemType Directory $stage | Out-Null
    Copy-Item $jar.FullName $stage

    $app = Join-Path $dest $name
    if (Test-Path $app) { Remove-Item -Recurse -Force $app }
    New-Item -ItemType Directory -Force $dest | Out-Null

    # JAVA_HOME is not always set when git runs this from a hook, so fall back to jpackage on the path
    if ($env:JAVA_HOME) { $jpackage = Join-Path $env:JAVA_HOME 'bin\jpackage.exe' }
    else { $jpackage = (Get-Command jpackage -ErrorAction Stop).Source }
    & $jpackage --type app-image --name $name --input $stage --main-jar $jar.Name `
        --icon desktop\src\main\assets\icons\windows.ico --dest $dest `
        --java-options '-XX:+IgnoreUnrecognizedVMOptions' `
        --java-options '-Donyx.dev=true'
    # onyx.dev keeps every mod and the testing tools open in Onyx's copy, a GitHub copy starts as plain Shattered (ModsUnlock)
    if ($LASTEXITCODE -ne 0) { throw 'jpackage failed' }

    $exe = Join-Path $app "$name.exe"
    $shell = New-Object -ComObject WScript.Shell
    $link = $shell.CreateShortcut((Join-Path ([Environment]::GetFolderPath('Desktop')) "Onyx's Pixel Dungeon.lnk"))
    $link.TargetPath = $exe
    $link.WorkingDirectory = $app
    $link.IconLocation = "$exe,0"
    $link.Description = "Onyx's Pixel Dungeon"
    $link.Save()

    "app:      $exe"
    "shortcut: $($link.FullName)"
} finally {
    Pop-Location
}
