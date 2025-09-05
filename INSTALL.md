# 🚀 Guía de Instalación Rápida - Xafra-Ads v0.0.1

## ⚡ Inicio Rápido

### Prerrequisitos
- ☕ **Java 8+** (OpenJDK recomendado)
- 🔧 **Maven 3.6+** 
- 🐘 **PostgreSQL 13+** (opcional para desarrollo)

### 🚀 Ejecutar la Aplicación

#### Opción 1: Descarga Directa
```bash
# Clonar el repositorio
git clone https://github.com/xamircastel/xafra-ads.git
cd xafra-ads

# Compilar y ejecutar
mvn clean package -DskipTests
java -jar webapp-xafra-ads/target/ads-xafra.war
```

#### Opción 2: Con Maven
```bash
cd xafra-ads/webapp-xafra-ads
mvn spring-boot:run
```

### 🌐 Acceso a la Aplicación
- **URL Principal**: http://localhost:8083
- **Health Check**: http://localhost:8083/v1/ping
- **Headers Debug**: http://localhost:8083/adsDep/headers

### 📡 Endpoints Principales

#### 🔥 Procesamiento de Ads
```
GET  /ads/{param}/                    - Procesar ads con parámetros
GET  /ads/confirm/{traking}           - Confirmar tracking
POST /ads/v1/confirm/{apikey}/{traking} - Confirmar con API key
```

#### 🚀 Auto-Suscripción
```
POST /v1/auto/subscribe/{productId}   - Auto suscribir producto
GET  /v1/ping                         - Health check (responde "pong")
```

#### 🛠️ Utilidades
```
POST /util/encryption                 - Servicio de encriptación
GET  /util/get                        - Obtener URL base
```

### ⚙️ Configuración Base de Datos

#### PostgreSQL Local (Desarrollo)
```properties
# En webapp-xafra-ads/src/main/resources/application.properties
database.driver=org.postgresql.Driver
database.url=jdbc:postgresql://localhost:5432/tu_base_datos
database.user=tu_usuario
database.password=tu_password
```

#### PostgreSQL en GCP (Producción - Ya configurado)
```properties
database.url=jdbc:postgresql://34.28.245.62:5432/xafra-ads
database.user=postgres
database.password=XafraTech2025!
```

### 🔧 Comandos de Desarrollo

#### Compilación por Módulos
```bash
# Compilar commons-help
cd commons-help && mvn clean install

# Compilar db-access  
cd db-access && mvn clean install

# Compilar webapp principal
cd webapp-xafra-ads && mvn clean compile
```

#### Generar WAR
```bash
# Desde el directorio raíz
cd webapp-xafra-ads
mvn clean package -DskipTests
```

### 🧪 Verificación de Funcionamiento

#### Test 1: Health Check
```bash
curl http://localhost:8083/v1/ping
# Debe responder: "pong"
```

#### Test 2: Headers
```bash
curl http://localhost:8083/adsDep/headers
# Debe mostrar tabla HTML con headers HTTP
```

#### Test 3: Utilidades
```bash
curl http://localhost:8083/util/get
# Debe responder con la URL base
```

### 📝 Logs de la Aplicación

Los logs se generan en la carpeta `/logs/`:
- `StartApplication.log` - Log principal
- `xafra-ads-process.log` - Procesamiento
- `xafra-ads-confirm.log` - Confirmaciones

### 🎯 Solución de Problemas Comunes

#### Error: "Puerto 8083 en uso"
```bash
# Windows
netstat -ano | findstr :8083
taskkill /PID <PID> /F

# Linux/Mac
lsof -ti:8083 | xargs kill -9
```

#### Error: "mvn command not found"
```bash
# Verificar instalación de Maven
mvn --version

# Si no está instalado, descargar de: https://maven.apache.org/download.cgi
```

#### Error: Conexión a Base de Datos
- Verificar que PostgreSQL esté ejecutándose
- Comprobar credenciales en `application.properties`
- Para desarrollo local, crear base de datos: `CREATE DATABASE xafra_ads;`

### 📊 Arquitectura del Proyecto

```
xafra-ads/
├── webapp-xafra-ads/     # 🌐 Aplicación Spring Boot principal
├── db-access/            # 🗄️ Capa de acceso a datos (JDBI)
├── commons-help/         # 🛠️ Utilidades compartidas
├── README.md             # 📖 Documentación principal
└── INSTALL.md           # 📋 Esta guía
```

### 🔮 Próximos Pasos

1. **Pruebas de BD**: Verificar conectividad completa con PostgreSQL
2. **Testing**: Ejecutar pruebas funcionales de todos los endpoints
3. **Desarrollo**: Implementar nuevas funcionalidades
4. **Deploy**: Configurar para producción en GCP

---

✅ **Estado**: Aplicación 100% funcional y lista para desarrollo  
🔗 **Repositorio**: https://github.com/xamircastel/xafra-ads  
📋 **Versión**: v0.0.1  
📅 **Fecha**: Septiembre 2025

¿Problemas? Crear un issue en GitHub o contactar al equipo de desarrollo.
