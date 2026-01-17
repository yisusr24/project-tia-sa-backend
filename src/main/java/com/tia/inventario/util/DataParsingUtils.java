package com.tia.inventario.util;

import java.math.BigDecimal;

public class DataParsingUtils {
    
    private DataParsingUtils() {
        throw new IllegalStateException("Utility class");
    }
    
    public static Long parseLong(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Long parseLongWithDefault(String value, Long defaultValue) {
        Long parsed = parseLong(value);
        return parsed != null ? parsed : defaultValue;
    }
    
    public static BigDecimal parseBigDecimal(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return new BigDecimal(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    public static Integer parseInteger(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    public static Boolean parseBoolean(String value) {
        if (value == null || value.trim().isEmpty()) {
            return true;
        }
        String v = value.trim().toLowerCase();
        return v.equals("true") || v.equals("1") || v.equals("sí") || 
               v.equals("si") || v.equals("yes") || v.equals("y");
    }
    
    public static String formatFileSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "";
        return String.format("%.1f %sB", bytes / Math.pow(1024, exp), pre);
    }
}
