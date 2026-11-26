function Get-SatJavaTool([string]$Name) {
 $candidates=@()
 if ($env:JAVA_HOME) {$candidates += Join-Path $env:JAVA_HOME "bin/$Name.exe"}
 foreach($root in @("$env:ProgramFiles/Java", "$env:ProgramFiles/Eclipse Adoptium")) {
  if(Test-Path $root){$candidates += Get-ChildItem $root -Directory | Sort-Object Name -Descending | ForEach-Object {Join-Path $_.FullName "bin/$Name.exe"}}
 }
 foreach($candidate in $candidates){if(Test-Path -LiteralPath $candidate){return $candidate}}
 $command=Get-Command $Name -ErrorAction SilentlyContinue
 if($command){return $command.Source}
 throw 'Instala un JDK 21 o superior y configura JAVA_HOME.'
}
