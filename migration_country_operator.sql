-- =====================================================================
-- SCRIPT DE MIGRACIÓN: Agregar campos PAÍS y OPERADOR
-- Proyecto: Xafra-Ads
-- Fecha: 2025-09-05
-- Descripción: Agrega campos country y operator a las tablas que no los tienen
-- =====================================================================

-- =========================================
-- 1. VERIFICACIÓN DE TABLAS EXISTENTES
-- =========================================

-- Verificar estructura actual de campaign (debería tener los campos)
SELECT column_name, data_type, is_nullable 
FROM information_schema.columns 
WHERE table_name = 'campaign' AND column_name IN ('country', 'operator')
ORDER BY column_name;

-- Verificar estructura actual de products
SELECT column_name, data_type, is_nullable 
FROM information_schema.columns 
WHERE table_name = 'products' AND column_name IN ('country', 'operator')
ORDER BY column_name;

-- Verificar estructura actual de customers  
SELECT column_name, data_type, is_nullable 
FROM information_schema.columns 
WHERE table_name = 'customers' AND column_name IN ('country', 'operator')
ORDER BY column_name;

-- =========================================
-- 2. AGREGAR CAMPOS A TABLA PRODUCTS
-- =========================================

-- Agregar campo country a products (nullable para compatibilidad)
ALTER TABLE products 
ADD COLUMN IF NOT EXISTS country VARCHAR(10) DEFAULT NULL 
COMMENT 'País del producto (PE, CO, MX, etc.)';

-- Agregar campo operator a products (nullable para compatibilidad)
ALTER TABLE products 
ADD COLUMN IF NOT EXISTS operator VARCHAR(50) DEFAULT NULL 
COMMENT 'Operador móvil (ENTEL, MOVISTAR, CLARO, etc.)';

-- =========================================
-- 3. AGREGAR CAMPOS A TABLA CUSTOMERS
-- =========================================

-- Agregar campo country a customers (nullable para compatibilidad)
ALTER TABLE customers 
ADD COLUMN IF NOT EXISTS country VARCHAR(10) DEFAULT NULL 
COMMENT 'País principal del customer (PE, CO, MX, etc.)';

-- Agregar campo operator a customers (nullable para compatibilidad)
ALTER TABLE customers 
ADD COLUMN IF NOT EXISTS operator VARCHAR(50) DEFAULT NULL 
COMMENT 'Operador móvil principal del customer';

-- =========================================
-- 4. VERIFICACIÓN POST-MIGRACIÓN
-- =========================================

-- Verificar que los campos se agregaron correctamente
SELECT 
    table_name,
    column_name,
    data_type,
    character_maximum_length,
    is_nullable,
    column_default
FROM information_schema.columns 
WHERE table_name IN ('campaign', 'products', 'customers') 
  AND column_name IN ('country', 'operator')
ORDER BY table_name, column_name;

-- =========================================
-- 5. CREAR ÍNDICES PARA PERFORMANCE
-- =========================================

-- Índices en products para consultas por país/operador
CREATE INDEX IF NOT EXISTS idx_products_country ON products(country) WHERE country IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_products_operator ON products(operator) WHERE operator IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_products_country_operator ON products(country, operator) WHERE country IS NOT NULL AND operator IS NOT NULL;

-- Índices en customers para consultas por país/operador
CREATE INDEX IF NOT EXISTS idx_customers_country ON customers(country) WHERE country IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_customers_operator ON customers(operator) WHERE operator IS NOT NULL;

-- Índices en campaign (si no existen ya)
CREATE INDEX IF NOT EXISTS idx_campaign_country ON campaign(country) WHERE country IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_campaign_operator ON campaign(operator) WHERE operator IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_campaign_country_operator ON campaign(country, operator) WHERE country IS NOT NULL AND operator IS NOT NULL;

-- =========================================
-- 6. ESTADÍSTICAS DE MIGRACIÓN
-- =========================================

-- Contar registros por tabla
SELECT 'products' as tabla, COUNT(*) as total_registros FROM products
UNION ALL
SELECT 'customers' as tabla, COUNT(*) as total_registros FROM customers  
UNION ALL
SELECT 'campaign' as tabla, COUNT(*) as total_registros FROM campaign;

-- Contar registros con campos NULL (esperado inicialmente)
SELECT 'products_country_null' as campo, COUNT(*) as registros_null 
FROM products WHERE country IS NULL
UNION ALL
SELECT 'products_operator_null' as campo, COUNT(*) as registros_null 
FROM products WHERE operator IS NULL
UNION ALL
SELECT 'customers_country_null' as campo, COUNT(*) as registros_null 
FROM customers WHERE country IS NULL
UNION ALL  
SELECT 'customers_operator_null' as campo, COUNT(*) as registros_null 
FROM customers WHERE operator IS NULL;

-- =========================================
-- 7. SCRIPT DE ROLLBACK (SI ES NECESARIO)
-- =========================================

/*
-- SOLO EJECUTAR EN CASO DE NECESITAR ROLLBACK
-- IMPORTANTE: Este script eliminará los campos agregados

-- Eliminar campos de products
ALTER TABLE products DROP COLUMN IF EXISTS country;
ALTER TABLE products DROP COLUMN IF EXISTS operator;

-- Eliminar campos de customers  
ALTER TABLE customers DROP COLUMN IF EXISTS country;
ALTER TABLE customers DROP COLUMN IF EXISTS operator;

-- Eliminar índices
DROP INDEX IF EXISTS idx_products_country;
DROP INDEX IF EXISTS idx_products_operator;
DROP INDEX IF EXISTS idx_products_country_operator;
DROP INDEX IF EXISTS idx_customers_country;
DROP INDEX IF EXISTS idx_customers_operator;

*/

-- =========================================
-- FIN DEL SCRIPT DE MIGRACIÓN
-- =========================================

COMMENT ON COLUMN products.country IS 'País del producto para segmentación por mercado';
COMMENT ON COLUMN products.operator IS 'Operador móvil asociado para reglas específicas';
COMMENT ON COLUMN customers.country IS 'País principal del customer para configuración regional';
COMMENT ON COLUMN customers.operator IS 'Operador móvil principal para partnerships';
