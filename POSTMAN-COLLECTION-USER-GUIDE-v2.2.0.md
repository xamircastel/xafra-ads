# 🚀 Guía de Uso: XAFRA-ADS Postman Collection v2.2.0

## 📋 **Introducción**

Esta guía te ayudará a usar la nueva **Collection v2.2.0** de XAFRA-ADS que incluye la funcionalidad de **tracking automático**. La collection está optimizada para testing, validación y documentación de todas las modalidades de tracking disponibles.

---

## 📥 **Instalación y Configuración**

### **1. Importar la Collection**
```bash
Archivo: XAFRA-ADS-APIs-Collection-v2.2.0.postman_collection.json
```

**Pasos:**
1. Abrir **Postman**
2. Click en **Import**
3. Seleccionar el archivo `XAFRA-ADS-APIs-Collection-v2.2.0.postman_collection.json`
4. Confirmar importación

### **2. Configurar Variables**
La collection incluye variables pre-configuradas:

```json
{
  "base_url": "https://apis.xafra-ads.com",
  "base_url_cloudrun": "https://xafra-ads-697203931362.us-central1.run.app",
  "api_key": "your_api_key_here",
  "tracking_id": "TRACK_{{$timestamp}}",
  "auto_tracking_format": "XAFRA_YYYYMMDD_HHMMSS_NNNNN",
  "product_110_param": "zhv130RYZdznrnewOUF$lA",
  "environment": "production",
  "version": "v2.2.0-auto-tracking"
}
```

**🔧 Configuración requerida:**
- Actualizar `api_key` con tu API key real (si usas endpoints autenticados)
- Verificar que `base_url` apunte al ambiente correcto

---

## 🧪 **Guía de Testing**

### **🆕 Sección 1: Tracking Automático (v2.2.0)**

#### **Test 1: Auto-Tracking Product ID 110 (Digital-X CR)**
```
GET {{base_url}}/ads/tr/{{product_110_param}}/
```

**✅ Validaciones automáticas:**
- Status code 302 (redirección)
- Header Location presente
- Formato tracking: `XAFRA_YYYYMMDD_HHMMSS_NNNNN`
- Redirección a Digital-X Costa Rica
- Operador Kolbi confirmado

**📊 Resultados esperados:**
```
🤖 Tracking auto-generado: XAFRA_20250908_223015_42851
✅ Redirección exitosa a: https://lp.digital-x.com.co/costarica/kolbi/campaign1?tracking=XAFRA_20250908_223015_42851
```

#### **Test 2: Auto-Tracking Product ID 1 (Movistar)**
```
GET {{base_url}}/ads/tr/M85DjiEk4XszqaUk94vETA/
```

**🎯 Propósito:** Demostrar versatilidad del mismo producto

**📋 Comparación automática:**
- Tracking Externo: `/ads/M85DjiEk4XszqaUk94vETA/?clickId=EXTERNAL`
- Tracking Automático: `/ads/tr/M85DjiEk4XszqaUk94vETA/`

#### **Test 3: Validación Comparativa**
**🧪 Endpoint de validación:** `GET {{base_url}}/v1/ping`

**📊 Tests ejecutados:**
- Verificación de generación de tracking automático
- Validación de formato para ambos productos
- Comparación de modalidades
- Confirmación de funcionamiento simultáneo

### **📱 Sección 2: Tracking Externo (Tradicional)**

#### **Tests de Compatibilidad:**
Ejecuta estos tests para confirmar que la funcionalidad existente sigue funcionando:

1. **Product ID 1:** `GET {{base_url}}/ads/M85DjiEk4XszqaUk94vETA/?clickId={{tracking_id}}`
2. **Product ID 2:** `GET {{base_url}}/ads/hE6wiDK+BUKM2cSJgSshnQ/?clickId={{tracking_id}}`
3. **Product ID 3:** `GET {{base_url}}/ads/T0jgZrjniEHPS7u1ZB9UkA/?clickId={{tracking_id}}`

### **📊 Sección 3: Documentación y Comparación**

