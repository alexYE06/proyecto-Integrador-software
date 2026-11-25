param([string]$LibDir = (Join-Path $PSScriptRoot 'lib'))
$ErrorActionPreference='Stop'
Push-Location $PSScriptRoot
try {
 if (!(Test-Path -LiteralPath $LibDir)) {
  if (!(Get-Command mvn -ErrorAction SilentlyContinue)) { throw 'Instala Maven o proporciona -LibDir con las bibliotecas de la entrega S15.' }
  & mvn dependency:copy-dependencies '-DoutputDirectory=lib'
  if ($LASTEXITCODE -ne 0) { throw 'No se descargaron las dependencias.' }
 }
 New-Item -ItemType Directory -Force target/classes | Out-Null
 $sources=Get-ChildItem src/main/java -Recurse -Filter '*.java' | Select-Object -ExpandProperty FullName
 & javac -encoding UTF-8 --release 21 -cp "$LibDir/*" -d target/classes $sources
 if ($LASTEXITCODE -ne 0) { throw 'Falló la compilación.' }
 Copy-Item -Path src/main/resources/* -Destination target/classes -Recurse -Force
 Write-Output 'Compilación completa. Inicio: java -cp "target/classes;lib/*" vista.DemoSelectorView'
} finally { Pop-Location }
