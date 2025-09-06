package com.develop.job.rest.controller;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/migration")
public class MigrationController {
    
    @Autowired
    private DataSource dataSource;
    
    @GetMapping("/check-tables")
    public String checkTableStructure() {
        StringBuilder result = new StringBuilder();
        
        try (Connection conn = dataSource.getConnection()) {
            result.append("=== VERIFICACIÓN DE ESTRUCTURA DE TABLAS ===\n\n");
            
            // Verificar tabla products
            result.append("TABLA PRODUCTS:\n");
            result.append(getTableColumns(conn, "products"));
            result.append("\n");
            
            // Verificar tabla customers
            result.append("TABLA CUSTOMERS:\n");
            result.append(getTableColumns(conn, "customers"));
            result.append("\n");
            
            // Verificar tabla campaigns
            result.append("TABLA CAMPAIGNS:\n");
            result.append(getTableColumns(conn, "campaigns"));
            result.append("\n");
            
        } catch (SQLException e) {
            result.append("Error: ").append(e.getMessage());
        }
        
        return result.toString();
    }
    
    @PostMapping("/execute-migration")
    public String executeMigration() {
        StringBuilder result = new StringBuilder();
        
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);
            
            result.append("=== EJECUTANDO MIGRACIÓN ===\n\n");
            
            try (Statement stmt = conn.createStatement()) {
                
                // Verificar si las columnas ya existen
                if (!columnExists(conn, "products", "country")) {
                    result.append("Agregando columna 'country' a tabla products...\n");
                    stmt.execute("ALTER TABLE products ADD COLUMN country VARCHAR(5)");
                } else {
                    result.append("Columna 'country' ya existe en tabla products\n");
                }
                
                if (!columnExists(conn, "products", "operator")) {
                    result.append("Agregando columna 'operator' a tabla products...\n");
                    stmt.execute("ALTER TABLE products ADD COLUMN operator VARCHAR(50)");
                } else {
                    result.append("Columna 'operator' ya existe en tabla products\n");
                }
                
                if (!columnExists(conn, "customers", "country")) {
                    result.append("Agregando columna 'country' a tabla customers...\n");
                    stmt.execute("ALTER TABLE customers ADD COLUMN country VARCHAR(5)");
                } else {
                    result.append("Columna 'country' ya existe en tabla customers\n");
                }
                
                if (!columnExists(conn, "customers", "operator")) {
                    result.append("Agregando columna 'operator' a tabla customers...\n");
                    stmt.execute("ALTER TABLE customers ADD COLUMN operator VARCHAR(50)");
                } else {
                    result.append("Columna 'operator' ya existe en tabla customers\n");
                }
                
                // Crear índices si no existen
                result.append("Creando índices...\n");
                
                try {
                    stmt.execute("CREATE INDEX IF NOT EXISTS idx_products_country ON products(country)");
                    result.append("Índice idx_products_country creado\n");
                } catch (SQLException e) {
                    result.append("Índice idx_products_country ya existe o error: ").append(e.getMessage()).append("\n");
                }
                
                try {
                    stmt.execute("CREATE INDEX IF NOT EXISTS idx_products_operator ON products(operator)");
                    result.append("Índice idx_products_operator creado\n");
                } catch (SQLException e) {
                    result.append("Índice idx_products_operator ya existe o error: ").append(e.getMessage()).append("\n");
                }
                
                try {
                    stmt.execute("CREATE INDEX IF NOT EXISTS idx_customers_country ON customers(country)");
                    result.append("Índice idx_customers_country creado\n");
                } catch (SQLException e) {
                    result.append("Índice idx_customers_country ya existe o error: ").append(e.getMessage()).append("\n");
                }
                
                try {
                    stmt.execute("CREATE INDEX IF NOT EXISTS idx_customers_operator ON customers(operator)");
                    result.append("Índice idx_customers_operator creado\n");
                } catch (SQLException e) {
                    result.append("Índice idx_customers_operator ya existe o error: ").append(e.getMessage()).append("\n");
                }
                
                conn.commit();
                result.append("\n✅ MIGRACIÓN COMPLETADA EXITOSAMENTE\n");
                
            } catch (SQLException e) {
                conn.rollback();
                result.append("\n❌ ERROR EN MIGRACIÓN: ").append(e.getMessage()).append("\n");
                result.append("Transacción revertida.\n");
            }
            
        } catch (SQLException e) {
            result.append("Error de conexión: ").append(e.getMessage());
        }
        
        return result.toString();
    }
    
    private String getTableColumns(Connection conn, String tableName) throws SQLException {
        StringBuilder columns = new StringBuilder();
        DatabaseMetaData metaData = conn.getMetaData();
        
        try (ResultSet rs = metaData.getColumns(null, null, tableName, null)) {
            while (rs.next()) {
                String columnName = rs.getString("COLUMN_NAME");
                String dataType = rs.getString("TYPE_NAME");
                int columnSize = rs.getInt("COLUMN_SIZE");
                String isNullable = rs.getString("IS_NULLABLE");
                
                columns.append("  ")
                       .append(columnName)
                       .append(" (")
                       .append(dataType)
                       .append("(")
                       .append(columnSize)
                       .append("), nullable: ")
                       .append(isNullable)
                       .append(")\n");
            }
        }
        
        return columns.toString();
    }
    
    private boolean columnExists(Connection conn, String tableName, String columnName) throws SQLException {
        DatabaseMetaData metaData = conn.getMetaData();
        try (ResultSet rs = metaData.getColumns(null, null, tableName, columnName)) {
            return rs.next();
        }
    }
}