#### **Endpoint de Documentación:**
```
GET {{base_url}}/v1/docs/tracking-comparison
```

**📚 Contenido incluido:**
- Tabla comparativa de modalidades
- Casos de uso recomendados
- Flujos de procesamiento
- Consideraciones técnicas

---

## 🎯 **Casos de Uso Recomendados**

### **🤖 Usar Tracking Automático cuando:**
```
✅ Partner no tiene sistema de tracking propio
✅ Se busca simplicidad en la implementación
✅ XAFRA-ADS debe controlar completamente el tracking
✅ Se requiere formato estandarizado para reporting
✅ URLs simples sin parámetros complejos
```

**Ejemplo de implementación:**
```javascript
// Simplemente acceder a la URL sin parámetros
fetch('https://apis.xafra-ads.com/ads/tr/zhv130RYZdznrnewOUF$lA/')
  .then(response => {
    // Sistema redirige automáticamente con tracking generado
    console.log('Tracking generado automáticamente por XAFRA-ADS');
  });
```

### **🔗 Usar Tracking Externo cuando:**
```
✅ Partner tiene sistema de tracking robusto
✅ Se requiere integración con analytics propios
✅ Necesidad de formatos específicos de tracking
✅ Control total sobre el ciclo de vida del tracking
```

**Ejemplo de implementación:**
```javascript
// Generar tracking propio y enviarlo como parámetro
const myTracking = 'PARTNER_' + Date.now();
fetch(`https://apis.xafra-ads.com/ads/M85DjiEk4XszqaUk94vETA/?clickId=${myTracking}`)
  .then(response => {
    // Sistema usa el tracking proporcionado
    console.log('Tracking controlado por partner:', myTracking);
  });
```

---

## 📊 **Interpretación de Resultados**

### **Tracking Automático Exitoso:**
```json
Status: 302 Found
Location: https://lp.digital-x.com.co/costarica/kolbi/campaign1?tracking=XAFRA_20250908_223015_42851

