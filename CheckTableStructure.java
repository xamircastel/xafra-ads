import java.sql.*;

/**
 * Script para verificar la estructura actual de las tablas campaign, product y customer
 * e identificar si existen campos de país y operador
 */
public class CheckTableStructure {
    
    private static final String URL = "jdbc:postgresql://34.28.245.62:5432/xafra-ads";
    private static final String USER = "postgres";
    private static final String PASSWORD = "XafraTech2025!";
    
    public static void main(String[] args) {
        try {
            Class.forName("org.postgresql.Driver");
            
            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
                System.out.println("=== ESTRUCTURA DE TABLAS - ANALISIS PAIS/OPERADOR ===\n");
                
                // Verificar estructura de tabla campaign
                System.out.println("1. TABLA CAMPAIGN:");
                checkTableStructure(connection, "campaign");
                
                System.out.println("\n2. TABLA PRODUCTS:");
                checkTableStructure(connection, "products");
                
                System.out.println("\n3. TABLA CUSTOMERS:");
                checkTableStructure(connection, "customers");
                
                // Verificar algunos registros de ejemplo
                System.out.println("\n=== ANALISIS DE DATOS EXISTENTES ===");
                
                System.out.println("\n4. MUESTRA DE CAMPAIGN (primeros 3 registros):");
                checkSampleData(connection, "campaign", 3);
                
                System.out.println("\n5. MUESTRA DE PRODUCTS (primeros 3 registros):");
                checkSampleData(connection, "products", 3);
                
                System.out.println("\n6. MUESTRA DE CUSTOMERS (primeros 3 registros):");
                checkSampleData(connection, "customers", 3);
                
            }
            
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void checkTableStructure(Connection connection, String tableName) {
        try {
            DatabaseMetaData metadata = connection.getMetaData();
            ResultSet columns = metadata.getColumns(null, null, tableName, null);
            
            System.out.println("   Columnas en tabla '" + tableName + "':");
            boolean hasCountry = false;
            boolean hasOperator = false;
            
            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                String dataType = columns.getString("TYPE_NAME");
                int columnSize = columns.getInt("COLUMN_SIZE");
                String isNullable = columns.getString("IS_NULLABLE");
                
                System.out.printf("   - %-20s | %-15s | Size: %-5d | Nullable: %s%n", 
                    columnName, dataType, columnSize, isNullable);
                
                // Detectar campos relacionados con país/operador
                if (columnName.toLowerCase().contains("country") || columnName.toLowerCase().contains("pais")) {
                    hasCountry = true;
                }
                if (columnName.toLowerCase().contains("operator") || columnName.toLowerCase().contains("operador")) {
                    hasOperator = true;
                }
            }
            
            System.out.println("   * Pais detectado: " + (hasCountry ? "SI" : "NO"));
            System.out.println("   * Operador detectado: " + (hasOperator ? "SI" : "NO"));
            
            columns.close();
            
        } catch (SQLException e) {
            System.err.println("   ERROR al analizar tabla " + tableName + ": " + e.getMessage());
        }
    }
    
    private static void checkSampleData(Connection connection, String tableName, int limit) {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName + " LIMIT " + limit);
            
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            
            // Mostrar headers
            System.out.print("   ");
            for (int i = 1; i <= columnCount; i++) {
                System.out.printf("%-15s | ", rsmd.getColumnName(i));
            }
            System.out.println();
            
            // Mostrar datos
            int rowCount = 0;
            while (rs.next() && rowCount < limit) {
                System.out.print("   ");
                for (int i = 1; i <= columnCount; i++) {
                    String value = rs.getString(i);
                    if (value != null && value.length() > 12) {
                        value = value.substring(0, 12) + "...";
                    }
                    System.out.printf("%-15s | ", value != null ? value : "NULL");
                }
                System.out.println();
                rowCount++;
            }
            
            rs.close();
            stmt.close();
            
        } catch (SQLException e) {
            System.err.println("   ERROR al obtener datos de " + tableName + ": " + e.getMessage());
        }
    }
}
