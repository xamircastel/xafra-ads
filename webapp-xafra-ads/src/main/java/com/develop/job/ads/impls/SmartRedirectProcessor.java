package com.develop.job.ads.impls;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.commons.help.job.utils.Encryption;
import com.develop.job.db.dao.bi.ICampaign;
import com.develop.job.ads.bo.IProductPerformanceBI;
import com.develop.job.db.entity.Campaign;
import com.develop.job.ads.entities.ProductPerformance;
import com.develop.job.db.dao.bi.IProductAndCustomer;
import com.develop.job.db.entity.Product;

/**
 * Procesador de redirección inteligente basada en performance
 * Implementa algoritmo de distribución ponderada según conversiones
 */
@Service
public class SmartRedirectProcessor {

	private static final Logger log = LoggerFactory.getLogger(SmartRedirectProcessor.class);

	@Autowired
	private IProductAndCustomer prodAndCusBo;

	@Autowired
	private ICampaign campaignBo;

	@Autowired
	private IProductPerformanceBI performanceBo;

	@Value("${password.encription}")
	private String passEncription;

	private Random random = new Random();

	/**
	 * Procesa la redirección inteligente basada en performance
	 */
	public void processSmartRedirect(String customerParam, String tracking, boolean isAutoTracking, 
			HttpServletRequest request, HttpServletResponse response) {
		
		ExecutorService threadPool = Executors.newSingleThreadExecutor();
		
		try {
			log.info("START - Smart redirect processing for customer: {}, tracking: {}", customerParam, tracking);
			
			// 1. Desencriptar customer ID
			Long customerId = decryptCustomerId(customerParam);
			if (customerId == null || customerId == 0) {
				log.error("Invalid customer ID from parameter: {}", customerParam);
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}
			
			log.info("Decrypted customer ID: {}", customerId);
			
			// 2. Obtener productos con random=1 y activos
			List<Product> randomProducts = prodAndCusBo.getRandomProductsByCustomer(customerId);
			if (randomProducts.isEmpty()) {
				log.error("No random products found for customer: {}", customerId);
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				return;
			}
			
			log.info("Found {} random products for customer {}", randomProducts.size(), customerId);
			
			// 3. Calcular performance de productos (últimas 24h)
			List<ProductPerformance> performances = performanceBo.calculatePerformanceLast24Hours(randomProducts);
			
			// 4. Seleccionar producto basado en algoritmo de performance
			Product selectedProduct = selectProductByPerformance(performances, randomProducts);
			
			if (selectedProduct == null) {
				log.error("Could not select any product for customer: {}", customerId);
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				return;
			}
			
			log.info("Selected product: {} (ID: {}) for customer: {}", 
					selectedProduct.getName(), selectedProduct.getIdProduct(), customerId);
			
			// 5. Generar campaign y redirigir
			Campaign campaign = buildCampaign(selectedProduct, tracking, isAutoTracking);
			insertCampaign(threadPool, campaign, request);
			
			// 6. Preparar URL de redirección
			String redirectUrl = prepareRedirectUrl(selectedProduct.getUrlRedirectSuccess(), tracking);
			
			// 7. Ejecutar redirección
			redirect(response, redirectUrl);
			
			log.info("END - Smart redirect completed. Redirected to: {}", redirectUrl);
			
		} catch (Exception e) {
			log.error("Error in smart redirect processing: {}", e.getMessage(), e);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		} finally {
			threadPool.shutdown();
		}
	}

	/**
	 * Desencripta el customer ID del parámetro
	 */
	private Long decryptCustomerId(String encryptedParam) {
		try {
			// Restaurar caracteres especiales
			String paramWithSlashes = encryptedParam.replace("$", "/");
			
			// Restaurar padding de Base64 si es necesario
			int padLength = (4 - (paramWithSlashes.length() % 4)) % 4;
			for (int i = 0; i < padLength; i++) {
				paramWithSlashes += "=";
			}
			
			String decrypted = Encryption.decrypt(paramWithSlashes, passEncription);
			return Long.valueOf(decrypted);
		} catch (Exception e) {
			log.error("Error decrypting customer parameter: {}", e.getMessage());
			return null;
		}
	}