Formato tracking: XAFRA_YYYYMMDD_HHMMSS_NNNNN
✅ XAFRA: Prefijo identificador
✅ 20250908: Fecha (8 de septiembre de 2025)
✅ 223015: Hora (22:30:15)
✅ 42851: Secuencia única
```

### **Variables Globales Generadas:**
```javascript
pm.globals.get('auto_generated_tracking_110')    // Para Product 110
pm.globals.get('auto_generated_tracking_movistar') // Para Product 1
pm.globals.get('current_tracking')                 // Para tracking externo
```

---

## 🔧 **Troubleshooting**

### **❌ Error 404 en Tracking Automático**
**Problema:** `GET /ads/tr/{param}/` retorna 404

**Soluciones:**
1. **Verificar deployment:** Confirmar que el nuevo código está desplegado
2. **Verificar URL:** Usar exactamente `/ads/tr/{param}/` (con `/tr/`)
3. **Testing local:** Probar primero en ambiente de desarrollo
4. **Logs del servidor:** Revisar logs de Cloud Run para errores de compilación

### **❌ Formato de Tracking Incorrecto**
**Problema:** Tracking generado no sigue formato `XAFRA_YYYYMMDD_HHMMSS_NNNNN`

**Verificaciones:**
```javascript
// Test incluido en la collection
let formatRegex = /^XAFRA_\d{8}_\d{6}_\d{5}$/;
if (!formatRegex.test(tracking)) {
  console.log('❌ Formato incorrecto:', tracking);
}
```

### **❌ Redirección a Destino Incorrecto**
**Problema:** No redirige al partner correcto

**Validaciones:**
1. **Parámetro encriptado:** Verificar que el parámetro corresponde al producto correcto
2. **Configuración BD:** Confirmar que Product ID está configurado correctamente
3. **URL destino:** Verificar que la URL del partner es correcta

---

## 📈 **Métricas y Monitoring**

### **Tests Automáticos Incluidos:**
La collection ejecuta automáticamente estos tests:

```javascript
✅ Status code is 302 (Redirect)
✅ Response has Location header  
✅ Response time is acceptable
✅ Tracking format is correct
✅ Redirects to correct domain
✅ Country is correct
✅ Operator is correct
```

### **Métricas Medidas:**
- **Tiempo de respuesta:** Debe ser < 3000ms
- **Unicidad:** Cada tracking debe ser único
- **Formato:** Debe seguir XAFRA_YYYYMMDD_HHMMSS_NNNNN
- **Redirección:** Debe apuntar al partner correcto

---

## 🎯 **Flujo de Testing Recomendado**

### **1. Testing Inicial:**
```
1. Ejecutar Health Checks → Verificar que la API está funcionando
2. Probar Tracking Externo → Confirmar compatibilidad backward
3. Probar Tracking Automático → Validar nueva funcionalidad
4. Ejecutar Tests Comparativos → Verificar coexistencia
```

### **2. Testing de Integración:**
```
1. Generar tracking automático → Obtener tracking ID
2. Usar tracking para confirmación → POST /ads/confirm/{tracking}
3. Verificar en BD → Confirmar que campaign se creó correctamente
4. Validar postbacks → Verificar notificaciones a traffic sources
```

### **3. Testing de Performance:**
```
1. Ejecutar múltiples requests → Verificar concurrencia
2. Medir tiempos de respuesta → Confirmar performance
3. Validar unicidad → Asegurar que no hay duplicados
4. Monitorear recursos → Verificar uso de CPU/memoria
```

---

## 📚 **Referencias y Documentación**

### **Archivos Relacionados:**
- `AUTO-TRACKING-IMPLEMENTATION.md` - Documentación técnica completa
- `POSTMAN-COLLECTION-CHANGELOG-v2.2.0.md` - Changelog detallado
- `TRACKING-AUTOMATION-SUMMARY.md` - Resumen del proyecto

### **Endpoints de Documentación:**
- `GET /v1/docs/tracking-comparison` - Comparación de modalidades
- `GET /actuator/health` - Estado de la aplicación
- `GET /v1/ping` - Health check simple

### **Variables de Environment:**
```
{{base_url}} - URL base de la API
{{product_110_param}} - Parámetro para Digital-X Costa Rica
{{tracking_id}} - Tracking ID generado automáticamente
{{auto_tracking_format}} - Formato de tracking automático
```

---

## 🚀 **Siguiente Nivel**

### **Automatización Avanzada:**
```javascript
// Script para testing automático de múltiples productos
const productos = [
  'M85DjiEk4XszqaUk94vETA',  // Product 1
  'hE6wiDK+BUKM2cSJgSshnQ',  // Product 2 
  'zhv130RYZdznrnewOUF$lA'   // Product 110
];

productos.forEach(param => {
  // Test tracking automático
  pm.sendRequest(`{{base_url}}/ads/tr/${param}/`, (err, res) => {
    console.log(`Auto-tracking para ${param}:`, res.headers.get('location'));
  });
});
```

### **Integración con CI/CD:**
```bash
# Ejecutar collection desde Newman (CLI)
newman run XAFRA-ADS-APIs-Collection-v2.2.0.postman_collection.json \
  --environment production.postman_environment.json \
  --reporters cli,junit \
  --reporter-junit-export results.xml
```

---

## 🎯 **Conclusión**

La **Collection v2.2.0** está diseñada para facilitar el testing y validación de la nueva funcionalidad de **tracking automático** mientras mantiene compatibilidad completa con todas las funcionalidades existentes.

**Beneficios clave para el testing:**
- ✅ **Tests automáticos** completos y detallados
- ✅ **Validación de formato** en tiempo real
- ✅ **Comparación de modalidades** automática
- ✅ **Documentación integrada** en cada endpoint
- ✅ **Troubleshooting** simplificado con logs detallados

**🚀 ¡Estás listo para probar la nueva funcionalidad de tracking automático!**

---

**📅 Última actualización:** Septiembre 8, 2025  
**🏷️ Versión:** v2.2.0-auto-tracking  
**👤 Autor:** XAFRA-ADS Development Team
