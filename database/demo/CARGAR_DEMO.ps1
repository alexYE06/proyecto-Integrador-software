param([string]$Mysql='mysql',[int]$Port=3317)
$ErrorActionPreference='Stop'
$exists = & $Mysql --host=127.0.0.1 "--port=$Port" --user=root --batch --skip-column-names --execute="SELECT SCHEMA_NAME FROM information_schema.SCHEMATA WHERE SCHEMA_NAME='sat_carmen_integracion_prueba'"
if ($LASTEXITCODE -ne 0) { throw 'No se pudo conectar a la instancia de pruebas.' }
if ($exists) { throw 'La base de pruebas ya existe: se conserva sin modificar. Usa una instancia nueva para repetir la batería.' }
$sql=Get-Content -LiteralPath (Join-Path $PSScriptRoot 'crear_demo.sql') -Raw -Encoding utf8
$previousEncoding=$OutputEncoding
try {
 $OutputEncoding=[System.Text.UTF8Encoding]::new($false)
 $sql | & $Mysql --host=127.0.0.1 "--port=$Port" --user=root --default-character-set=utf8mb4
 if ($LASTEXITCODE -ne 0) { throw 'Falló la carga. No reintentar sobre una carga parcial.' }
} finally { $OutputEncoding=$previousEncoding }
