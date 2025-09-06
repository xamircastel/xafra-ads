package com.develop.job.ads.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Utilidad para verificar estructura de la tabla campaign
 */
public class CampaignTableChecker {

    private static final String DB_URL = "jdbc:postgresql://34.28.245.62:5432/xafra-ads";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "XafraTech2025!";

    public static void main(String[] args) {
        
        System.out.println("=== VERIFICANDO ESTRUCTURA TABLA CAMPAIGN ===");
        
        try {
            Class.forName("org.postgresql.Driver");
            
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                
                System.out.println("OK Conectado a la base de datos");
                
                checkCampaignStructure(connection);
                
            } catch (Exception e) {
                System.err.println("ERROR durante la verificacion: " + e.getMessage());
                e.printStackTrace();
            }
            
        } catch (Exception e) {
            System.err.println("ERROR conectando a la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void checkCampaignStructure(Connection connection) throws Exception {
        
        try (Statement stmt = connection.createStatement()) {
            
            System.out.println("\n--- ESTRUCTURA ACTUAL DE TABLA 'campaign' ---");
            
            // Verificar todas las columnas de campaign
            ResultSet rs = stmt.executeQuery(
                "SELECT column_name, data_type, is_nullable, column_default " +
                "FROM information_schema.columns " +
                "WHERE table_name = 'campaign' " +
                "ORDER BY ordinal_position"
            );
            
            while (rs.next()) {
                String colName = rs.getString("column_name");
                String dataType = rs.getString("data_type");
                String nullable = rs.getString("is_nullable");
                String defaultVal = rs.getString("column_default");
                
                System.out.println(String.format("  %-20s | %-15s | %-8s | %s", 
                    colName, dataType, nullable, defaultVal != null ? defaultVal : "NULL"));
            }
            
            System.out.println("\n--- VERIFICANDO CAMPOS ESPECÍFICOS ---");
            
            // Verificar si existe tracking_corto
            ResultSet trackingCorto = stmt.executeQuery(
                "SELECT column_name FROM information_schema.columns " +
                "WHERE table_name = 'campaign' AND column_name = 'tracking_corto'"
            );
            
            if (trackingCorto.next()) {
                System.out.println("INFO: Campo 'tracking_corto' EXISTE en la tabla");
            } else {
                System.out.println("INFO: Campo 'tracking_corto' NO EXISTE en la tabla");
            }
            
            // Verificar campos country y operator
            ResultSet countryOp = stmt.executeQuery(
                "SELECT column_name FROM information_schema.columns " +
                "WHERE table_name = 'campaign' AND column_name IN ('country', 'operator')"
            );
            
            System.out.println("Campos country/operator:");
            while (countryOp.next()) {
                System.out.println("  - " + countryOp.getString("column_name"));
            }
        }
    }
}
