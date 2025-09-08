# 🔐 XAFRA-ADS - Guía de Encriptación

## 📋 API de Encriptación Corregida

### ❌ **Forma INCORRECTA** (en Postman Collection original)
```bash
curl --location 'https://apis.xafra-ads.com/util/encryption' \
--header 'Content-Type: application/json' \
--data '{
    "text": "datos_a_encriptar",
    "key": "encryption_key"
}'
```

### ✅ **Forma CORRECTA** (como la usabas antes)
```bash
curl --location 'https://apis.xafra-ads.com/util/encryption' \
--header 'user: xafra2-ads-encrytion3' \
--header 'Content-Type: text/plain' \
--data '14'
```

## 🎯 **Cómo Usar la API Correctamente**

### 📝 **Parámetros Requeridos**

#### 1. **Body (Texto Plano)**
- **Contenido**: El `customer_id` que quieres encriptar
- **Formato**: Texto plano (no JSON)
- **Ejemplo**: `14` (sin comillas en el body)

#### 2. **Headers Obligatorios**
- **user**: `xafra2-ads-encrytion3` (credencial de autenticación)
- **Content-Type**: `text/plain` (muy importante!)

#### 3. **URL**
- **Producción**: `https://apis.xafra-ads.com/util/encryption`
- **Método**: `POST`

### 🔧 **Configuración en Postman**

#### Paso 1: Request Setup
```
Method: POST
URL: https://apis.xafra-ads.com/util/encryption
```

#### Paso 2: Headers
```
user: xafra2-ads-encrytion3
Content-Type: text/plain
```

#### Paso 3: Body
```
Seleccionar: raw → Text
Contenido: 14
```

### 📊 **Ejemplo Completo**

#### Request:
```bash
POST https://apis.xafra-ads.com/util/encryption
Headers:
  user: xafra2-ads-encrytion3
  Content-Type: text/plain
Body:
  14
```

#### Response Esperada:
```
ENCRYPTED_STRING_HERE (ej: AeYqQ4rNm3kP9)
```

### 🎯 **Casos de Uso**

#### Customer ID 14:
```bash
curl --location 'https://apis.xafra-ads.com/util/encryption' \
--header 'user: xafra2-ads-encrytion3' \
--header 'Content-Type: text/plain' \
--data '14'
```

#### Customer ID 25:
```bash
curl --location 'https://apis.xafra-ads.com/util/encryption' \
--header 'user: xafra2-ads-encrytion3' \
--header 'Content-Type: text/plain' \
--data '25'
```

#### Customer ID 100:
```bash
curl --location 'https://apis.xafra-ads.com/util/encryption' \
--header 'user: xafra2-ads-encrytion3' \
--header 'Content-Type: text/plain' \
--data '100'
```

## 🔐 **Cómo Funciona Internamente**

### 🔧 **Configuración del Sistema**
```properties
# En application-prod.properties
password.encription=%a7ra-.passd0474
user.encription=xafra2-ads-encrytion3
```

### 📝 **Implementación Java**
```java
@PostMapping("/encryption")
public ResponseEntity<?> encryp(
    @RequestHeader("user") String user, 
    @RequestBody String encryp) {
    
    if(userEncryp.equals(user)) { // xafra2-ads-encrytion3
        String encription = Encryption.encrypt(encryp, passEncription);
        encription = encription.replace("/", "$").replaceAll("=", "");
        return ResponseEntity.ok(encription);
    }
    return ResponseEntity.badRequest().build();
}
```

### 🔄 **Proceso de Encriptación**
1. **Input**: customer_id (ej: "14")
2. **Validación**: user header = "xafra2-ads-encrytion3"
3. **Encriptación**: AES con password "%a7ra-.passd0474"
4. **Limpieza**: Reemplaza "/" por "$" y elimina "="
5. **Output**: String encriptado listo para URLs

## ⚠️ **Errores Comunes**

### ❌ **Error 400 - Bad Request**
**Causa**: Header 'user' incorrecto o faltante
**Solución**: Usar exactamente `user: xafra2-ads-encrytion3`

### ❌ **Error 500 - Internal Server Error**
**Causa**: Error en el proceso de encriptación
**Solución**: Verificar que el body contenga solo el customer_id

### ❌ **Respuesta Vacía**
**Causa**: Content-Type incorrecto
**Solución**: Usar `Content-Type: text/plain`

## 🧪 **Testing en Postman**

### Collection Actualizada
La collection ha sido corregida con:
- ✅ Headers correctos
- ✅ Content-Type: text/plain
- ✅ Body como texto plano
- ✅ Ejemplo con customer_id "14"

### Variables Recomendadas
```json
{
  "encryption_user": "xafra2-ads-encrytion3",
  "customer_id_test": "14",
  "base_url": "https://apis.xafra-ads.com"
}
```

### Script de Testing
```javascript
// Pre-request script
pm.globals.set("test_customer_id", Math.floor(Math.random() * 1000));

// Test script
pm.test("Encryption successful", function () {
    pm.expect(pm.response.code).to.equal(200);
    pm.expect(pm.response.text()).to.not.be.empty;
});

pm.test("Response is encrypted string", function () {
    var responseText = pm.response.text();
    pm.expect(responseText).to.match(/^[A-Za-z0-9$]+$/);
});
```

## 🌐 **URLs de Testing**

### Producción
```
https://apis.xafra-ads.com/util/encryption
```

### Backup (Cloud Run Directo)
```
https://xafra-ads-697203931362.us-central1.run.app/util/encryption
```

## 📋 **Checklist de Verificación**

Antes de usar la API, verificar:
- ✅ URL correcta: `/util/encryption`
- ✅ Método: POST
- ✅ Header 'user': xafra2-ads-encrytion3
- ✅ Content-Type: text/plain
- ✅ Body: solo el customer_id (sin JSON)
- ✅ Respuesta: string encriptado

---

**🔐 Encriptación configurada correctamente**  
**📅 Septiembre 2025**  
**🏢 XafraTech © 2025**
