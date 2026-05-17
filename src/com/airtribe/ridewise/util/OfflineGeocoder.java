package com.airtribe.ridewise.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class OfflineGeocoder {
    private static final Map<String, double[]> LOCALITY_MAP = new HashMap<>();

    static {
        // Bangalore major localities with real coordinates
        LOCALITY_MAP.put("Indiranagar", new double[]{12.9784, 77.6408});
        LOCALITY_MAP.put("Koramangala", new double[]{12.9279, 77.6233});
        LOCALITY_MAP.put("MG Road", new double[]{12.9756, 77.6065});
        LOCALITY_MAP.put("Whitefield", new double[]{12.9698, 77.7499});
        LOCALITY_MAP.put("Jayanagar", new double[]{12.9309, 77.5802});
        LOCALITY_MAP.put("Bangalore Fort", new double[]{12.9724, 77.6056});
        LOCALITY_MAP.put("Brigade Road", new double[]{12.9705, 77.6098});
        LOCALITY_MAP.put("Vijayanagar", new double[]{12.9352, 77.5783});
        LOCALITY_MAP.put("Marathahalli", new double[]{12.9716, 77.7064});
        LOCALITY_MAP.put("Hebbal", new double[]{13.0012, 77.5920});
        LOCALITY_MAP.put("Electronic City", new double[]{12.8395, 77.6789});
        LOCALITY_MAP.put("Bellandur", new double[]{12.9333, 77.7333});
        LOCALITY_MAP.put("Sarjapur", new double[]{12.9101, 77.7764});
        LOCALITY_MAP.put("Varthur", new double[]{12.9458, 77.7468});
        LOCALITY_MAP.put("Yelahanka", new double[]{13.0835, 77.5993});
        LOCALITY_MAP.put("Yeshwantpur", new double[]{13.0060, 77.5737});
        LOCALITY_MAP.put("Bannerghatta", new double[]{12.8675, 77.5933});
        LOCALITY_MAP.put("Silk Board", new double[]{12.9402, 77.6245});
        LOCALITY_MAP.put("Domlur", new double[]{12.9759, 77.6327});
        LOCALITY_MAP.put("Indiranagar East", new double[]{12.9850, 77.6480});
    }

    public static double[] geocode(String locality) {
        if (locality == null) {
            return null;
        }
        return LOCALITY_MAP.get(locality.trim());
    }

    public static Set<String> getAllLocalities() {
        return LOCALITY_MAP.keySet();
    }

    public static boolean isValidLocality(String locality) {
        return locality != null && LOCALITY_MAP.containsKey(locality.trim());
    }
}
