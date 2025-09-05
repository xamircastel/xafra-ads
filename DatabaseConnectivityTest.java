package com.develop.job.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Test directo de conectividad a PostgreSQL en GCP
 * @author XafraTech
 */
public class DatabaseConnectivityTest {

    public static void main(String[] args) {
        System.out.println("Iniciando test de conectividad a PostgreSQL GCP...");
        System.out.println("============================================================");
        
        // Configuración de conexión desde db.properties
        String url = "jdbc:postgresql://34.28.245.62:5432/xafra-ads";
        String user = "postgres";
        String password = "XafraTech2025!";
        String driver = "org.postgresql.Driver";
        
        Connection connection = null;
        
        try {
            // 1. Test de driver
            System.out.println("1. Verificando driver PostgreSQL...");
            Class.forName(driver);
            System.out.println("OK - Driver PostgreSQL cargado correctamente");
            
            // 2. Test de conexión
            System.out.println("\n2. Intentando conectar a la base de datos...");
            System.out.println("   URL: " + url);
            System.out.println("   Usuario: " + user);
            
            connection = DriverManager.getConnection(url, user, password);
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
        } catch (Exception e) {
            System.err.println("ERROR inesperado: " + e.getMessage());
            e.printStackTrace();
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
    }
}
