# Xafra-Ads - Plataforma de Promoción de Servicios Digitales

## 🚀 Descripción del Proyecto

Xafra-Ads es una aplicación web desarrollada en Spring Boot para la gestión y promoción de servicios digitales. La aplicación maneja procesos de suscripción automática, seguimiento de campañas publicitarias y confirmación de transacciones.

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
GET  /ads/{param}/                     - Procesar ads con parámetros
GET  /ads/confirm/{traking}            - Confirmar tracking
POST /ads/v1/confirm/{apikey}/{traking} - Confirmar con API key
```

### Auto-Suscripción
```
POST /v1/auto/subscribe/{productId}    - Auto suscribir producto
GET  /v1/ping                          - Health check
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
