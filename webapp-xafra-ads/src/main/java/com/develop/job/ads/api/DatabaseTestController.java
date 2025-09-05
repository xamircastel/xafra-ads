package com.develop.job.ads.api;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.develop.job.jdbi.bi.ICampaignBI;
import com.develop.job.db.entity.Campaign;

/**
 * Controller para testing de conectividad de base de datos
 * @author XafraTech
 */
@RestController
@RequestMapping(value = "/v1/db")
public class DatabaseTestController {

    @Autowired(required = false)
    private DataSource dataSource;
    
    @Autowired(required = false)
    private ICampaignBI campaignBI;

    /**
     * Test básico de conexión a la base de datos
     */
    @GetMapping("/test-connection")
    public ResponseEntity<Map<String, Object>> testConnection() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (dataSource == null) {
                response.put("status", "ERROR");
                response.put("message", "DataSource no configurado");
                response.put("connected", false);
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
            }

            try (Connection conn = dataSource.getConnection()) {
                DatabaseMetaData metaData = conn.getMetaData();
                
                response.put("status", "SUCCESS");
                response.put("connected", true);
                response.put("databaseProduct", metaData.getDatabaseProductName());
                response.put("databaseVersion", metaData.getDatabaseProductVersion());
                response.put("driverName", metaData.getDriverName());
                response.put("driverVersion", metaData.getDriverVersion());
                response.put("catalog", conn.getCatalog());
                response.put("schema", conn.getSchema());
                response.put("readOnly", conn.isReadOnly());
                response.put("autoCommit", conn.getAutoCommit());
                response.put("message", "Conexion a base de datos exitosa");
                
                return ResponseEntity.ok(response);
            }
            
        } catch (SQLException e) {
            response.put("status", "ERROR");
            response.put("connected", false);
            response.put("message", "Error de conectividad SQL: " + e.getMessage());
            response.put("sqlState", e.getSQLState());
            response.put("errorCode", e.getErrorCode());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
        } catch (Exception e) {
            response.put("status", "ERROR");
            response.put("connected", false);
            response.put("message", "Error inesperado: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Test de acceso a datos básico
     */
    @GetMapping("/test-data-access")
    public ResponseEntity<Map<String, Object>> testDataAccess() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (dataSource == null) {
                response.put("status", "ERROR");
                response.put("message", "DataSource no configurado");
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
            }

            // Test de consulta básica
            try (Connection conn = dataSource.getConnection()) {
                java.sql.Statement stmt = conn.createStatement();
                java.sql.ResultSet rs = stmt.executeQuery("SELECT version(), current_database(), current_user, now()");
                
                if (rs.next()) {
                    response.put("status", "SUCCESS");
                    response.put("postgresqlVersion", rs.getString(1));
                    response.put("currentDatabase", rs.getString(2));
                    response.put("currentUser", rs.getString(3));
                    response.put("serverDateTime", rs.getTimestamp(4));
                    response.put("message", "Acceso a datos funcionando correctamente");
                } else {
                    response.put("status", "ERROR");
                    response.put("message", "No se pudieron obtener datos básicos");
                }
                
                rs.close();
                stmt.close();
                return ResponseEntity.ok(response);
                
            }
            
        } catch (SQLException e) {
            response.put("status", "ERROR");
            response.put("message", "Error SQL: " + e.getMessage());
            response.put("sqlState", e.getSQLState());
            response.put("errorCode", e.getErrorCode());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
        } catch (Exception e) {
            response.put("status", "ERROR");
            response.put("message", "Error inesperado: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Test específico de tabla campaigns
     */
    @GetMapping("/test-campaigns")
    public ResponseEntity<Map<String, Object>> testCampaigns() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (campaignBI == null) {
                response.put("status", "WARNING");
                response.put("message", "CampaignBI no disponible - probando acceso directo");
                return testCampaignsDirectAccess();
            }

            // Test usando JDBI - probamos con un productId de ejemplo
            List<Campaign> campaigns = campaignBI.getAllCampainByProductId(1L);
            
            response.put("status", "SUCCESS");
            response.put("campaignCount", campaigns != null ? campaigns.size() : 0);
            response.put("message", "Acceso a tabla campaign via JDBI exitoso");
            response.put("productIdTested", 1L);
            response.put("firstCampaigns", campaigns != null && !campaigns.isEmpty() ? 
                campaigns.subList(0, Math.min(3, campaigns.size())) : null);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("status", "ERROR");
            response.put("message", "Error accediendo a campaigns via JDBI: " + e.getMessage());
            
            // Fallback a acceso directo
            return testCampaignsDirectAccess();
        }
    }
    
    /**
     * Test directo de tabla campaigns sin JDBI
     */
    private ResponseEntity<Map<String, Object>> testCampaignsDirectAccess() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (dataSource == null) {
                response.put("status", "ERROR");
                response.put("message", "DataSource no configurado");
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
            }

            try (Connection conn = dataSource.getConnection()) {
                java.sql.Statement stmt = conn.createStatement();
                java.sql.ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as record_count FROM campaign");
                
                if (rs.next()) {
                    int count = rs.getInt("record_count");
                    response.put("status", "SUCCESS");
                    response.put("campaignCount", count);
                    response.put("message", "Acceso directo a tabla campaign exitoso");
                    response.put("method", "Direct SQL");
                } else {
                    response.put("status", "ERROR");
                    response.put("message", "No se pudo obtener conteo de campaigns");
                }
                
                rs.close();
                stmt.close();
                return ResponseEntity.ok(response);
                
            }
            
        } catch (SQLException e) {
            response.put("status", "ERROR");
            response.put("message", "Error SQL accediendo a campaign: " + e.getMessage());
            response.put("sqlState", e.getSQLState());
            response.put("errorCode", e.getErrorCode());
            
            if (e.getMessage().contains("does not exist")) {
                response.put("suggestion", "Verificar que la tabla 'campaign' exista en el esquema");
            }
            
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
        } catch (Exception e) {
            response.put("status", "ERROR");
            response.put("message", "Error inesperado: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Health check completo de la base de datos
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> checks = new HashMap<>();
        boolean overallHealth = true;
        
        // 1. Test de DataSource
        try {
            if (dataSource != null) {
                checks.put("dataSource", "Available");
            } else {
                checks.put("dataSource", "Not configured");
                overallHealth = false;
            }
        } catch (Exception e) {
            checks.put("dataSource", "Error: " + e.getMessage());
            overallHealth = false;
        }
        
        // 2. Test de conexión
        try {
            if (dataSource != null) {
                try (Connection conn = dataSource.getConnection()) {
                    checks.put("connection", "Connected successfully");
                }
            } else {
                checks.put("connection", "DataSource not available");
                overallHealth = false;
            }
        } catch (Exception e) {
            checks.put("connection", "Failed: " + e.getMessage());
            overallHealth = false;
        }
        
        // 3. Test de JDBI
        try {
            if (campaignBI != null) {
                checks.put("jdbi", "CampaignBI available");
            } else {
                checks.put("jdbi", "CampaignBI not injected");
                // No marca como fallo porque puede ser configuración opcional
            }
        } catch (Exception e) {
            checks.put("jdbi", "Error: " + e.getMessage());
        }
        
        response.put("status", overallHealth ? "HEALTHY" : "UNHEALTHY");
        response.put("timestamp", System.currentTimeMillis());
        response.put("checks", checks);
        response.put("message", overallHealth ? 
            "Todos los componentes de BD están funcionando" : 
            "Algunos componentes de BD tienen problemas");
        
        return ResponseEntity.status(overallHealth ? HttpStatus.OK : HttpStatus.SERVICE_UNAVAILABLE)
                            .body(response);
    }
}
