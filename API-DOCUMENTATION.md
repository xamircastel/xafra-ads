# 📋 XAFRA-ADS - Documentación de APIs

## 🌐 Información General

- **Dominio Principal:** https://apis.xafra-ads.com
- **URL Cloud Run:** https://xafra-ads-697203931362.us-central1.run.app
- **Versión:** v2.1.0-production
- **Postman Collection:** XAFRA-ADS-APIs-Collection.postman_collection.json

## 🚀 Quick Start

### 1. Importar Collection en Postman
1. Abrir Postman
2. Click en "Import" → "Choose Files"
3. Seleccionar `XAFRA-ADS-APIs-Collection.postman_collection.json`
4. ¡Listo! Todas las APIs estarán disponibles

### 2. Variables de Entorno
La collection incluye estas variables predefinidas:
- `{{base_url}}`: https://apis.xafra-ads.com
- `{{base_url_cloudrun}}`: URL de backup de Cloud Run
- `{{api_key}}`: Para endpoints autenticados
- `{{environment}}`: production

## 📚 Grupos de APIs

### 🚀 Health Checks
Endpoints para verificar el estado del sistema:
- **GET** `/actuator/health` - Spring Boot health check
- **GET** `/v1/db/health` - Health check de base de datos
- **GET** `/v1/ping` - Ping simple (responde "pong")

### 📱 Procesamiento de Ads
Motor principal de publicidad:
- **GET** `/ads/{param}/?clickId={id}&utm_source={source}` - Procesar ads
- **GET** `/ads/confirm/{tracking}` - Confirmar conversión
- **POST** `/ads/v1/confirm/{apikey}/{tracking}` - Confirmación autenticada

### 🤖 Auto-Suscripción Masiva
Sistema de suscripción automática:
- **POST** `/v1/auto/subscribe/{productId}/{hour}/{timeSleep}/{source}/{limit}`

### 🔍 Testing y Base de Datos
Endpoints de diagnóstico:
- **GET** `/v1/db/test-connection` - Test conectividad PostgreSQL
- **GET** `/v1/db/test-data-access` - Test acceso JDBI
- **GET** `/v1/db/test-campaigns` - Test tabla campaigns (2.9M+ registros)

### 🔧 Utilidades
Herramientas del sistema:
- **POST** `/util/encryption` - Encriptación AES
- **GET** `/util/get` - Obtener URL base

### 🛒 Sistema de Compras
Gestión de transacciones:
- **POST** `/path/ping` - Health check compras
- **POST** `/path/buy` - Obtener compra
- **POST** `/path/newbuy` - Crear nueva compra

### 🔧 Desarrollo y Debug
Para testing y desarrollo:
- **GET** `/adsDep/headers` - Ver headers HTTP
- **POST** `/adsDep/p/{param}` - Procesar parámetros debug

## 🎯 Ejemplos de Uso

### Verificar Estado del Sistema
```bash
# Health check principal
GET https://apis.xafra-ads.com/actuator/health

# Test de base de datos
GET https://apis.xafra-ads.com/v1/db/health
```

### Procesar un Anuncio
```bash
# Procesar ad con tracking
GET https://apis.xafra-ads.com/ads/ABC123/?clickId=TRACK_001&utm_source=google&utm_campaign=test_campaign

# Confirmar conversión
GET https://apis.xafra-ads.com/ads/confirm/TRACK_001
```

### Auto-Suscripción Masiva
```bash
# Iniciar auto-suscripción
POST https://apis.xafra-ads.com/v1/auto/subscribe/PROD001/23/10/AA230/250
Content-Type: application/json

{
    "phone_numbers": ["+50612345678", "+50687654321"],
    "campaign_id": "CAMP001"
}
```

### Servicio de Encriptación (Customer ID)
```bash
POST https://apis.xafra-ads.com/util/encryption
Content-Type: text/plain
user: xafra2-ads-encrytion3

# Body (texto plano):
14

# Respuesta: Texto encriptado para usar en URLs
```

