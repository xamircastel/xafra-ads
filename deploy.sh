#!/bin/bash

# 🚀 XAFRA-ADS Deployment Script
# Automatiza el despliegue completo eecho ""
echo "🎉 ¡Despliegue completado exitosamente!"
echo "=================================================="
echo "🌐 URLs de Producción:"
echo "   📍 Dominio Principal: https://apis.xafra-ads.com"
echo "   📍 Cloud Run Directo: $SERVICE_URL"
echo ""
echo "🔍 Health Checks:"
echo "   🩺 Health: https://apis.xafra-ads.com/actuator/health"
echo "   🗄️ Database: https://apis.xafra-ads.com/v1/db/health"
echo ""
echo "📋 Postman Collection: XAFRA-ADS-APIs-Collection.postman_collection.json"
echo "📊 Logs: gcloud logging read \"resource.type=cloud_run_revision AND resource.labels.service_name=$SERVICE_NAME\" --limit=50"le Cloud Run
# Autor: XafraTech Team
# Fecha: 2025-09-08

set -e  # Exit on any error

echo "🚀 Iniciando despliegue automático de XAFRA-ADS..."
echo "=================================================="

# Variables de configuración
PROJECT_ID="xafra-ads"
SERVICE_NAME="xafra-ads"
REGION="us-central1"
IMAGE_NAME="gcr.io/${PROJECT_ID}/${SERVICE_NAME}"

# Credenciales de Base de Datos
DB_HOST="34.28.245.62"
DB_PORT="5432"
DB_NAME="xafra-ads"
DB_USER="postgres"
DB_PASSWORD="XafraTech2025!"

echo "📋 Configuración:"
echo "   Proyecto: ${PROJECT_ID}"
echo "   Servicio: ${SERVICE_NAME}"
echo "   Región: ${REGION}"
echo "   Imagen: ${IMAGE_NAME}"
echo ""

# Verificar que estamos en el directorio correcto
if [ ! -f "webapp-xafra-ads/Dockerfile" ]; then
    echo "❌ Error: No se encuentra webapp-xafra-ads/Dockerfile"
    echo "   Ejecuta este script desde el directorio raíz del proyecto"
    exit 1
fi

# Verificar que gcloud está configurado
echo "🔧 Verificando configuración de gcloud..."
gcloud config get-value project > /dev/null 2>&1 || {
    echo "❌ Error: gcloud no está configurado"
    echo "   Ejecuta: gcloud auth login && gcloud config set project ${PROJECT_ID}"
    exit 1
}

# Verificar que Docker está corriendo
echo "🐳 Verificando Docker..."
docker info > /dev/null 2>&1 || {
    echo "❌ Error: Docker no está corriendo"
    exit 1
}

# Paso 1: Construir imagen Docker
echo "🔨 Construyendo imagen Docker..."
cd webapp-xafra-ads
docker build -t ${IMAGE_NAME} .
cd ..

# Paso 2: Subir imagen a Container Registry
echo "📤 Subiendo imagen a Container Registry..."
docker push ${IMAGE_NAME}

# Paso 3: Desplegar en Cloud Run
echo "🚀 Desplegando en Cloud Run..."
gcloud run deploy ${SERVICE_NAME} \
    --image ${IMAGE_NAME} \
    --region ${REGION} \
    --platform managed \
    --port 8080 \
    --memory 2Gi \
    --cpu 2 \
    --max-instances 10 \
    --timeout 900 \
    --execution-environment gen2 \
    --set-env-vars="DB_HOST=${DB_HOST},DB_PORT=${DB_PORT},DB_NAME=${DB_NAME},DB_USER=${DB_USER},DB_PASSWORD=${DB_PASSWORD},SPRING_PROFILES_ACTIVE=prod" \
    --allow-unauthenticated

# Paso 4: Verificar el despliegue
echo "✅ Obteniendo URL del servicio..."
SERVICE_URL=$(gcloud run services describe ${SERVICE_NAME} --region=${REGION} --format='value(status.url)')

echo ""
echo "🎉 ¡Despliegue completado exitosamente!"
echo "=================================================="
echo "📍 URL del servicio: ${SERVICE_URL}"
echo "🔍 Health check: ${SERVICE_URL}/actuator/health"
echo "📊 Logs: gcloud logging read \"resource.type=cloud_run_revision AND resource.labels.service_name=${SERVICE_NAME}\" --limit=50"
echo ""
echo "💡 Para verificar el estado:"
echo "   gcloud run services describe ${SERVICE_NAME} --region=${REGION}"
echo ""
