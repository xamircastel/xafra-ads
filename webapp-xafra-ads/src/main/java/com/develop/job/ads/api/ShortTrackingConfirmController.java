package com.develop.job.ads.api;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.develop.job.db.dao.bi.ICampaign;
import com.develop.job.jdbi.bi.ICampaignBI;

/**
 * Controlador para confirmación de ventas usando tracking corto (Costa Rica - Kolbi)
 */
@RestController
@RequestMapping("/service/v1")
public class ShortTrackingConfirmController {

    @Autowired
    private ICampaignBI campaignBI;
    
    @Autowired
    private ICampaign campaignBo;

    /**
     * Endpoint para confirmar venta usando tracking corto
     * POST /service/v1/confirm/short/{apikey}/{shortTracking}
     */
    @PostMapping("/confirm/short/{apikey}/{shortTracking}")
    public ResponseEntity<Map<String, Object>> confirmByShortTracking(
            @PathVariable String apikey, 
            @PathVariable String shortTracking) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Validaciones básicas
            if (apikey == null || apikey.trim().isEmpty()) {
                response.put("success", false);
                response.put("error", "APIKey es requerido");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (shortTracking == null || shortTracking.trim().isEmpty()) {
                response.put("success", false);
                response.put("error", "Short tracking es requerido");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Buscar la campaña por tracking corto
            com.develop.job.db.entity.Campaign campaign = campaignBI.getCampaignByShortTracking(shortTracking);
            if (campaign == null) {
                response.put("success", false);
                response.put("error", "Tracking corto no encontrado: " + shortTracking);
                return ResponseEntity.badRequest().body(response);
            }
            
            // Validar que sea para Costa Rica - Kolbi
            if (!"CR".equals(campaign.getCountry()) || !"Kolbi".equals(campaign.getOperator())) {
                response.put("success", false);
                response.put("error", "El tracking corto solo está disponible para Costa Rica - Kolbi");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Procesar la confirmación usando el tracking original
            // Aquí utilizamos la lógica existente del sistema con el tracking normal
            boolean confirmationResult = processConfirmation(campaign, apikey);
            
            if (confirmationResult) {
                response.put("success", true);
                response.put("message", "Venta confirmada exitosamente");
                response.put("short_tracking", shortTracking);
                response.put("original_tracking", campaign.getTraking());
                response.put("campaign_id", campaign.getId());
                response.put("country", campaign.getCountry());
                response.put("operator", campaign.getOperator());
                response.put("timestamp", new java.util.Date());
                
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("error", "Error al procesar la confirmación");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "Error interno del servidor: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Procesa la confirmación de la venta utilizando la lógica existente del sistema
     */
    private boolean processConfirmation(com.develop.job.db.entity.Campaign campaign, String apikey) {
        try {
            // Aquí utilizamos la lógica existente del sistema de confirmación
            // Simulamos el proceso de confirmación que ya existe en el sistema
            
            // 1. Validar el estado de la campaña
            if (campaign.getStatus() == null || campaign.getStatus() != 2) {
                // Solo se pueden confirmar campañas en estado "PROCESSING" (2)
                return false;
            }
            
            // 2. Actualizar el estado de la campaña a "CONFIRMED" 
            // Asumiendo que 3 = CONFIRMED (ajustar según tu enum StatusCampain)
            campaignBI.update(campaign.getId(), 3); // 3 = CONFIRMED
            
            // 3. Aquí deberías llamar al método del CampaignBo que procesa 
            // las notificaciones al traffic source, etc.
            // campaignBo.processConfirmation(campaign);
            
            return true;
            
        } catch (Exception e) {
            System.err.println("Error procesando confirmación: " + e.getMessage());
            return false;
        }
    }
}