	/**
	 * Selecciona producto basado en algoritmo de performance
	 */
	private Product selectProductByPerformance(List<ProductPerformance> performances, List<Product> products) {
		
		if (performances.isEmpty()) {
			// Sin datos de performance → distribución equitativa
			log.info("No performance data available, using random selection");
			return products.get(random.nextInt(products.size()));
		}
		
		// Calcular peso total para distribución ponderada
		double totalWeight = 0.0;
		for (ProductPerformance perf : performances) {
			// Peso = (conversiones + 1) para dar oportunidad a productos sin conversiones
			double weight = (perf.getConversions() + 1) * (perf.getConversionRate() + 0.1);
			perf.setWeight(weight);
			totalWeight += weight;
			
			log.debug("Product ID: {}, Conversions: {}, Rate: {:.2f}%, Weight: {:.3f}", 
					perf.getProductId(), perf.getConversions(), perf.getConversionRate() * 100, weight);
		}
		
		// Selección ponderada aleatoria
		double randomValue = random.nextDouble() * totalWeight;
		double currentWeight = 0.0;
		
		for (ProductPerformance perf : performances) {
			currentWeight += perf.getWeight();
			if (randomValue <= currentWeight) {
				// Encontrar producto correspondiente
				return products.stream()
						.filter(p -> p.getIdProduct().equals(perf.getProductId()))
						.findFirst()
						.orElse(products.get(0)); // Fallback
			}
		}
		
		// Fallback - retornar primer producto
		return products.get(0);
	}

	/**
	 * Construye el objeto Campaign
	 */
	private Campaign buildCampaign(Product product, String tracking, boolean isAutoTracking) {
		Campaign campaign = new Campaign();
		campaign.setProductId(product.getIdProduct());
		campaign.setTraking(tracking);
		campaign.setStatus(0); // Pendiente de conversión
		campaign.setStatusPostBack(0);
		campaign.setCountry(product.getCountry());
		campaign.setOperator(product.getOperator());
		
		// Generar xafra_tracking_id interno
		if (isAutoTracking) {
			String internalTracking = generateXafraInternalTracking();
			campaign.setXafraTrackingId(internalTracking);
			log.info("Generated internal tracking: {}", internalTracking);
		}
		
		return campaign;
	}

	/**
	 * Genera tracking interno de Xafra
	 */
	private String generateXafraInternalTracking() {
		java.time.ZonedDateTime now = java.time.ZonedDateTime.now(java.time.ZoneOffset.UTC);
		java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
		String timestamp = now.format(formatter);
		String uuid = java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
		return "XFR_" + timestamp + "_" + uuid;
	}

	/**
	 * Inserta campaign en base de datos de forma asíncrona
	 */
	private void insertCampaign(ExecutorService threadPool, Campaign campaign, HttpServletRequest request) {
		threadPool.execute(() -> {
			try {
				campaign.setUuid(java.util.UUID.randomUUID().toString());
				campaign.setCreationDate(new java.sql.Timestamp(System.currentTimeMillis()));
				
				// Guardar datos de request si es necesario
				String userAgent = request.getHeader("User-Agent");
				String remoteAddr = request.getRemoteAddr();
				
				log.info("Inserting campaign - Product: {}, Tracking: {}, Remote: {}", 
						campaign.getProductId(), campaign.getTraking(), remoteAddr);
				
				campaignBo.insert(campaign);
				
				log.info("Campaign inserted successfully with ID: {}", campaign.getId());
				
			} catch (Exception e) {
				log.error("Error inserting campaign: {}", e.getMessage(), e);
			}
		});
	}

	/**
	 * Prepara URL de redirección agregando parámetros de tracking
	 */
	private String prepareRedirectUrl(String baseUrl, String tracking) {
		if (baseUrl.contains("?")) {
			return baseUrl + "&tracking=" + tracking;
		} else {
			return baseUrl + "?tracking=" + tracking;
		}
	}

	/**
	 * Ejecuta la redirección HTTP 302
	 */
	private void redirect(HttpServletResponse response, String url) {
		try {
			response.sendRedirect(url);
			log.debug("Redirect executed to: {}", url);
		} catch (IOException e) {
			log.error("Error executing redirect: {}", e.getMessage());
		}
	}
}
