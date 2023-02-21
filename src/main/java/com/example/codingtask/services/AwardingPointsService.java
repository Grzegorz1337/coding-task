package com.example.codingtask.services;


import com.example.codingtask.dtos.MoneyDto;
import com.example.codingtask.util.AwardingPointsProcessor;
import com.example.codingtask.util.AwardingPointsResource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

@Service
public class AwardingPointsService {

    private static final Map<BigDecimal, Double> awardPointsResource = AwardingPointsResource.getThresholdsWithMultipliers();

    public Double calculateAwardPoints(MoneyDto moneyDto) {
        SortedSet<BigDecimal> sortedThresholds = getSortedThresholdKeysForAwardPoints();
        BigDecimal moneyAmount = moneyDto.getAmount();
        double awardPoints = 0;

        for (BigDecimal threshold : sortedThresholds) {
            Double multiplier = awardPointsResource.get(threshold);
            awardPoints += AwardingPointsProcessor.process(moneyAmount, threshold, multiplier);
            moneyAmount = moneyAmount.min(threshold);
        }

        return awardPoints;
    }

    private SortedSet<BigDecimal> getSortedThresholdKeysForAwardPoints() {
        SortedSet<BigDecimal> keys = new TreeSet<>(Collections.reverseOrder());
        keys.addAll(awardPointsResource.keySet());
        return keys;
    }
}
