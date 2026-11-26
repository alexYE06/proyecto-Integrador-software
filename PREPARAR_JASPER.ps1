$ErrorActionPreference='Stop'
. (Join-Path $PSScriptRoot 'JAVA.ps1')
Push-Location $PSScriptRoot
try {
 & ./COMPILAR.ps1
 & (Get-SatJavaTool 'java') -cp 'target/classes;lib/*' app.PrepararJasper
 if ($LASTEXITCODE -ne 0) {throw 'No se pudo preparar el adaptador. Configura MySQL primero.'}
} finally {Pop-Location}
