@echo off
setlocal
title SAT-CARMEN
powershell.exe -NoProfile -ExecutionPolicy Bypass -File "%~dp0INICIAR.ps1"
if errorlevel 1 pause
endlocal
