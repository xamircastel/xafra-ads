package com.develop.job.ads.entities;

/**
 * Entidad para representar el performance de un producto
 * Incluye métricas de conversión y peso para algoritmo de distribución
 */
public class ProductPerformance {
	
	private Long productId;
	private String productName;
	private Long totalClicks;
	private Long conversions;
	private Double conversionRate;
	private Double weight; // Para algoritmo de distribución ponderada
	
	public ProductPerformance() {}
	
	public ProductPerformance(Long productId, String productName, Long totalClicks, Long conversions) {
		this.productId = productId;
		this.productName = productName;
		this.totalClicks = totalClicks;
		this.conversions = conversions;
		this.conversionRate = (totalClicks > 0) ? (conversions.doubleValue() / totalClicks.doubleValue()) : 0.0;
	}
	
	// Getters and Setters
	public Long getProductId() {
		return productId;
	}
	
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	
	public String getProductName() {
		return productName;
	}
	
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	public Long getTotalClicks() {
		return totalClicks;
	}
	
	public void setTotalClicks(Long totalClicks) {
		this.totalClicks = totalClicks;
		// Recalcular conversion rate
		this.conversionRate = (totalClicks > 0) ? (conversions.doubleValue() / totalClicks.doubleValue()) : 0.0;
	}
	
	public Long getConversions() {
		return conversions;
	}
	
	public void setConversions(Long conversions) {
		this.conversions = conversions;
		// Recalcular conversion rate
		this.conversionRate = (totalClicks > 0) ? (conversions.doubleValue() / totalClicks.doubleValue()) : 0.0;
	}
	
	public Double getConversionRate() {
		return conversionRate;
	}
	
	public void setConversionRate(Double conversionRate) {
		this.conversionRate = conversionRate;
	}
	
	public Double getWeight() {
		return weight;
	}
	
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	
	@Override
	public String toString() {
		return "ProductPerformance{" +
				"productId=" + productId +
				", productName='" + productName + '\'' +
				", totalClicks=" + totalClicks +
				", conversions=" + conversions +
				", conversionRate=" + String.format("%.2f%%", conversionRate * 100) +
				", weight=" + weight +
				'}';
	}
}
