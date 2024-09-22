Write-Host "[INFO] Welcome to Purrooser"

$DIR = "$HOME\.purrooser"
$REPO_URL = "https://github.com/Thoq-jar/Purrooser.git"

if (-Not (Test-Path $DIR)) {
    git clone $REPO_URL $DIR | Out-Null
} else {
    Set-Location $DIR
    Write-Host "[UPDATE SERVICE] Updating to latest version..."
    git pull | Out-Null
}

if (-Not (Get-Command java -ErrorAction SilentlyContinue)) {
    Write-Host "Java is not installed. Please install Java to continue!"
    exit 1
}

if (-Not (Get-Command mvn -ErrorAction SilentlyContinue)) {
    Write-Host "Maven is not installed. Please install Maven to continue!"
    exit 1
}

Write-Host "[UPDATE SERVICE] Done!"
Write-Host "[INFO] Starting"
& "./mvnw" javafx:run | Out-Null
Write-Host "[INFO] Exiting"
exit 0