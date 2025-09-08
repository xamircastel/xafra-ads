# 🚀 XAFRA-ADS Deployment Script para Windows PowerShell
# Automatiza el despliegue completo en Google Cloud Run
# Autor: XafraTech Team
# Fecha: 2025-09-08

param(
    [switch]$SkipBuild = $false,
    [switch]$SkipPush = $false
)

$ErrorActionPreference = "Stop"

Write-Host "🚀 Iniciando despliegue automático de XAFRA-ADS..." -ForegroundColor Green
Write-Host "==================================================" -ForegroundColor Green

# Variables de configuración
$PROJECT_ID = "xafra-ads"
$SERVICE_NAME = "xafra-ads"
$REGION = "us-central1"
$IMAGE_NAME = "gcr.io/$PROJECT_ID/$SERVICE_NAME"

# Credenciales de Base de Datos
$DB_HOST = "34.28.245.62"
$DB_PORT = "5432"
$DB_NAME = "xafra-ads"
$DB_USER = "postgres"
$DB_PASSWORD = "XafraTech2025!"

Write-Host "📋 Configuración:" -ForegroundColor Cyan
Write-Host "   Proyecto: $PROJECT_ID" -ForegroundColor White
Write-Host "   Servicio: $SERVICE_NAME" -ForegroundColor White
Write-Host "   Región: $REGION" -ForegroundColor White
Write-Host "   Imagen: $IMAGE_NAME" -ForegroundColor White
Write-Host ""

# Verificar que estamos en el directorio correcto
if (-not (Test-Path "webapp-xafra-ads\Dockerfile")) {
    Write-Host "❌ Error: No se encuentra webapp-xafra-ads\Dockerfile" -ForegroundColor Red
    Write-Host "   Ejecuta este script desde el directorio raíz del proyecto" -ForegroundColor Yellow
    exit 1
}

# Verificar gcloud
Write-Host "🔧 Verificando configuración de gcloud..." -ForegroundColor Yellow
try {
    $currentProject = gcloud config get-value project 2>$null
    if (-not $currentProject) {
        throw "gcloud no configurado"
    }
} catch {
    Write-Host "❌ Error: gcloud no está configurado" -ForegroundColor Red
    Write-Host "   Ejecuta: gcloud auth login && gcloud config set project $PROJECT_ID" -ForegroundColor Yellow
    exit 1
}

# Verificar Docker
Write-Host "🐳 Verificando Docker..." -ForegroundColor Yellow
try {
    docker info > $null 2>&1
} catch {
    Write-Host "❌ Error: Docker no está corriendo" -ForegroundColor Red
    exit 1
}

# Paso 1: Construir imagen Docker
if (-not $SkipBuild) {
    Write-Host "🔨 Construyendo imagen Docker..." -ForegroundColor Yellow
    Set-Location webapp-xafra-ads
    docker build -t $IMAGE_NAME .
    Set-Location ..
} else {
    Write-Host "⏭️ Omitiendo construcción de imagen..." -ForegroundColor Yellow
}

# Paso 2: Subir imagen
if (-not $SkipPush) {
    Write-Host "📤 Subiendo imagen a Container Registry..." -ForegroundColor Yellow
    docker push $IMAGE_NAME
} else {
    Write-Host "⏭️ Omitiendo push de imagen..." -ForegroundColor Yellow
}

# Paso 3: Desplegar en Cloud Run
Write-Host "🚀 Desplegando en Cloud Run..." -ForegroundColor Yellow
$envVars = "DB_HOST=$DB_HOST,DB_PORT=$DB_PORT,DB_NAME=$DB_NAME,DB_USER=$DB_USER,DB_PASSWORD=$DB_PASSWORD,SPRING_PROFILES_ACTIVE=prod"

gcloud run deploy $SERVICE_NAME `
    --image $IMAGE_NAME `
    --region $REGION `
    --platform managed `
    --port 8080 `
    --memory 2Gi `
    --cpu 2 `
    --max-instances 10 `
    --timeout 900 `
    --execution-environment gen2 `
    --set-env-vars="$envVars" `
    --allow-unauthenticated

# Paso 4: Obtener información del despliegue
Write-Host "✅ Obteniendo información del servicio..." -ForegroundColor Yellow
$SERVICE_URL = gcloud run services describe $SERVICE_NAME --region=$REGION --format='value(status.url)'

Write-Host ""
Write-Host "🎉 ¡Despliegue completado exitosamente!" -ForegroundColor Green
Write-Host "==================================================" -ForegroundColor Green
Write-Host "📍 URL del servicio: $SERVICE_URL" -ForegroundColor Cyan
Write-Host "🔍 Health check: $SERVICE_URL/actuator/health" -ForegroundColor Cyan
Write-Host "📊 Logs: gcloud logging read `"resource.type=cloud_run_revision AND resource.labels.service_name=$SERVICE_NAME`" --limit=50" -ForegroundColor White
Write-Host ""
Write-Host "💡 Para verificar el estado:" -ForegroundColor Yellow
Write-Host "   gcloud run services describe $SERVICE_NAME --region=$REGION" -ForegroundColor White
Write-Host ""

# Ejemplos de uso
Write-Host "📖 Ejemplos de uso:" -ForegroundColor Magenta
Write-Host "   .\deploy.ps1                    # Despliegue completo" -ForegroundColor White
Write-Host "   .\deploy.ps1 -SkipBuild         # Solo push y deploy" -ForegroundColor White
Write-Host "   .\deploy.ps1 -SkipBuild -SkipPush  # Solo deploy" -ForegroundColor White
