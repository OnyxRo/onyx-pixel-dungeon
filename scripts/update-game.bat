@echo off
rem Double-click this to rebuild the desktop game with the latest changes and open it.
rem Close the game first. Windows will not let the old copy be replaced while it runs.
cd /d "%~dp0.."
powershell -NoProfile -ExecutionPolicy Bypass -File scripts\make-exe.ps1
start "" "%USERPROFILE%\Documents\OnyxPixelDungeon-app\OnyxPixelDungeon\OnyxPixelDungeon.exe"
pause