**Importante:** Esta API requiere:
- **Body**: customer_id como texto plano (ej: "14")
- **Header 'user'**: xafra2-ads-encrytion3 (autenticación)
- **Content-Type**: text/plain
- **Respuesta**: String encriptado listo para URLs de campaña
```

## 🔒 Autenticación

### API Keys
Para endpoints que requieren autenticación:
1. Configurar variable `api_key` en Postman
2. Usar en endpoints como: `/ads/v1/confirm/{api_key}/{tracking}`

### Headers Recomendados
```
Content-Type: application/json
User-Agent: YourApp/1.0
X-API-Version: v2.1.0
```

## 📊 Monitoreo y Métricas

### Health Checks Automatizados
```javascript
// Script para Postman - Validación automática
pm.test("✅ API is healthy", function () {
    pm.expect(pm.response.code).to.equal(200);
    pm.expect(pm.response.responseTime).to.be.below(5000);
});

pm.test("🔗 Database is connected", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData.status).to.equal("UP");
});
```

### Performance Benchmarks
- **Response Time:** < 2 segundos (promedio)
- **Availability:** 99.9% uptime
- **Throughput:** 10,000+ requests/día
- **Database:** 2,970,685+ registros procesados

## 🎯 Códigos de Respuesta

### Exitosos
- **200 OK**: Request procesado correctamente
- **201 Created**: Recurso creado exitosamente
- **202 Accepted**: Request aceptado para procesamiento

### Errores Comunes
- **400 Bad Request**: Parámetros incorrectos
- **401 Unauthorized**: API key requerida o inválida
- **404 Not Found**: Endpoint no encontrado
- **500 Internal Server Error**: Error interno del servidor

### Respuestas Health Check
```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP",
      "details": {
        "database": "PostgreSQL",
        "validationQuery": "isValid()"
      }
    },
    "diskSpace": {
      "status": "UP",
      "details": {
        "total": 2147483648,
        "free": 1073741824,
        "threshold": 10485760,
        "exists": true
      }
    }
  }
}
```

## 🚀 Tips para Testing

### Variables Dinámicas en Postman
```javascript
// Pre-request script para generar IDs únicos
pm.globals.set("tracking_id", "TRACK_" + Math.random().toString(36).substr(2, 9));
pm.globals.set("timestamp", new Date().toISOString());
```

### Testing de Performance
```javascript
// Test script para medir performance
pm.test("⚡ Response time is acceptable", function () {
    pm.expect(pm.response.responseTime).to.be.below(2000);
});

pm.test("📊 Response size is reasonable", function () {
    pm.expect(pm.response.responseSize).to.be.below(50000);
});
```

### Automatización de Tests
```javascript
// Script para ejecutar tests en secuencia
const testRequests = [
    "/actuator/health",
    "/v1/ping",
    "/v1/db/health"
];

testRequests.forEach(endpoint => {
    pm.sendRequest({
        url: pm.variables.get("base_url") + endpoint,
        method: 'GET'
    }, function (err, response) {
        console.log(`✅ ${endpoint}: ${response.code}`);
    });
});
```

## 🔧 Configuración Avanzada

### Environments en Postman
Crear diferentes environments para:

#### Production Environment
```json
{
    "base_url": "https://apis.xafra-ads.com",
    "api_key": "prod_api_key",
    "environment": "production"
}
```

#### Development Environment
```json
{
    "base_url": "http://localhost:8083",
    "api_key": "dev_api_key",
    "environment": "development"
}
```

### Certificados SSL
Para testing de SSL:
- Verificar que el dominio `apis.xafra-ads.com` tiene certificado válido
- Configurar Postman para validar certificados SSL
- Usar HTTPS exclusivamente en producción

## 📋 Troubleshooting

### Problemas Comunes

#### API no responde
1. Verificar URL: `https://apis.xafra-ads.com/actuator/health`
2. Verificar conectividad de red
3. Revisar logs de Cloud Run: `gcloud logging tail`

#### Base de datos no conecta
1. Test: `/v1/db/test-connection`
2. Verificar credenciales en variables de entorno
3. Verificar firewall de PostgreSQL

#### Performance lenta
1. Verificar región (us-central1)
2. Revisar logs de aplicación
3. Escalar instancias si es necesario

### Comandos de Diagnóstico
```bash
# Verificar estado del servicio
gcloud run services describe xafra-ads --region=us-central1

# Ver logs en tiempo real
gcloud logging tail "resource.type=cloud_run_revision AND resource.labels.service_name=xafra-ads"

# Test de conectividad
curl -I https://apis.xafra-ads.com/actuator/health
```

---

**📅 Última actualización:** Septiembre 2025  
**🏢 XafraTech © 2025** | Motor de Publicidad APIs  
**🌐 Dominio:** apis.xafra-ads.com
