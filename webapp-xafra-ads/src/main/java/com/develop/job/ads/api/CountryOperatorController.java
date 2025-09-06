package com.develop.job.ads.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.develop.job.ads.constants.CountryOperatorConstants;

/**
 * Controlador para gestionar información de países y operadores
 */
@RestController
@RequestMapping("/v1/config")
public class CountryOperatorController {

    /**
     * Obtiene todos los países soportados
     */
    @GetMapping("/countries")
    public ResponseEntity<Map<String, Object>> getSupportedCountries() {
        Map<String, Object> response = new HashMap<>();
        response.put("countries", CountryOperatorConstants.SUPPORTED_COUNTRIES);
        response.put("total", CountryOperatorConstants.SUPPORTED_COUNTRIES.size());
        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene todos los operadores por país
     */
    @GetMapping("/operators")
    public ResponseEntity<Map<String, Object>> getAllOperators() {
        Map<String, Object> response = new HashMap<>();
        response.put("operators_by_country", CountryOperatorConstants.getAllOperators());
        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene los operadores de un país específico
     */
    @GetMapping("/operators/{country}")
    public ResponseEntity<Map<String, Object>> getOperatorsByCountry(@PathVariable String country) {
        Map<String, Object> response = new HashMap<>();
        
        if (!CountryOperatorConstants.isValidCountry(country)) {
            response.put("error", "País no soportado: " + country);
            response.put("supported_countries", CountryOperatorConstants.SUPPORTED_COUNTRIES);
            return ResponseEntity.badRequest().body(response);
        }
        
        List<String> operators = CountryOperatorConstants.getOperatorsForCountry(country);
        response.put("country", country.toUpperCase());
        response.put("operators", operators);
        response.put("total", operators.size());
        
        return ResponseEntity.ok(response);
    }

    /**
     * Valida una combinación país-operador
     */
    @GetMapping("/validate/{country}/{operator}")
    public ResponseEntity<Map<String, Object>> validateCountryOperator(
            @PathVariable String country, 
            @PathVariable String operator) {
        
        Map<String, Object> response = new HashMap<>();
        
        boolean isValid = CountryOperatorConstants.isValidOperator(country, operator);
        
        response.put("country", country.toUpperCase());
        response.put("operator", operator);
        response.put("is_valid", isValid);
        
        if (!isValid) {
            response.put("valid_operators", CountryOperatorConstants.getOperatorsForCountry(country));
        }
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Información específica para Costa Rica - Kolbi
     */
    @GetMapping("/costa-rica-kolbi")
    public ResponseEntity<Map<String, Object>> getCostaRicaKolbiInfo() {
        Map<String, Object> response = new HashMap<>();
        
        response.put("country_code", "CR");
        response.put("country_name", "Costa Rica");
        response.put("operator", "Kolbi");
        response.put("short_tracking_enabled", true);
        response.put("description", "Configuración para tracking corto en Costa Rica - Kolbi");
        Map<String, String> exampleCustomer = new HashMap<>();
        exampleCustomer.put("name", "Customer Costa Rica");
        exampleCustomer.put("country", "CR");
        exampleCustomer.put("operator", "Kolbi");
        response.put("example_customer", exampleCustomer);
        
        Map<String, String> exampleProduct = new HashMap<>();
        exampleProduct.put("name", "SMS Premium CR");
        exampleProduct.put("country", "CR");
        exampleProduct.put("operator", "Kolbi");
        response.put("example_product", exampleProduct);
        
        return ResponseEntity.ok(response);
    }
}
