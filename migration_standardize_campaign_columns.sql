-- Migración para estandarizar nombres de columnas en tabla campaign
-- Renombrar columnas en español a inglés para mantener consistencia

-- Paso 1: Renombrar 'tracking_corto' a 'short_tracking'
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'campaign' AND column_name = 'tracking_corto') THEN
        ALTER TABLE campaign RENAME COLUMN tracking_corto TO short_tracking;
        RAISE NOTICE 'Columna tracking_corto renombrada a short_tracking';
    ELSE
        RAISE NOTICE 'Columna tracking_corto no existe';
    END IF;
END $$;

-- Paso 2: Renombrar 'pais' a 'country'
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'campaign' AND column_name = 'pais') THEN
        ALTER TABLE campaign RENAME COLUMN pais TO country;
        RAISE NOTICE 'Columna pais renombrada a country';
    ELSE
        RAISE NOTICE 'Columna pais no existe';
    END IF;
END $$;

-- Paso 3: Renombrar 'operador' a 'operator'
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'campaign' AND column_name = 'operador') THEN
        ALTER TABLE campaign RENAME COLUMN operador TO operator;
        RAISE NOTICE 'Columna operador renombrada a operator';
    ELSE
        RAISE NOTICE 'Columna operador no existe';
    END IF;
END $$;

-- Verificación final
SELECT 
    'campaign' as tabla,
    column_name as columna,
    data_type as tipo,
    is_nullable as nullable
FROM information_schema.columns 
WHERE table_name = 'campaign' 
AND column_name IN ('short_tracking', 'country', 'operator')
ORDER BY column_name;
