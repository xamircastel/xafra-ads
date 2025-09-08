# 📁 XAFRA-ADS - Estructura del Proyecto

## 🏗️ Arquitectura General

```
xafra-ads-vf/
├── 📄 README.md                          # Documentación principal
├── � API-DOCUMENTATION.md               # Documentación completa de APIs
├── 📋 XAFRA-ADS-APIs-Collection.postman_collection.json # Collection Postman
├── �🚀 deploy.sh                          # Script de despliegue (Linux/Mac)
├── 🚀 deploy.ps1                         # Script de despliegue (Windows)
├── 📦 backup-project.ps1                 # Script de respaldo automático
├── 📋 DEPLOYMENT-SUCCESS-LOG.md          # Log del despliegue exitoso
├── 📋 PROJECT-STRUCTURE.md               # Este archivo
├── 📋 CLEANUP-SUMMARY.md                 # Resumen de organización
├── 🗃️ DatabaseConnectivityTest.java      # Test de conectividad BD
│
└── webapp-xafra-ads/                     # 📦 Aplicación principal
    ├── 🐳 Dockerfile                     # Configuración Docker optimizada
    ├── ☁️ app.yaml                       # Configuración App Engine (respaldo)
    ├── 📊 pom.xml                        # Dependencias Maven
    ├── 🎯 target/ads-xafra.war           # WAR compilado listo para deploy
    │
    ├── src/main/
    │   ├── java/com/develop/job/          # 📋 Código fuente Java
    │   │   ├── SimpleRestApplication.java # Aplicación principal Spring Boot
    │   │   ├── auth/                      # 🔐 Autenticación y filtros
    │   │   ├── ads/                       # 📢 Motor de publicidad
    │   │   ├── controller/                # 🎮 Controladores REST
    │   │   └── util/                      # 🛠️ Utilidades
    │   │
    │   └── resources/
    │       ├── application.properties          # Configuración base
    │       ├── application-prod.properties     # 🌐 Configuración producción
    │       └── logback.xml                     # Configuración logs
    │
    └── WEB-INF/
        └── classes/                       # Clases compiladas
```

## 🌐 **URLs de Producción**

### 🎯 **Dominio Principal (Recomendado)**
- **API Base**: https://apis.xafra-ads.com
- **Health Check**: https://apis.xafra-ads.com/actuator/health
- **Database Test**: https://apis.xafra-ads.com/v1/db/health
- **Postman Collection**: Importar XAFRA-ADS-APIs-Collection.postman_collection.json

### ☁️ **Cloud Run Directo (Backup)**
- **URL Directa**: https://xafra-ads-697203931362.us-central1.run.app
- **Health Check**: https://xafra-ads-697203931362.us-central1.run.app/actuator/health

## 🔧 Componentes Clave

### 🐳 **Dockerfile** (Optimizado para Cloud Run)
- Base: OpenJDK 8 Alpine
- Lazy initialization habilitada
- Optimizaciones JVM para GCP
- Health checks configurados

### ⚙️ **application-prod.properties**
- Puerto dinámico: `${PORT:8080}`
- Conexión PostgreSQL con variables de entorno
- Endpoints de salud habilitados
- Logging optimizado para GCP

### 🚀 **Scripts de Despliegue**
- `deploy.sh`: Para Linux/Mac
- `deploy.ps1`: Para Windows PowerShell
- Automatización completa del pipeline

## 📊 **Configuración de Producción**

### 🗄️ Base de Datos
- **Tipo:** PostgreSQL 13.21 en GCP
- **Host:** 34.28.245.62:5432
- **Base:** xafra-ads
- **Usuario:** postgres

### ☁️ Cloud Run
- **Región:** us-central1
- **Recursos:** 2GB RAM, 2 vCPU
- **Escalado:** 0-10 instancias
- **Timeout:** 15 minutos

## 🔄 **Pipeline de Despliegue**

1. **Build** → Docker construye imagen optimizada
2. **Push** → Sube a Google Container Registry
3. **Deploy** → Despliega en Cloud Run
4. **Verify** → Verifica endpoints de salud

## 🎯 **URLs de Producción**

- **App:** https://xafra-ads-697203931362.us-central1.run.app
- **Health:** https://xafra-ads-697203931362.us-central1.run.app/actuator/health

## 🔍 **Monitoreo y Logs**

```bash
# Ver logs en tiempo real
gcloud logging read "resource.type=cloud_run_revision AND resource.labels.service_name=xafra-ads" --limit=50

# Estado del servicio
gcloud run services describe xafra-ads --region=us-central1

# Métricas
gcloud monitoring metrics list --filter="resource.type=cloud_run_revision"
```

## 🛠️ **Comandos Útiles**

```bash
# Despliegue rápido
./deploy.ps1

# Solo redeploy (sin rebuild)
./deploy.ps1 -SkipBuild -SkipPush

# Ver logs en vivo
gcloud logging tail "resource.type=cloud_run_revision"

# Escalar manualmente
gcloud run services update xafra-ads --max-instances=20 --region=us-central1
```
