$ErrorActionPreference='Stop'
. (Join-Path $PSScriptRoot 'JAVA.ps1')
Push-Location $PSScriptRoot
try {
 if ($env:DB_NAME -ne 'sat_carmen_integracion_prueba') { throw 'Configura DB_NAME=sat_carmen_integracion_prueba. Nunca ejecutar contra datos reales.' }
 & ./COMPILAR.ps1
 New-Item -ItemType Directory -Force target/test-classes | Out-Null
 & (Get-SatJavaTool 'javac') -encoding UTF-8 --release 21 -cp 'target/classes;lib/*' -d target/test-classes src/test/java/RegressionSuite.java
 if ($LASTEXITCODE -ne 0) { throw 'Falló la compilación de pruebas.' }
 & (Get-SatJavaTool 'java') '-Djava.awt.headless=true' -cp 'target/test-classes;target/classes;lib/*' RegressionSuite
 if ($LASTEXITCODE -ne 0) { throw 'Fallaron las pruebas.' }
} finally { Pop-Location }
