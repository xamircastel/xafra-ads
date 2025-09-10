package com.develop.job.ads.bo;

import java.util.List;

import com.develop.job.ads.entities.ProductPerformance;
import com.develop.job.db.entity.Product;

/**
 * Business Object Interface para cálculo de performance de productos
 */
public interface IProductPerformanceBI {
	
	/**
	 * Calcula el performance de productos en las últimas 24 horas
	 * @param products Lista de productos a evaluar
	 * @return Lista de ProductPerformance con métricas calculadas
	 */
	List<ProductPerformance> calculatePerformanceLast24Hours(List<Product> products);
	
	/**
	 * Obtiene performance de un producto específico
	 * @param productId ID del producto
	 * @return ProductPerformance del producto
	 */
	ProductPerformance getProductPerformance(Long productId);
	
	/**
	 * Obtiene performance de productos por customer en período específico
	 * @param customerId ID del customer
	 * @param hoursBack Horas hacia atrás desde ahora
	 * @return Lista de ProductPerformance
	 */
	List<ProductPerformance> getCustomerProductsPerformance(Long customerId, int hoursBack);
	
}
