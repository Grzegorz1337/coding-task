package com.example.codingtask.util;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class AwardingPointsResource {
    private final static Map<BigDecimal, Double> thresholdsMap;

    static {
        thresholdsMap = new HashMap<>();
        thresholdsMap.put(BigDecimal.valueOf(50), 1.0);
        thresholdsMap.put(BigDecimal.valueOf(100), 2.0);
    }

    public static Map<BigDecimal, Double> getThresholdsWithMultipliers() {
        return thresholdsMap;
    }
}