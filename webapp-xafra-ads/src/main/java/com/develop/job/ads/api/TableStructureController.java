package com.develop.job.ads.api;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador para verificar la estructura de tablas existentes
 * y determinar si ya tienen campos de país y operador
 */
@RestController
@RequestMapping("/v1/db")
public class TableStructureController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/analyze-structure")
    public ResponseEntity<Map<String, Object>> analyzeTableStructure() {
        Map<String, Object> result = new HashMap<>();
        
        try (Connection connection = dataSource.getConnection()) {
            result.put("success", true);
            result.put("analysis", analyzeAllTables(connection));
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        return ResponseEntity.ok(result);
    }

    private Map<String, Object> analyzeAllTables(Connection connection) throws Exception {
        Map<String, Object> analysis = new HashMap<>();
        
        // Analizar tablas principales
        String[] tables = {"campaign", "products", "customers"};
        
        for (String tableName : tables) {
            analysis.put(tableName, analyzeTable(connection, tableName));
        }
        
        return analysis;
    }

    private Map<String, Object> analyzeTable(Connection connection, String tableName) throws Exception {
        Map<String, Object> tableInfo = new HashMap<>();
        List<Map<String, String>> columns = new ArrayList<>();
        
        boolean hasCountry = false;
        boolean hasOperator = false;
        
        // Obtener metadatos de la tabla
        DatabaseMetaData metadata = connection.getMetaData();
        ResultSet rs = metadata.getColumns(null, null, tableName, null);
        
        while (rs.next()) {
            Map<String, String> column = new HashMap<>();
            String columnName = rs.getString("COLUMN_NAME");
            
            column.put("name", columnName);
            column.put("type", rs.getString("TYPE_NAME"));
            column.put("size", String.valueOf(rs.getInt("COLUMN_SIZE")));
            column.put("nullable", rs.getString("IS_NULLABLE"));
            
            columns.add(column);
            
            // Detectar campos relacionados con país/operador
            String lowerName = columnName.toLowerCase();
            if (lowerName.contains("country") || lowerName.contains("pais")) {
                hasCountry = true;
            }
            if (lowerName.contains("operator") || lowerName.contains("operador")) {
                hasOperator = true;
            }
        }
        rs.close();
        
        tableInfo.put("columns", columns);
        tableInfo.put("hasCountryField", hasCountry);
        tableInfo.put("hasOperatorField", hasOperator);
        tableInfo.put("columnCount", columns.size());
        
        // Obtener conteo de registros
        try (Statement stmt = connection.createStatement()) {
            ResultSet countRs = stmt.executeQuery("SELECT COUNT(*) FROM " + tableName);
            if (countRs.next()) {
                tableInfo.put("recordCount", countRs.getInt(1));
            }
            countRs.close();
        } catch (Exception e) {
            tableInfo.put("recordCount", "Error: " + e.getMessage());
        }
        
        return tableInfo;
    }

    @GetMapping("/check-campaign-fields")
    public ResponseEntity<Map<String, Object>> checkCampaignFields() {
        Map<String, Object> result = new HashMap<>();
        
        try (Connection connection = dataSource.getConnection()) {
            
            // Verificar específicamente la tabla campaign
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM campaign LIMIT 1");
            
            List<String> columns = new ArrayList<>();
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                columns.add(rs.getMetaData().getColumnName(i));
            }
            
            result.put("success", true);
            result.put("campaignColumns", columns);
            
            // Buscar específicamente campos de país y operador
            boolean foundCountry = columns.stream().anyMatch(col -> 
                col.toLowerCase().contains("country") || col.toLowerCase().contains("pais"));
            boolean foundOperator = columns.stream().anyMatch(col -> 
                col.toLowerCase().contains("operator") || col.toLowerCase().contains("operador"));
                
            result.put("hasCountryField", foundCountry);
            result.put("hasOperatorField", foundOperator);
            result.put("needsCountryField", !foundCountry);
            result.put("needsOperatorField", !foundOperator);
            
            rs.close();
            stmt.close();
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        return ResponseEntity.ok(result);
    }
}
