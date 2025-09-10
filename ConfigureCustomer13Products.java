import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Script para configurar productos del customer_id 13 con random=1
 * para pruebas del sistema de redirección inteligente
 */
public class ConfigureCustomer13Products {
    
    private static final String DB_URL = "jdbc:postgresql://34.28.245.62:5432/xafra-ads";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "XafraTech2025!";
    private static final Long CUSTOMER_ID = 13L;
    
    public static void main(String[] args) {
        Connection conn = null;
        
        try {
            // Cargar driver PostgreSQL
            Class.forName("org.postgresql.Driver");
            
            // Conectar a la base de datos
            System.out.println("CONECTANDO: Conectando a la base de datos...");
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("OK: Conexion exitosa!");
            
            // 1. Mostrar productos actuales del customer 13
            System.out.println("\nLISTA: PRODUCTOS ACTUALES DEL CUSTOMER 13:");
            showCustomerProducts(conn, CUSTOMER_ID);
            
            // 2. Configurar primeros 3 productos activos con random=1
            System.out.println("\nCONFIG: CONFIGURANDO PRODUCTOS PARA REDIRECCION INTELIGENTE...");
            List<Long> productIds = configureRandomProducts(conn, CUSTOMER_ID, 3);
            
            // 3. Mostrar resultado final
            System.out.println("\nRESULTADO: PRODUCTOS CONFIGURADOS:");
            showRandomProducts(conn, CUSTOMER_ID);
            
            // 4. Mostrar resumen
            System.out.println("\nRESUMEN:");
            System.out.println("Customer ID: " + CUSTOMER_ID);
            System.out.println("Productos configurados: " + productIds.size());
            System.out.println("Productos IDs: " + productIds);
            
            System.out.println("\nPROXIMO PASO:");
            System.out.println("Encriptar customer_id " + CUSTOMER_ID + " para generar URL de prueba");
            
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) conn.close();
                System.out.println("\nCONEXION: Conexion cerrada.");
            } catch (SQLException e) {
                System.err.println("Error cerrando conexion: " + e.getMessage());
            }
        }
    }
    
    /**
     * Mostrar todos los productos del customer
     */
    private static void showCustomerProducts(Connection conn, Long customerId) throws SQLException {
        String sql = "SELECT id_product, name, active, random, url_redirect_success FROM products WHERE id_customer = ? ORDER BY id_product";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, customerId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.println("ID_PRODUCT | NOMBRE | ACTIVE | RANDOM | URL");
                System.out.println("-----------|--------|--------|--------|----");
                
                while (rs.next()) {
                    String url = rs.getString("url_redirect_success");
                    System.out.printf("%-10d | %-6s | %-6d | %-6d | %s%n",
                        rs.getLong("id_product"),
                        rs.getString("name") != null ? rs.getString("name").substring(0, Math.min(6, rs.getString("name").length())) : "NULL",
                        rs.getInt("active"),
                        rs.getInt("random"),
                        url != null ? url.substring(0, Math.min(30, url.length())) + "..." : "NULL"
                    );
                }
            }
        }
    }
    
    /**
     * Configurar productos con random=1
     */
    private static List<Long> configureRandomProducts(Connection conn, Long customerId, int limit) throws SQLException {
        List<Long> configuredProducts = new ArrayList<>();
        
        // Primero obtener productos activos
        String selectSql = "SELECT id_product FROM products WHERE id_customer = ? AND active = 1 ORDER BY id_product LIMIT ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(selectSql)) {
            pstmt.setLong(1, customerId);
            pstmt.setInt(2, limit);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    configuredProducts.add(rs.getLong("id_product"));
                }
            }
        }
        
        if (configuredProducts.isEmpty()) {
            System.out.println("WARNING: No se encontraron productos activos para el customer " + customerId);
            return configuredProducts;
        }
        
        // Actualizar productos seleccionados
        String updateSql = "UPDATE products SET random = 1 WHERE id_product = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
            for (Long productId : configuredProducts) {
                pstmt.setLong(1, productId);
                int updated = pstmt.executeUpdate();
                
                if (updated > 0) {
                    System.out.println("OK: Producto " + productId + " configurado con random=1");
                } else {
                    System.out.println("WARNING: No se pudo actualizar producto " + productId);
                }
            }
        }
        
        return configuredProducts;
    }
    
    /**
     * Mostrar productos con random=1
     */
    private static void showRandomProducts(Connection conn, Long customerId) throws SQLException {
        String sql = "SELECT id_product, name, active, random, url_redirect_success FROM products WHERE id_customer = ? AND random = 1 ORDER BY id_product";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, customerId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.println("ID_PRODUCT | NOMBRE | ACTIVE | RANDOM | URL");
                System.out.println("-----------|--------|--------|--------|----");
                
                int count = 0;
                while (rs.next()) {
                    count++;
                    String url = rs.getString("url_redirect_success");
                    System.out.printf("%-10d | %-6s | %-6d | %-6d | %s%n",
                        rs.getLong("id_product"),
                        rs.getString("name") != null ? rs.getString("name").substring(0, Math.min(6, rs.getString("name").length())) : "NULL",
                        rs.getInt("active"),
                        rs.getInt("random"),
                        url != null ? url.substring(0, Math.min(30, url.length())) + "..." : "NULL"
                    );
                }
                
                if (count == 0) {
                    System.out.println("WARNING: No hay productos configurados para redireccion inteligente");
                } else {
                    System.out.println("\nTOTAL: Total productos para smart redirect: " + count);
                }
            }
        }
    }
}
