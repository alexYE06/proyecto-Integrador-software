param([switch]$MySQL)
$ErrorActionPreference='Stop'
. (Join-Path $PSScriptRoot 'JAVA.ps1')
Push-Location $PSScriptRoot
try {
 & ./COMPILAR.ps1
 New-Item -ItemType Directory -Force target/test-classes | Out-Null
 & (Get-SatJavaTool 'javac') -encoding UTF-8 --release 21 -cp 'target/classes;lib/*' -d target/test-classes src/test/java/PresentacionSmoke.java
 if($LASTEXITCODE -ne 0){throw 'No se pudieron compilar las pruebas.'}
 $options=@('-Djava.awt.headless=true','-cp','target/test-classes;target/classes;lib/*','PresentacionSmoke')
 if($MySQL){$options += '--mysql'}
 & (Get-SatJavaTool 'java') @options
 if($LASTEXITCODE -ne 0){throw 'La verificación falló. Revisa el mensaje anterior.'}
} finally {Pop-Location}
