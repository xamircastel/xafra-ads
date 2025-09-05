package co.com.xafra.ads.webapp.rest;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.develop.job.jdbi.bi.ICampaignBI;
import com.develop.job.db.entity.Campaign;

/**
 * Controller para testing de conectividad de base de datos
 * @author XafraTech
 */
@RestController
public class DatabaseTestController {

    @Autowired(required = false)
    private DataSource dataSource;
    
    @Autowired(required = false)
    private ICampaignBI campaignBI;

    /**
     * Test básico de conexión a la base de datos
     */
    @GetMapping("/v1/db/test-connection")
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
                response.put("database_product", metaData.getDatabaseProductName());
                response.put("database_version", metaData.getDatabaseProductVersion());
                response.put("driver_name", metaData.getDriverName());
                response.put("driver_version", metaData.getDriverVersion());
                response.put("url", metaData.getURL());
                response.put("username", metaData.getUserName());
                response.put("catalog", conn.getCatalog());
                response.put("schema", conn.getSchema());
                response.put("readonly", conn.isReadOnly());
                response.put("autocommit", conn.getAutoCommit());
                
                return ResponseEntity.ok(response);
            }
            
        } catch (SQLException e) {
            response.put("status", "ERROR");
            response.put("connected", false);
            response.put("error_message", e.getMessage());
            response.put("error_code", e.getErrorCode());
            response.put("sql_state", e.getSQLState());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
        } catch (Exception e) {
            response.put("status", "ERROR");
            response.put("connected", false);
            response.put("error_message", e.getMessage());
            response.put("error_type", e.getClass().getSimpleName());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Test de acceso a datos usando JDBI
     */
    @GetMapping("/v1/db/test-data-access")
    public ResponseEntity<Map<String, Object>> testDataAccess() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (campaignBI == null) {
                response.put("status", "ERROR");
                response.put("message", "CampaignBI no está disponible");
                response.put("jdbi_configured", false);
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
            }

            // Intentar obtener campañas con un producto específico
            try {
                java.util.List<Campaign> campaigns = campaignBI.getAllCampainByProductId(1L);
                response.put("status", "SUCCESS");
                response.put("jdbi_configured", true);
                response.put("campaign_count", campaigns.size());
                response.put("data_access", "WORKING");
                response.put("test_query", "getAllCampainByProductId(1L)");
                
                return ResponseEntity.ok(response);
            } catch (Exception e) {
                response.put("status", "WARNING");
                response.put("jdbi_configured", true);
                response.put("data_access", "ERROR");
                response.put("error_message", e.getMessage());
                response.put("note", "JDBI configurado pero posible problema con tabla o datos");
                
                return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(response);
            }
            
        } catch (Exception e) {
            response.put("status", "ERROR");
            response.put("jdbi_configured", false);
            response.put("error_message", e.getMessage());
            response.put("error_type", e.getClass().getSimpleName());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Test de listado básico de campañas por producto
     */
    @GetMapping("/v1/db/test-campaigns")
    public ResponseEntity<Map<String, Object>> testCampaigns() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (campaignBI == null) {
                response.put("status", "ERROR");
                response.put("message", "CampaignBI no disponible");
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
            }

            // Intentar obtener campañas para producto 1
            java.util.List<Campaign> campaigns = campaignBI.getAllCampainByProductId(1L);
            
            response.put("status", "SUCCESS");
            response.put("total_campaigns", campaigns.size());
            response.put("campaigns_preview", campaigns.stream()
                .limit(5)
                .map(c -> {
                    Map<String, Object> campaignInfo = new HashMap<>();
                    campaignInfo.put("id", c.getId());
                    campaignInfo.put("productId", c.getProductId());
                    campaignInfo.put("traking", c.getTraking());
                    campaignInfo.put("status", c.getStatus());
                    campaignInfo.put("uuid", c.getUuid());
                    return campaignInfo;
                }).toArray());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("status", "ERROR");
            response.put("error_message", e.getMessage());
            response.put("error_type", e.getClass().getSimpleName());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Health check completo de base de datos
     */
    @GetMapping("/v1/db/health")
    public ResponseEntity<Map<String, Object>> databaseHealth() {
        Map<String, Object> response = new HashMap<>();
        
        // Test 1: Conexión básica
        Map<String, Object> connectionTest = new HashMap<>();
        try {
            if (dataSource != null) {
                try (Connection conn = dataSource.getConnection()) {
                    connectionTest.put("status", "OK");
                    connectionTest.put("connected", true);
                    connectionTest.put("database", conn.getCatalog());
                    connectionTest.put("url", conn.getMetaData().getURL());
                }
            } else {
                connectionTest.put("status", "NO_DATASOURCE");
                connectionTest.put("connected", false);
                connectionTest.put("error", "DataSource no configurado");
            }
        } catch (Exception e) {
            connectionTest.put("status", "FAILED");
            connectionTest.put("connected", false);
            connectionTest.put("error", e.getMessage());
        }
        
        // Test 2: JDBI
        Map<String, Object> jdbiTest = new HashMap<>();
        try {
            if (campaignBI != null) {
                java.util.List<Campaign> campaigns = campaignBI.getAllCampainByProductId(1L);
                jdbiTest.put("status", "OK");
                jdbiTest.put("available", true);
                jdbiTest.put("test_campaign_count", campaigns.size());
            } else {
                jdbiTest.put("status", "NOT_CONFIGURED");
                jdbiTest.put("available", false);
            }
        } catch (Exception e) {
            jdbiTest.put("status", "FAILED");
            jdbiTest.put("available", false);
            jdbiTest.put("error", e.getMessage());
        }
        
        response.put("connection", connectionTest);
        response.put("jdbi", jdbiTest);
        response.put("timestamp", System.currentTimeMillis());
        response.put("server_time", new java.util.Date());
        
        boolean allOk = "OK".equals(connectionTest.get("status")) && 
                       ("OK".equals(jdbiTest.get("status")) || "NOT_CONFIGURED".equals(jdbiTest.get("status")));
        
        if (allOk) {
            response.put("overall_status", "HEALTHY");
            return ResponseEntity.ok(response);
        } else {
            response.put("overall_status", "UNHEALTHY");
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
        }
    }
}
