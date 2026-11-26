param([string]$DbHost='', [int]$Port=0, [string]$Database='', [string]$DbUser='')
$ErrorActionPreference='Stop'
. (Join-Path $PSScriptRoot 'JAVA.ps1')
Push-Location $PSScriptRoot
try {
 & ./COMPILAR.ps1
 if ($DbHost) {$env:DB_HOST=$DbHost}
 if ($Port) {$env:DB_PORT="$Port"}
 if ($Database) {$env:DB_NAME=$Database}
 if ($DbUser) {$env:DB_USER=$DbUser}
 & (Get-SatJavaTool 'java') -cp 'target/classes;lib/*' app.SatCarmen
 if ($LASTEXITCODE -ne 0) {throw 'La aplicación terminó con un error.'}
} finally {Pop-Location}
