# 🚀 XAFRA-ADS - Motor de Publicidad

[![Deployment Status](https://img.shields.io/badge/Deployment-✅%20LIVE-brightgreen)](https://xafra-ads-697203931362.us-central1.run.app)
[![Platform](https://img.shields.io/badge/Platform-Google%20Cloud%20Run-blue)](https://cloud.google.com/run)
[![Java](https://img.shields.io/badge/Java-8-orange)](https://openjdk.java.net/projects/jdk8/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.18-green)](https://spring.io/projects/spring-boot)

Motor de publicidad para gestión de anuncios y tracking de conversiones, optimizado para 10,000+ visitas diarias.

## � **En Producción**

- **URL:** https://xafra-ads-697203931362.us-central1.run.app
- **Health Check:** https://xafra-ads-697203931362.us-central1.run.app/actuator/health
- **Plataforma:** Google Cloud Run
- **Estado:** ✅ ACTIVO

## 🚀 **Despliegue Rápido**

### Windows PowerShell:
```powershell
.\deploy.ps1
```

### Linux/Mac:
```bash
chmod +x deploy.sh
./deploy.sh
```

## 📋 **Requisitos**

- ✅ Docker Desktop
- ✅ Google Cloud SDK
- ✅ Proyecto GCP configurado (`xafra-ads`)
- ✅ Credenciales de acceso a BD PostgreSQL

## ⚙️ **Configuración**

### �️ Base de Datos
```properties
Host: 34.28.245.62
Puerto: 5432
Base de datos: xafra-ads
Usuario: postgres
Contraseña: XafraTech2025!
```

### ☁️ Cloud Run
- **Región:** us-central1
- **Memoria:** 2GB
- **CPU:** 2 vCPU
- **Escalado:** 0-10 instancias automático

## 🏗️ **Arquitectura**

```
📱 Cliente → 🌐 Cloud Run → 🗄️ PostgreSQL
                ↓
           📊 Logs & Métricas
```

### Módulos del Proyecto
- **`webapp-xafra-ads`**: Aplicación principal Spring Boot con endpoints REST
- **`db-access`**: Capa de acceso a datos utilizando JDBI 3.x
- **`commons-help`**: Módulo de utilidades compartidas (encriptación, logging, herramientas)

### Stack Tecnológico
- **Framework**: Spring Boot 2.7.18
- **Java**: OpenJDK 1.8.0_292
- **Base de Datos**: PostgreSQL 13 (Google Cloud Platform)
- **ORM**: JDBI 3.x
- **Build Tool**: Apache Maven 3.8.8
- **Logging**: Logback con configuración personalizada
- **Template Engine**: Thymeleaf
- **Utilities**: Lombok, Google Guava

## 📚 **Documentación**

- 📋 [**Estructura del Proyecto**](PROJECT-STRUCTURE.md)
- 🚀 [**Log de Despliegue Exitoso**](DEPLOYMENT-SUCCESS-LOG.md)
- 🔧 Scripts automatizados: `deploy.ps1` / `deploy.sh`

## 🌐 Endpoints Principales

### Procesamiento de Ads
```
GET  /ads/{param}/                     - Procesar ads con parámetros encriptados
     Query params soportados: ClickId, clickId, ClickID, clickID, tracker
     Ejemplo: /ads/ABC123/?clickId=TRACK_001&utm_source=google
     
GET  /ads/confirm/{tracking}            - Confirmar conversión y ejecutar postback
POST /ads/v1/confirm/{apikey}/{tracking} - Confirmación autenticada con API key
```

### Auto-Suscripción Masiva
```
POST /v1/auto/subscribe/{productId}/{hour}/{timeSleep}/{source}/{limit}
     - productId: ID del producto a suscribir
     - hour: Hora de procesamiento (default: "23")
     - timeSleep: Tiempo entre requests en ms (default: 10)
     - source: Código fuente (default: "AA230")
     - limit: Límite de números a procesar

GET  /v1/ping                          - Health check (responde "pong")
```

### Testing y Monitoreo
```
GET  /v1/db/test-connection             - Test conectividad PostgreSQL
GET  /v1/db/test-data-access            - Test acceso a datos JDBI
GET  /v1/db/test-campaigns              - Validar tabla campaigns (2.9M+ registros)
GET  /v1/db/health                      - Health check completo de BD
GET  /actuator/health                   - Health check Spring Boot (PRODUCCIÓN)
```

### Utilidades
```
POST /util/encryption                  - Servicio de encriptación
GET  /util/get                         - Obtener URL base
```

### Compras
```
POST /path/ping                        - Health check
POST /path/buy                         - Obtener compra
POST /path/newbuy                      - Crear nueva compra
```

## 🎯 **Características Principales**

- ✅ **Motor de Publicidad:** Gestión completa de anuncios
- ✅ **Tracking de Conversiones:** Sistema de seguimiento Costa Rica-Kolbi
- ✅ **APIs REST:** Endpoints para país/operador
- ✅ **Health Checks:** Monitoreo automático de salud
- ✅ **Escalado Automático:** Hasta 10,000+ visitas/día
- ✅ **Logs Centralizados:** Google Cloud Logging
- ✅ **Auto-Suscripción Masiva:** Multi-threading para suscripciones

### 📊 Estadísticas Activas
- 🗄️ **2,970,685** campañas procesadas en producción
- 🌐 **PostgreSQL 13.21** en Google Cloud Platform (34.28.245.62)
- ⚡ **Multi-threading** para auto-suscripciones masivas
- 🔒 **Encriptación AES** para parámetros sensibles

## 🔧 **Comandos Útiles**

```bash
# Ver estado del servicio
gcloud run services describe xafra-ads --region=us-central1

# Ver logs en tiempo real
gcloud logging tail "resource.type=cloud_run_revision AND resource.labels.service_name=xafra-ads"

# Escalar manualmente
gcloud run services update xafra-ads --max-instances=20 --region=us-central1

# Desplegar solo cambios de configuración
gcloud run services update xafra-ads --set-env-vars="NEW_VAR=value" --region=us-central1
```

## 📊 **Monitoreo**

### Logs:
```bash
gcloud logging read "resource.type=cloud_run_revision AND resource.labels.service_name=xafra-ads" --limit=50
```

### Métricas:
- CPU, Memoria, Requests/seg
- Latencia de respuesta
- Errores 4xx/5xx

## 🛠️ **Desarrollo Local**

### Prerrequisitos
- Java 8 o superior
- Maven 3.6+
- Docker Desktop
- PostgreSQL 13+ (para base de datos)

### Compilación
```bash
# Compilar todos los módulos
mvn clean compile

# Generar WAR ejecutable
mvn clean package -DskipTests

# Construir y probar localmente con Docker
docker build -t xafra-ads .
docker run -p 8080:8080 -e SPRING_PROFILES_ACTIVE=prod xafra-ads
```

### Ejecución Local
```bash
# Opción 1: Con Maven
mvn spring-boot:run

# Opción 2: Con Java directamente
java -jar webapp-xafra-ads/target/ads-xafra.war
```

La aplicación estará disponible en: `http://localhost:8083`

## 📝 Estructura del Proyecto

```
xafra-ads/
├── webapp-xafra-ads/          # Aplicación principal Spring Boot
│   ├── src/main/java/         # Código fuente Java
│   ├── src/main/resources/    # Recursos (properties, templates)
│   └── pom.xml               # Configuración Maven
├── db-access/                 # Capa de acceso a datos
│   ├── src/main/java/         # DAOs, entidades, interfaces JDBI
│   └── pom.xml               # Configuración Maven
├── commons-help/              # Utilidades compartidas
│   ├── src/main/java/         # Herramientas, encriptación, logging
│   └── pom.xml               # Configuración Maven
├── deploy.ps1                 # Script despliegue PowerShell
├── deploy.sh                  # Script despliegue Bash
├── Dockerfile                 # Configuración contenedor producción
├── PROJECT-STRUCTURE.md       # Documentación arquitectura
└── DEPLOYMENT-SUCCESS-LOG.md  # Log despliegue exitoso
```

## 🔒 **Seguridad**

- ✅ Conexión segura a BD PostgreSQL
- ✅ Variables de entorno para credenciales
- ✅ HTTPS automático en Cloud Run
- ✅ Filtros de autenticación configurados
- ✅ Encriptación AES para parámetros sensibles

### Variables de Entorno
```properties
server.port=8083
password.encription=%a7ra-.passd0474
user.encription=xafra2-ads-encrytion3
limit.subscribe=250
limit.topup=10
```

## 📈 **Performance**

- **Capacidad:** 10,000+ visitas/día
- **Startup Time:** ~30 segundos (optimizado)
- **Escalado:** Automático basado en tráfico
- **Disponibilidad:** 99.9% SLA de Google Cloud
- **Memoria:** 2GB RAM por instancia
- **CPU:** 2 vCPU por instancia

## 🆘 **Solución de Problemas**

### Deployment falla:
1. Verificar credenciales: `gcloud auth list`
2. Verificar proyecto: `gcloud config get-value project`
3. Verificar Docker: `docker info`

### Aplicación no responde:
1. Verificar logs: `gcloud logging tail`
2. Verificar health: `/actuator/health`
3. Verificar BD: Test de conectividad

### BD no conecta:
1. Verificar credenciales en application-prod.properties
2. Probar desde local: `psql -h 34.28.245.62 -p 5432 -U postgres -d xafra-ads`
3. Verificar firewall GCP

---

**🏢 XafraTech © 2025** | Motor de Publicidad en Producción
├── .gitignore                # Archivos ignorados por Git
└── README.md                 # Este archivo
```

## 🔧 Desarrollo

### Compilación y Testing
```bash
# Compilar módulos individuales
cd commons-help && mvn clean install
cd db-access && mvn clean install
cd webapp-xafra-ads && mvn clean compile

# Ejecutar tests
mvn test
```

### Logging
El sistema utiliza Logback con archivos de log separados:
- `StartApplication.log` - Log principal de la aplicación
- `xafra-ads-process.log` - Log de procesamiento de ads
- `xafra-ads-confirm.log` - Log de confirmaciones

## 🌟 Estado del Proyecto

✅ **Compilación**: Exitosa sin errores  
✅ **Dependencias**: Todas resueltas correctamente  
✅ **Endpoints**: Funcionales y verificados  
✅ **Logging**: Configurado y operativo  
✅ **Build**: WAR ejecutable generado  

## 🔮 Próximos Pasos

1. **Pruebas de Conectividad**: Validar conexión completa con PostgreSQL
2. **Testing de Endpoints**: Pruebas funcionales de todos los servicios
3. **Optimización**: Mejoras de escalabilidad y rendimiento
4. **Nuevas Funcionalidades**: Implementación de features adicionales
5. **Despliegue**: Configuración para Google Cloud Platform

## 📞 Contacto

Para más información sobre el proyecto Xafra-Ads, contactar al equipo de desarrollo.

---

**Versión**: 0.0.1  
**Fecha de última actualización**: Septiembre 2025  
**Estado**: Funcional y listo para desarrollo adicional
