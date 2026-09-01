$adb = "$env:LOCALAPPDATA\Android\Sdk\platform-tools\adb.exe"

Write-Host "Configurando conexion con InventoryApi..."

& $adb reverse tcp:5133 tcp:5133

Write-Host ""
Write-Host "Puertos configurados:"
& $adb reverse --list