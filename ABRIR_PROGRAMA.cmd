@echo off
setlocal
title SAT-CARMEN
echo Abriendo SAT-CARMEN...
echo Si se solicita, introduce tu clave de MySQL.
powershell.exe -NoProfile -ExecutionPolicy Bypass -File "%~dp0INICIAR.ps1" -DbHost localhost -Port 3307 -Database sat_carmen_sem14_demo -DbUser root
if errorlevel 1 (
  echo.
  echo No se pudo abrir el programa. Conserva el mensaje para revisarlo.
  pause
)
endlocal
