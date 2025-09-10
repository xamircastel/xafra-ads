package com.develop.job.ads.bo.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.develop.job.ads.bo.IProductPerformanceBI;
import com.develop.job.ads.entities.ProductPerformance;
import com.develop.job.db.entity.Product;

/**
 * Implementación de cálculo de performance de productos
 */
@Service
public class ProductPerformanceBI implements IProductPerformanceBI {

	private static final Logger log = LoggerFactory.getLogger(ProductPerformanceBI.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<ProductPerformance> calculatePerformanceLast24Hours(List<Product> products) {
		List<ProductPerformance> performances = new ArrayList<>();
		
		if (products.isEmpty()) {
			return performances;
		}
		
		try {
			// Construir lista de IDs para la consulta
			String productIds = products.stream()
					.map(p -> p.getIdProduct().toString())
					.reduce((a, b) -> a + "," + b)
					.orElse("0");
			
			String sql = String.format(
				"SELECT " +
				"p.id_product, " +
				"p.name as product_name, " +
				"COUNT(c.id) as total_clicks, " +
				"COUNT(CASE WHEN c.status = 1 THEN 1 END) as conversions " +
				"FROM products p " +
				"LEFT JOIN campaign c ON p.id_product = c.id_product " +
				"AND c.creation_date >= NOW() - INTERVAL '24 HOURS' " +
				"WHERE p.id_product IN (%s) " +
				"AND p.active = 1 " +
				"AND p.random = 1 " +
				"GROUP BY p.id_product, p.name " +
				"ORDER BY conversions DESC, total_clicks DESC", 
				productIds);
			
			log.debug("Executing performance query: {}", sql);
			
			performances = jdbcTemplate.query(sql, (rs, rowNum) -> {
				Long productId = rs.getLong("id_product");
				String productName = rs.getString("product_name");
				Long totalClicks = rs.getLong("total_clicks");
				Long conversions = rs.getLong("conversions");
				
				ProductPerformance perf = new ProductPerformance(productId, productName, totalClicks, conversions);
				
				log.debug("Product {}: {} clicks, {} conversions, {:.2f}% rate", 
						productName, totalClicks, conversions, perf.getConversionRate() * 100);
				
				return perf;
			});
			
			log.info("Calculated performance for {} products", performances.size());
			
		} catch (DataAccessException e) {
			log.error("Error calculating product performance: {}", e.getMessage(), e);
		}
		
		return performances;
	}

	@Override
	public ProductPerformance getProductPerformance(Long productId) {
		try {
			String sql = 
				"SELECT " +
				"p.id_product, " +
				"p.name as product_name, " +
				"COUNT(c.id) as total_clicks, " +
				"COUNT(CASE WHEN c.status = 1 THEN 1 END) as conversions " +
				"FROM products p " +
				"LEFT JOIN campaign c ON p.id_product = c.id_product " +
				"AND c.creation_date >= NOW() - INTERVAL '24 HOURS' " +
				"WHERE p.id_product = ? " +
				"GROUP BY p.id_product, p.name";
			
			return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
				String productName = rs.getString("product_name");
				Long totalClicks = rs.getLong("total_clicks");
				Long conversions = rs.getLong("conversions");
				
				return new ProductPerformance(productId, productName, totalClicks, conversions);
			}, productId);
			
		} catch (DataAccessException e) {
			log.error("Error getting performance for product {}: {}", productId, e.getMessage());
			return new ProductPerformance(productId, "Unknown", 0L, 0L);
		}
	}

	@Override
	public List<ProductPerformance> getCustomerProductsPerformance(Long customerId, int hoursBack) {
		List<ProductPerformance> performances = new ArrayList<>();
		
		try {
			String sql = String.format(
				"SELECT " +
				"p.id_product, " +
				"p.name as product_name, " +
				"COUNT(c.id) as total_clicks, " +
				"COUNT(CASE WHEN c.status = 1 THEN 1 END) as conversions " +
				"FROM products p " +
				"LEFT JOIN campaign c ON p.id_product = c.id_product " +
				"AND c.creation_date >= NOW() - INTERVAL '%d HOURS' " +
				"WHERE p.id_customer = ? " +
				"AND p.active = 1 " +
				"AND p.random = 1 " +
				"GROUP BY p.id_product, p.name " +
				"ORDER BY conversions DESC, total_clicks DESC", 
				hoursBack);
			
			performances = jdbcTemplate.query(sql, (rs, rowNum) -> {
				Long productId = rs.getLong("id_product");
				String productName = rs.getString("product_name");
				Long totalClicks = rs.getLong("total_clicks");
				Long conversions = rs.getLong("conversions");
				
				return new ProductPerformance(productId, productName, totalClicks, conversions);
			}, customerId);
			
			log.info("Retrieved performance for customer {} - {} products in last {} hours", 
					customerId, performances.size(), hoursBack);
			
		} catch (DataAccessException e) {
			log.error("Error getting customer {} performance: {}", customerId, e.getMessage(), e);
		}
		
		return performances;
	}
}
