package com.develop.job.ads.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Utilidad para estandarizar nombres de columnas en tabla campaign
 */
public class StandardizeCampaignColumns {

    private static final String DB_URL = "jdbc:postgresql://34.28.245.62:5432/xafra-ads";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "XafraTech2025!";

    public static void main(String[] args) {
        
        System.out.println("=== ESTANDARIZANDO COLUMNAS TABLA CAMPAIGN ===");
        
        try {
            Class.forName("org.postgresql.Driver");
            
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                
                System.out.println("OK Conectado a la base de datos");
                
                standardizeColumns(connection);
                
                System.out.println("OK ESTANDARIZACION COMPLETADA");
                
            } catch (Exception e) {
                System.err.println("ERROR durante la estandarizacion: " + e.getMessage());
                e.printStackTrace();
            }
            
        } catch (Exception e) {
            System.err.println("ERROR conectando a la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void standardizeColumns(Connection connection) throws Exception {
        
        try (Statement stmt = connection.createStatement()) {
            
            System.out.println("\n--- PASO 1: Renombrando tracking_corto a short_tracking ---");
            try {
                stmt.execute("ALTER TABLE campaign RENAME COLUMN tracking_corto TO short_tracking");
                System.out.println("OK Columna tracking_corto renombrada a short_tracking");
            } catch (Exception e) {
                System.out.println("INFO tracking_corto no existe o ya fue renombrada: " + e.getMessage());
            }
            
            System.out.println("\n--- PASO 2: Renombrando pais a country ---");
            try {
                stmt.execute("ALTER TABLE campaign RENAME COLUMN pais TO country");
                System.out.println("OK Columna pais renombrada a country");
            } catch (Exception e) {
                System.out.println("INFO pais no existe o ya fue renombrada: " + e.getMessage());
            }
            
            System.out.println("\n--- PASO 3: Renombrando operador a operator ---");
            try {
                stmt.execute("ALTER TABLE campaign RENAME COLUMN operador TO operator");
                System.out.println("OK Columna operador renombrada a operator");
            } catch (Exception e) {
                System.out.println("INFO operador no existe o ya fue renombrada: " + e.getMessage());
            }
            
            System.out.println("\n--- VERIFICACION FINAL ---");
            ResultSet rs = stmt.executeQuery(
                "SELECT column_name, data_type FROM information_schema.columns " +
                "WHERE table_name = 'campaign' AND column_name IN ('short_tracking', 'country', 'operator') " +
                "ORDER BY column_name"
            );
            
            System.out.println("Columnas estandarizadas:");
            while (rs.next()) {
                System.out.println("  - " + rs.getString("column_name") + " (" + rs.getString("data_type") + ")");
            }
        }
    }
}
