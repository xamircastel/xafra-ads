import java.sql.*;

/**
 * Script para ver la estructura de la tabla products
 */
public class CheckProductsTable {
    
    private static final String DB_URL = "jdbc:postgresql://34.28.245.62:5432/xafra-ads";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "XafraTech2025!";
    
    public static void main(String[] args) {
        Connection conn = null;
        
        try {
            // Cargar driver PostgreSQL
            Class.forName("org.postgresql.Driver");
            
            // Conectar a la base de datos
            System.out.println("CONECTANDO: Conectando a la base de datos...");
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("OK: Conexion exitosa!");
            
            // Ver estructura de la tabla products
            System.out.println("\nESTRUCTURA: Columnas de la tabla products:");
            showTableStructure(conn, "products");
            
            // Ver algunos productos del customer 13
            System.out.println("\nDATA: Productos del customer 13:");
            showCustomer13Products(conn);
            
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
    
    private static void showTableStructure(Connection conn, String tableName) throws SQLException {
        String sql = "SELECT column_name, data_type, is_nullable FROM information_schema.columns WHERE table_name = ? ORDER BY ordinal_position";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tableName);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.println("COLUMN_NAME | DATA_TYPE | NULLABLE");
                System.out.println("------------|-----------|----------");
                
                while (rs.next()) {
                    System.out.printf("%-15s | %-10s | %s%n",
                        rs.getString("column_name"),
                        rs.getString("data_type"),
                        rs.getString("is_nullable")
                    );
                }
            }
        }
    }
    
    private static void showCustomer13Products(Connection conn) throws SQLException {
        String sql = "SELECT id_product, name, active, random FROM products WHERE id_customer = 13 ORDER BY id_product LIMIT 5";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.println("ID_PRODUCT | NOMBRE | ACTIVE | RANDOM");
                System.out.println("-----------|--------|--------|--------");
                
                while (rs.next()) {
                    System.out.printf("%-10d | %-6s | %-6d | %-6d%n",
                        rs.getLong("id_product"),
                        rs.getString("name") != null ? rs.getString("name").substring(0, Math.min(6, rs.getString("name").length())) : "NULL",
                        rs.getInt("active"),
                        rs.getInt("random")
                    );
                }
            }
        }
    }
}
