# 📋 XAFRA-ADS Postman Collection

## 🚀 Quick Start

### 1. Importar en Postman
1. Abrir Postman Desktop o Web
2. Click en **"Import"**
3. Seleccionar **"Choose Files"**
4. Buscar y seleccionar: `XAFRA-ADS-APIs-Collection.postman_collection.json`
5. Click **"Import"**

### 2. Configurar Variables
La collection viene con estas variables predefinidas:
- `{{base_url}}`: https://apis.xafra-ads.com
- `{{api_key}}`: your_api_key_here (cambiar por tu API key)
- `{{environment}}`: production

## 📚 Estructura de la Collection

### 🚀 Health Checks (3 endpoints)
- Spring Boot Health Check
- Database Health Check  
- Simple Ping

### 📱 Procesamiento de Ads (3 endpoints)
- Procesar Ad con Parámetros
- Confirmar Conversión
- Confirmar Conversión con API Key

### 🤖 Auto-Suscripción Masiva (1 endpoint)
- Iniciar Auto-Suscripción

### 🔍 Testing y Base de Datos (3 endpoints)
- Test Conexión BD
- Test Acceso a Datos
- Test Campañas (2.9M+ registros)

### 🔧 Utilidades (2 endpoints)
- Servicio de Encriptación
- Obtener URL Base

### 🛒 Sistema de Compras (3 endpoints)
- Health Check Compras
- Obtener Compra
- Crear Nueva Compra

### 🔧 Desarrollo y Debug (2 endpoints)
- Ver Headers HTTP
- Procesar Parámetros Debug

### 🌐 Web Interface (1 endpoint)
- Página Principal

## 🎯 Ejemplos de Uso

### Health Check Rápido
1. Seleccionar: **Health Checks** → **Spring Boot Health Check**
2. Click **"Send"**
3. Verificar respuesta: `{"status":"UP"}`

### Procesar un Anuncio
1. Seleccionar: **Procesamiento de Ads** → **Procesar Ad con Parámetros**
2. Modificar URL si es necesario: `/ads/ABC123/?clickId=TRACK_001`
3. Click **"Send"**

### Test de Base de Datos
1. Seleccionar: **Testing y Base de Datos** → **Database Health Check**
2. Click **"Send"**
3. Verificar conexión a PostgreSQL

## ⚙️ Configuración Avanzada

### Crear Environment
1. Click en el ícono de configuración (engranaje)
2. Seleccionar **"Manage Environments"**
3. Click **"Add"**
4. Crear environment con estas variables:

#### Production Environment
```json
{
  "base_url": "https://apis.xafra-ads.com",
  "api_key": "your_production_api_key",
  "environment": "production"
}
```

#### Development Environment
```json
{
  "base_url": "http://localhost:8083",
  "api_key": "your_development_api_key", 
  "environment": "development"
}
```

### Scripts de Testing Automático
La collection incluye scripts que se ejecutan automáticamente:

#### Pre-request Script
```javascript
// Se ejecuta antes de cada request
console.log('🚀 Ejecutando request a XAFRA-ADS API');
pm.globals.set('request_timestamp', new Date().toISOString());
```

#### Test Script
```javascript
// Se ejecuta después de cada response
pm.test('✅ Status code is success', function () {
    pm.expect(pm.response.code).to.be.oneOf([200, 201, 202]);
});

pm.test('⚡ Response time is acceptable', function () {
    pm.expect(pm.response.responseTime).to.be.below(10000);
});
```

## 🔒 Autenticación

### API Keys
Para endpoints que requieren autenticación:
1. Ir a la pestaña **"Variables"** de la collection
2. Cambiar `api_key` por tu clave real
3. Usar en endpoints como: `/ads/v1/confirm/{{api_key}}/TRACK_001`

### Headers de Seguridad
Algunos endpoints pueden requerir headers adicionales:
```
Authorization: Bearer {{api_key}}
X-API-Version: v2.1.0
User-Agent: YourApp/1.0
```

## 📊 Monitoreo y Testing

### Collection Runner
Para ejecutar tests automáticos:
1. Click en **"..."** en la collection
2. Seleccionar **"Run collection"**
3. Configurar número de iteraciones
4. Click **"Run XAFRA-ADS APIs Collection"**

### Monitoring
Configurar monitoring automático:
1. Click en **"Monitor"** en la collection
2. Configurar frecuencia (cada 5 minutos)
3. Seleccionar environment de producción
4. Configurar notificaciones por email

## 🚀 Testing de Performance

### Variables Dinámicas
```javascript
// Pre-request script para generar datos únicos
pm.globals.set("tracking_id", "TRACK_" + Math.random().toString(36).substr(2, 9));
pm.globals.set("phone_number", "+506" + Math.floor(Math.random() * 90000000 + 10000000));
```

### Assertions Avanzadas
```javascript
// Validar estructura de respuesta
pm.test("Response has required fields", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.have.property('status');
    pm.expect(jsonData).to.have.property('timestamp');
});

// Validar performance
pm.test("Response time under 2 seconds", function () {
    pm.expect(pm.response.responseTime).to.be.below(2000);
});
```

## 🔧 Troubleshooting

### Error: "Could not resolve hostname"
1. Verificar conectividad a internet
2. Probar URL manualmente: https://apis.xafra-ads.com/actuator/health
3. Verificar configuración de proxy en Postman

### Error: "SSL certificate problem"
1. Ir a Settings → General
2. Desactivar **"SSL certificate verification"** (solo para testing)
3. O configurar certificados apropiados

### Error: "Timeout"
1. Aumentar timeout en Settings → General
2. Verificar que el servicio esté activo
3. Usar URL de backup de Cloud Run

## 📈 Métricas y Reportes

### Generar Reportes
1. Ejecutar collection con Collection Runner
2. Click en **"Export Results"**
3. Seleccionar formato JSON o HTML
4. Analizar métricas de performance

### KPIs Importantes
- **Success Rate**: > 99%
- **Average Response Time**: < 2 segundos
- **Max Response Time**: < 10 segundos
- **Availability**: 24/7

---

**📅 Última actualización:** Septiembre 2025  
**🌐 Dominio:** apis.xafra-ads.com  
**📊 Total de Endpoints:** 18 APIs organizadas en 8 grupos  
**🏢 XafraTech © 2025**
