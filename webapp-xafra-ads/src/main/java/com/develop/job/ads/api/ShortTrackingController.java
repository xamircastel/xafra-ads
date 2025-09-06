package com.develop.job.ads.api;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.develop.job.ads.constants.CountryOperatorConstants;
import com.develop.job.jdbi.bi.ICampaignBI;

/**
 * Controlador para el sistema de tracking corto de Costa Rica - Kolbi
 */
@RestController
@RequestMapping("/api/config")
public class ShortTrackingController {

    @Autowired
    private ICampaignBI campaignBI;

    /**
     * Endpoint para que el customer notifique el tracking corto generado
     * POST /api/config/xafra
     */
    @PostMapping("/xafra")
    public ResponseEntity<Map<String, Object>> configureShortTracking(@RequestBody Map<String, Object> request) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Validar request
            String apikey = (String) request.get("apikey");
            String originalTracking = (String) request.get("original_tracking");
            String shortTracking = (String) request.get("short_tracking");
            Boolean enabled = (Boolean) request.get("enabled");
            
            // Validaciones básicas
            if (apikey == null || apikey.trim().isEmpty()) {
                response.put("success", false);
                response.put("error", "Campo 'apikey' es requerido");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (originalTracking == null || originalTracking.trim().isEmpty()) {
                response.put("success", false);
                response.put("error", "Campo 'original_tracking' es requerido");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (shortTracking == null || shortTracking.trim().isEmpty()) {
                response.put("success", false);
                response.put("error", "Campo 'short_tracking' es requerido");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Validar que el tracking original existe
            com.develop.job.db.entity.Campaign campaign = campaignBI.getCampainByTraking(originalTracking);
            if (campaign == null) {
                response.put("success", false);
                response.put("error", "Tracking original no encontrado: " + originalTracking);
                return ResponseEntity.badRequest().body(response);
            }
            
            // Validar que sea para Costa Rica - Kolbi
            if (!"CR".equals(campaign.getCountry()) || !"Kolbi".equals(campaign.getOperator())) {
                response.put("success", false);
                response.put("error", "El tracking corto solo está disponible para Costa Rica - Kolbi");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Verificar que el short_tracking no exista ya
            com.develop.job.db.entity.Campaign existingCampaign = campaignBI.getCampaignByShortTracking(shortTracking);
            if (existingCampaign != null) {
                response.put("success", false);
                response.put("error", "El tracking corto ya existe: " + shortTracking);
                return ResponseEntity.badRequest().body(response);
            }
            
            // Actualizar el campaign con el tracking corto
            campaignBI.updateShortTracking(originalTracking, shortTracking);
            
            // Respuesta exitosa
            response.put("success", true);
            response.put("message", "Tracking corto configurado exitosamente");
            response.put("original_tracking", originalTracking);
            response.put("short_tracking", shortTracking);
            response.put("country", campaign.getCountry());
            response.put("operator", campaign.getOperator());
            response.put("enabled", enabled != null ? enabled : true);
            response.put("timestamp", new java.util.Date());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "Error interno del servidor: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
