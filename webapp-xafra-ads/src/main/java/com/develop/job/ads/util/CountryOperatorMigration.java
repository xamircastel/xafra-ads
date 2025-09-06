package com.develop.job.ads.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Utilidad para ejecutar la migración de país y operador
 */
public class CountryOperatorMigration {

    private static final String DB_URL = "jdbc:postgresql://34.28.245.62:5432/xafra-ads";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "XafraTech2025!";

    public static void main(String[] args) {
        
        System.out.println("=== INICIANDO MIGRACION PAIS Y OPERADOR ===");
        
        try {
            // Cargar driver de PostgreSQL
            Class.forName("org.postgresql.Driver");
            
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                
                System.out.println("OK Conectado a la base de datos");
                
                executeMigrationSteps(connection);
                
                System.out.println("OK MIGRACION COMPLETADA EXITOSAMENTE");
                
            } catch (Exception e) {
                System.err.println("ERROR durante la migracion: " + e.getMessage());
                e.printStackTrace();
            }
            
        } catch (Exception e) {
            System.err.println("ERROR conectando a la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void executeMigrationSteps(Connection connection) throws Exception {
        
        try (Statement stmt = connection.createStatement()) {
            
            System.out.println("\n--- PASO 1: Modificando tabla 'products' ---");
            
            // Agregar campos a tabla products
            try {
                stmt.execute("ALTER TABLE products ADD COLUMN IF NOT EXISTS country VARCHAR(10) DEFAULT NULL");
                System.out.println("OK Campo 'country' agregado a tabla 'products'");
            } catch (Exception e) {
                System.out.println("INFO Campo 'country' ya existe en tabla 'products': " + e.getMessage());
            }
            
            try {
                stmt.execute("ALTER TABLE products ADD COLUMN IF NOT EXISTS operator VARCHAR(50) DEFAULT NULL");
                System.out.println("OK Campo 'operator' agregado a tabla 'products'");
            } catch (Exception e) {
                System.out.println("INFO Campo 'operator' ya existe en tabla 'products': " + e.getMessage());
            }
            
            System.out.println("\n--- PASO 2: Modificando tabla 'customers' ---");
            
            // Agregar campos a tabla customers
            try {
                stmt.execute("ALTER TABLE customers ADD COLUMN IF NOT EXISTS country VARCHAR(10) DEFAULT NULL");
                System.out.println("OK Campo 'country' agregado a tabla 'customers'");
            } catch (Exception e) {
                System.out.println("INFO Campo 'country' ya existe en tabla 'customers': " + e.getMessage());
            }
            
            try {
                stmt.execute("ALTER TABLE customers ADD COLUMN IF NOT EXISTS operator VARCHAR(50) DEFAULT NULL");
                System.out.println("OK Campo 'operator' agregado a tabla 'customers'");
            } catch (Exception e) {
                System.out.println("INFO Campo 'operator' ya existe en tabla 'customers': " + e.getMessage());
            }
            
            System.out.println("\n--- PASO 3: Verificando estructura final ---");
            
            // Verificar campos en products
            System.out.println("Verificando estructura de tabla 'products':");
            ResultSet rs1 = stmt.executeQuery("SELECT column_name, data_type, is_nullable FROM information_schema.columns WHERE table_name = 'products' AND column_name IN ('country', 'operator') ORDER BY column_name");
            while (rs1.next()) {
                System.out.println("  - " + rs1.getString("column_name") + " (" + rs1.getString("data_type") + ")");
            }
            
            // Verificar campos en customers
            System.out.println("Verificando estructura de tabla 'customers':");
            ResultSet rs2 = stmt.executeQuery("SELECT column_name, data_type, is_nullable FROM information_schema.columns WHERE table_name = 'customers' AND column_name IN ('country', 'operator') ORDER BY column_name");
            while (rs2.next()) {
                System.out.println("  - " + rs2.getString("column_name") + " (" + rs2.getString("data_type") + ")");
            }
            
            // Verificar campos en campaign
            System.out.println("Verificando estructura de tabla 'campaign':");
            ResultSet rs3 = stmt.executeQuery("SELECT column_name, data_type, is_nullable FROM information_schema.columns WHERE table_name = 'campaign' AND column_name IN ('country', 'operator') ORDER BY column_name");
            while (rs3.next()) {
                System.out.println("  - " + rs3.getString("column_name") + " (" + rs3.getString("data_type") + ")");
            }
        }
    }
}
