param([string]$DbHost='127.0.0.1',[int]$Port=3306,[string]$Database='sistema_alertas',[string]$DbUser='root')
$ErrorActionPreference='Stop'
$keys=@('DB_HOST','DB_PORT','DB_NAME','DB_USER','DB_PASSWORD')
$before=@{}
foreach($key in $keys){$before[$key]=[Environment]::GetEnvironmentVariable($key,'Process')}
Push-Location $PSScriptRoot
try {
 if (!(Test-Path target/classes/vista/DemoSelectorView.class)) { & ./COMPILAR.ps1 }
 $secret=Read-Host 'Contraseña MySQL (solo para esta ejecución)' -AsSecureString
 $ptr=[Runtime.InteropServices.Marshal]::SecureStringToBSTR($secret)
 try {$env:DB_PASSWORD=[Runtime.InteropServices.Marshal]::PtrToStringBSTR($ptr)}
 finally {[Runtime.InteropServices.Marshal]::ZeroFreeBSTR($ptr)}
 $env:DB_HOST=$DbHost;$env:DB_PORT="$Port";$env:DB_NAME=$Database;$env:DB_USER=$DbUser
 & java -cp 'target/classes;lib/*' vista.DemoSelectorView
 if($LASTEXITCODE -ne 0){throw 'La aplicación terminó con un error.'}
} finally {
 foreach($key in $keys){[Environment]::SetEnvironmentVariable($key,$before[$key],'Process')}
 Pop-Location
}
