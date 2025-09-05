import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Test de conectividad a PostgreSQL en GCP
 * Creado para verificar la conexión a la base de datos xafra-ads
 */
public class TestDB {
    
    // Configuración de base de datos
    private static final String DRIVER = "org.postgresql.Driver";
    private static final String URL = "jdbc:postgresql://34.28.245.62:5432/xafra-ads";
    private static final String USER = "postgres";
    private static final String PASSWORD = "XafraTech2025!";
    
    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("TEST DE CONECTIVIDAD - POSTGRESQL EN GCP");
        System.out.println("============================================================");
        System.out.println("Base de datos: xafra-ads");
        System.out.println("Servidor: 34.28.245.62:5432");
        System.out.println("Usuario: postgres");
        System.out.println("Fecha: " + new java.util.Date());
        System.out.println("============================================================");
        
        Connection connection = null;
        boolean success = true;
        
        try {
            // 1. Test de driver
            System.out.println("1. Verificando driver PostgreSQL...");
            Class.forName(DRIVER);
            System.out.println("OK - Driver PostgreSQL cargado correctamente");
            
            // 2. Test de conexión
            System.out.println("\n2. Intentando conectar a la base de datos...");
            System.out.println("   URL: " + URL);
            System.out.println("   Usuario: " + USER);
            
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("OK - Conexion establecida exitosamente!");
            
            // 3. Obtener metadatos
            System.out.println("\n3. Informacion de la base de datos:");
            DatabaseMetaData metaData = connection.getMetaData();
            
            System.out.println("   Producto: " + metaData.getDatabaseProductName());
            System.out.println("   Version: " + metaData.getDatabaseProductVersion());
            System.out.println("   Driver: " + metaData.getDriverName());
            System.out.println("   Version Driver: " + metaData.getDriverVersion());
            System.out.println("   Catalogo: " + connection.getCatalog());
            System.out.println("   Schema: " + connection.getSchema());
            System.out.println("   Solo Lectura: " + connection.isReadOnly());
            System.out.println("   Auto-commit: " + connection.getAutoCommit());
            
            // 4. Test de consulta básica
            System.out.println("\n4. Test de consulta basica...");
            try (Statement stmt = connection.createStatement()) {
                ResultSet rs = stmt.executeQuery("SELECT version(), current_database(), current_user, now()");
                if (rs.next()) {
                    System.out.println("   Version PostgreSQL: " + rs.getString(1));
                    System.out.println("   Base de datos actual: " + rs.getString(2));
                    System.out.println("   Usuario actual: " + rs.getString(3));
                    System.out.println("   Fecha/Hora servidor: " + rs.getTimestamp(4));
                }
                rs.close();
            }
            
            // 5. Test de tablas existentes
            System.out.println("\n5. Verificando tablas en el esquema 'public'...");
            try (Statement stmt = connection.createStatement()) {
                ResultSet rs = stmt.executeQuery(
                    "SELECT table_name, table_type FROM information_schema.tables " +
                    "WHERE table_schema = 'public' ORDER BY table_name"
                );
                
                int tableCount = 0;
                while (rs.next()) {
                    tableCount++;
                    System.out.println("   Tabla: " + rs.getString("table_name") + " (" + rs.getString("table_type") + ")");
                }
                
                if (tableCount == 0) {
                    System.out.println("   WARNING - No se encontraron tablas en el esquema 'public'");
                } else {
                    System.out.println("   OK - Se encontraron " + tableCount + " tablas");
                }
                rs.close();
            }
            
            // 6. Test de tabla campaign específicamente
            System.out.println("\n6. Verificando tabla 'campaign'...");
            try (Statement stmt = connection.createStatement()) {
                ResultSet rs = stmt.executeQuery(
                    "SELECT COUNT(*) as record_count FROM campaign"
                );
                if (rs.next()) {
                    int count = rs.getInt("record_count");
                    System.out.println("   Registros en tabla 'campaign': " + count);
                } else {
                    System.out.println("   WARNING - No se pudo obtener el conteo de registros");
                }
                rs.close();
            } catch (SQLException e) {
                System.out.println("   ERROR - Error al acceder a tabla 'campaign': " + e.getMessage());
                System.out.println("   INFO - Posible causa: La tabla no existe o no hay permisos");
            }
            
            // 7. Test de performance básico
            System.out.println("\n7. Test basico de performance...");
            long startTime = System.currentTimeMillis();
            try (Statement stmt = connection.createStatement()) {
                ResultSet rs = stmt.executeQuery("SELECT 1");
                rs.next();
                rs.close();
            }
            long endTime = System.currentTimeMillis();
            System.out.println("   Tiempo de respuesta: " + (endTime - startTime) + "ms");
            
            System.out.println("\nTEST COMPLETADO EXITOSAMENTE!");
            System.out.println("============================================================");
            System.out.println("OK - La conectividad con PostgreSQL en GCP esta funcionando correctamente");
            
        } catch (ClassNotFoundException e) {
            System.err.println("ERROR: Driver PostgreSQL no encontrado");
            System.err.println("   Detalle: " + e.getMessage());
            System.err.println("   Solucion: Verificar que postgresql-xx.jar este en el classpath");
            success = false;
        } catch (SQLException e) {
            System.err.println("ERROR de conectividad SQL");
            System.err.println("   Codigo de error: " + e.getErrorCode());
            System.err.println("   Estado SQL: " + e.getSQLState());
            System.err.println("   Mensaje: " + e.getMessage());
            
            // Diagnosticar errores comunes
            if (e.getMessage().contains("Connection refused")) {
                System.err.println("\nDiagnostico:");
                System.err.println("   - Verificar que el servidor PostgreSQL este ejecutandose");
                System.err.println("   - Comprobar la direccion IP y puerto");
                System.err.println("   - Revisar configuracion de firewall");
            } else if (e.getMessage().contains("authentication failed")) {
                System.err.println("\nDiagnostico:");
                System.err.println("   - Verificar usuario y password");
                System.err.println("   - Comprobar permisos de acceso del usuario");
            } else if (e.getMessage().contains("database") && e.getMessage().contains("does not exist")) {
                System.err.println("\nDiagnostico:");
                System.err.println("   - Verificar que la base de datos 'xafra-ads' exista");
                System.err.println("   - Comprobar permisos de acceso a la base de datos");
            }
            success = false;
        } catch (Exception e) {
            System.err.println("ERROR inesperado: " + e.getMessage());
            e.printStackTrace();
            success = false;
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                    System.out.println("\nConexion cerrada correctamente");
                } catch (SQLException e) {
                    System.err.println("WARNING - Error al cerrar conexion: " + e.getMessage());
                }
            }
        }
        
        System.out.println("\n" + "============================================================");
        if (success) {
            System.out.println("RESULTADO: CONECTIVIDAD EXITOSA - La base de datos esta accesible");
        } else {
            System.out.println("RESULTADO: CONECTIVIDAD FALLIDA - Revisar configuracion y logs");
        }
        System.out.println("============================================================");
        
        System.exit(success ? 0 : 1);
    }
}
