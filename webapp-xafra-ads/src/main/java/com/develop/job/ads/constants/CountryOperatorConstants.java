package com.develop.job.ads.constants;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Constantes para países y operadores soportados
 */
public class CountryOperatorConstants {

    // Países soportados (ISO 3166-1 alpha-2)
    public static final String PERU = "PE";
    public static final String COLOMBIA = "CO";
    public static final String CHILE = "CL";
    public static final String MEXICO = "MX";
    public static final String ARGENTINA = "AR";
    public static final String ECUADOR = "EC";
    public static final String BRASIL = "BR";
    public static final String COSTA_RICA = "CR";

    public static final List<String> SUPPORTED_COUNTRIES = Arrays.asList(
        PERU, COLOMBIA, CHILE, MEXICO, ARGENTINA, ECUADOR, BRASIL, COSTA_RICA
    );

    // Operadores por país
    private static final Map<String, List<String>> OPERATORS_BY_COUNTRY = new HashMap<>();
    
    static {
        // Perú
        OPERATORS_BY_COUNTRY.put(PERU, Arrays.asList(
            "Movistar", "Claro", "Entel", "Bitel"
        ));
        
        // Colombia
        OPERATORS_BY_COUNTRY.put(COLOMBIA, Arrays.asList(
            "Claro", "Movistar", "Tigo", "Avantel"
        ));
        
        // Chile
        OPERATORS_BY_COUNTRY.put(CHILE, Arrays.asList(
            "Movistar", "Entel", "Claro", "WOM"
        ));
        
        // México
        OPERATORS_BY_COUNTRY.put(MEXICO, Arrays.asList(
            "Telcel", "AT&T", "Movistar", "Unefon"
        ));
        
        // Argentina
        OPERATORS_BY_COUNTRY.put(ARGENTINA, Arrays.asList(
            "Claro", "Movistar", "Personal", "Tuenti"
        ));
        
        // Ecuador
        OPERATORS_BY_COUNTRY.put(ECUADOR, Arrays.asList(
            "Claro", "Movistar", "CNT", "Tuenti"
        ));
        
        // Brasil
        OPERATORS_BY_COUNTRY.put(BRASIL, Arrays.asList(
            "Vivo", "TIM", "Claro", "Oi"
        ));
        
        // Costa Rica
        OPERATORS_BY_COUNTRY.put(COSTA_RICA, Arrays.asList(
            "Kolbi", "Movistar", "Claro"
        ));
    }

    /**
     * Valida si un país es soportado
     */
    public static boolean isValidCountry(String country) {
        return country != null && SUPPORTED_COUNTRIES.contains(country.toUpperCase());
    }

    /**
     * Valida si un operador es válido para un país específico
     */
    public static boolean isValidOperator(String country, String operator) {
        if (country == null || operator == null) {
            return false;
        }
        
        List<String> operators = OPERATORS_BY_COUNTRY.get(country.toUpperCase());
        return operators != null && operators.contains(operator);
    }

    /**
     * Obtiene la lista de operadores para un país
     */
    public static List<String> getOperatorsForCountry(String country) {
        if (country == null) {
            return null;
        }
        return OPERATORS_BY_COUNTRY.get(country.toUpperCase());
    }

    /**
     * Obtiene todos los operadores disponibles
     */
    public static Map<String, List<String>> getAllOperators() {
        return new HashMap<>(OPERATORS_BY_COUNTRY);
    }
}
