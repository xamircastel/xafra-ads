# 📦 XAFRA-ADS - Backup Script
# Automatización para crear respaldo completo del proyecto

param(
    [string]$BackupPath = "C:\Users\XCAST\Desktop\xafra-ads-backup-$(Get-Date -Format 'yyyy-MM-dd-HHmm')",
    [switch]$SkipMaven,
    [switch]$Compress
)

# Colores para PowerShell
$Green = "`e[32m"
$Blue = "`e[34m"
$Yellow = "`e[33m"
$Red = "`e[31m"
$Reset = "`e[0m"

Write-Host "${Blue}📦 XAFRA-ADS PROJECT BACKUP${Reset}" -ForegroundColor Blue
Write-Host "============================================" -ForegroundColor Blue

$ErrorActionPreference = "Stop"

try {
    # Verificar directorio actual
    if (-not (Test-Path "webapp-xafra-ads")) {
        Write-Host "${Red}❌ Error: Este script debe ejecutarse desde el directorio raíz del proyecto${Reset}" -ForegroundColor Red
        exit 1
    }

    Write-Host "${Yellow}📁 Creando directorio de backup: $BackupPath${Reset}" -ForegroundColor Yellow
    New-Item -ItemType Directory -Path $BackupPath -Force | Out-Null

    # Archivos y directorios a respaldar
    $itemsToBackup = @(
        "webapp-xafra-ads",
        "db-access", 
        "commons-help",
        "pom.xml",
        "README.md",
        "PROJECT-STRUCTURE.md",
        "DEPLOYMENT-SUCCESS-LOG.md",
        "Dockerfile",
        "deploy.ps1",
        "deploy.sh",
        ".gitignore"
    )

    Write-Host "${Blue}📋 Copiando archivos del proyecto...${Reset}" -ForegroundColor Blue
    foreach ($item in $itemsToBackup) {
        if (Test-Path $item) {
            if (Test-Path $item -PathType Container) {
                Write-Host "  📁 $item"
                Copy-Item -Path $item -Destination $BackupPath -Recurse -Force
            } else {
                Write-Host "  📄 $item"
                Copy-Item -Path $item -Destination $BackupPath -Force
            }
        } else {
            Write-Host "  ⚠️  $item (no encontrado)" -ForegroundColor Yellow
        }
    }

    # Incluir WAR compilado si existe
    $warPath = "webapp-xafra-ads\target\ads-xafra.war"
    if (Test-Path $warPath) {
        Write-Host "${Green}📦 Incluyendo WAR compilado...${Reset}" -ForegroundColor Green
        Copy-Item -Path $warPath -Destination "$BackupPath\ads-xafra.war" -Force
    }

    # Información de versión y ambiente
    Write-Host "${Blue}📊 Generando información del sistema...${Reset}" -ForegroundColor Blue
    $systemInfo = @"
# XAFRA-ADS Project Backup Information
Fecha: $(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')
Usuario: $env:USERNAME
Computadora: $env:COMPUTERNAME
Directorio Origen: $(Get-Location)
Directorio Backup: $BackupPath

## Estado del Proyecto
- Java Version: $(if (Get-Command java -ErrorAction SilentlyContinue) { java -version 2>&1 | Select-Object -First 1 } else { "No encontrado" })
- Maven Version: $(if (Get-Command mvn -ErrorAction SilentlyContinue) { mvn -version 2>&1 | Select-Object -First 1 } else { "No encontrado" })
- Docker Version: $(if (Get-Command docker -ErrorAction SilentlyContinue) { docker --version } else { "No encontrado" })
- Git Commit: $(if (Get-Command git -ErrorAction SilentlyContinue) { git rev-parse HEAD 2>$null } else { "No encontrado" })

## Archivos Incluidos
$($itemsToBackup | ForEach-Object { "- $_" } | Out-String)

## Producción
- URL: https://xafra-ads-697203931362.us-central1.run.app
- Cloud Run: Activo
- Base de Datos: 34.28.245.62:5432/xafra-ads

## Scripts de Despliegue
- PowerShell: deploy.ps1
- Bash: deploy.sh

Backup completado exitosamente.
"@

    $systemInfo | Out-File -FilePath "$BackupPath\BACKUP-INFO.md" -Encoding UTF8

    # Comprimir si se solicita
    if ($Compress) {
        Write-Host "${Blue}🗜️  Comprimiendo backup...${Reset}" -ForegroundColor Blue
        $zipPath = "$BackupPath.zip"
        Compress-Archive -Path "$BackupPath\*" -DestinationPath $zipPath -Force
        Remove-Item -Path $BackupPath -Recurse -Force
        Write-Host "${Green}✅ Backup comprimido creado: $zipPath${Reset}" -ForegroundColor Green
    } else {
        Write-Host "${Green}✅ Backup creado en: $BackupPath${Reset}" -ForegroundColor Green
    }

    # Resumen
    Write-Host ""
    Write-Host "${Green}🎉 BACKUP COMPLETADO EXITOSAMENTE${Reset}" -ForegroundColor Green
    Write-Host "============================================" -ForegroundColor Green
    if ($Compress) {
        Write-Host "📦 Archivo: $zipPath.zip"
    } else {
        Write-Host "📁 Directorio: $BackupPath"
    }
    Write-Host "📊 Información: BACKUP-INFO.md"
    Write-Host "🚀 Estado: Listo para restaurar"

} catch {
    Write-Host "${Red}❌ Error durante el backup: $($_.Exception.Message)${Reset}" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "${Blue}💡 Para restaurar: Copiar contenido del backup al directorio de trabajo${Reset}" -ForegroundColor Cyan
Write-Host "${Blue}🚀 Para desplegar: .\deploy.ps1${Reset}" -ForegroundColor Cyan
