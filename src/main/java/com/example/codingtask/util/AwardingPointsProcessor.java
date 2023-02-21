package com.example.codingtask.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class AwardingPointsProcessor {
    public static Double process(BigDecimal value, BigDecimal threshold, Double multiplier) {
        BigDecimal buffer = value.subtract(threshold).setScale(0, RoundingMode.DOWN);
        if (buffer.signum() < 1) {
            return (double) 0;
        }
        return buffer.multiply(BigDecimal.valueOf(multiplier)).doubleValue();
    }
}
