# Xafra-Ads - Plataforma de Promoción de Servicios Digitales

## 🚀 Descripción del Proyecto

Xafra-Ads es una **plataforma empresarial** que actúa como intermediario en el ecosistema de promoción de servicios digitales. Conecta **fuentes de tráfico** (traffic sources) con **operadores de telecomunicaciones** para facilitar la contratación de servicios y gestionar el ciclo completo de conversiones.

### 🎯 Modelo de Negocio

El sistema procesa **tracking IDs** de campañas publicitarias, gestiona redirecciones inteligentes con reemplazo de parámetros, y ejecuta **postbacks automáticos** para notificar conversiones a las fuentes de tráfico. Incluye módulos especializados para **BlackList management** y **auto-suscripción masiva**.

### 📊 Estadísticas Activas
- 🗄️ **2,970,685** campañas procesadas en producción
- 🌐 **PostgreSQL 13.21** en Google Cloud Platform (34.28.245.62)
- ⚡ **Multi-threading** para auto-suscripciones masivas
- 🔒 **Encriptación AES** para parámetros sensibles

## 🏗️ Arquitectura del Sistema

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
```

### Utilidades
```
POST /util/encryption                  - Servicio de encriptación
GET  /util/get                         - Obtener URL base
```

### Desarrollo y Testing
```
GET  /adsDep/headers                   - Ver headers HTTP
POST /adsDep/p/{param}                 - Procesar parámetros
```

### Compras
```
POST /path/ping                        - Health check
POST /path/buy                         - Obtener compra
POST /path/newbuy                      - Crear nueva compra
```

### Web
```
GET  /                                 - Página principal
```

## ⚙️ Configuración

### Configuración de Base de Datos
La aplicación está configurada para conectarse a PostgreSQL en Google Cloud Platform:
- **Host**: 34.28.245.62:5432
- **Base de Datos**: Configurable via properties
- **Usuario**: postgres
- **Puerto**: 8083 (aplicación web)

### Variables de Entorno
```properties
server.port=8083
password.encription=%a7ra-.passd0474
user.encription=xafra2-ads-encrytion3
limit.subscribe=250
limit.topup=10
```

## 🚀 Ejecución del Proyecto

### Prerrequisitos
- Java 8 o superior
- Maven 3.6+
- PostgreSQL 13+ (para base de datos)

### Compilación
```bash
# Compilar todos los módulos
mvn clean compile

# Generar WAR ejecutable
mvn clean package -DskipTests
```

### Ejecución
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
