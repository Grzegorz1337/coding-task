package com.example.codingtask.services;


import com.example.codingtask.dtos.MoneyDto;
import com.example.codingtask.util.AwardingPointsResource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class AwardingPointsServiceTests {

    @Autowired
    AwardingPointsService awardingPointsService;
    private static final Map<BigDecimal, Double> awardPointsResource = AwardingPointsResource.getThresholdsWithMultipliers();
    private final SortedSet<BigDecimal> sortedThresholds = new TreeSet<>(awardPointsResource.keySet());


    @Test
    @DisplayName("Should return zero for money amount lower than lowest threshold")
    void testAwardPointsCalculatedBelowFirstThreshold() {
        MoneyDto moneyDto = new MoneyDto();
        moneyDto.setAmount(BigDecimal.valueOf(1));
        assertEquals(sortedThresholds.first().compareTo(moneyDto.getAmount()), 1);

        Double awardPoints = awardingPointsService.calculateAwardPoints(moneyDto);

        assertEquals(awardPoints, Double.valueOf(0));
    }

    @Test
    @DisplayName("Should return zero for money amount exactly at first threshold")
    void testAwardPointsCalculatedAtFirstThreshold() {
        MoneyDto moneyDto = new MoneyDto();
        moneyDto.setAmount(sortedThresholds.first());

        Double awardPoints = awardingPointsService.calculateAwardPoints(moneyDto);

        assertEquals(awardPoints, Double.valueOf(0));
    }

    @Test
    @DisplayName("Should return subtraction between amount and lower threshold by given multiplier")
    void testAwardPointsCalculatedBetweenThresholds() {
        MoneyDto moneyDto = new MoneyDto();
        moneyDto.setAmount(BigDecimal.valueOf(75));
        Iterator<BigDecimal> thresholdsIterator = sortedThresholds.iterator();
        BigDecimal firstThreshold = thresholdsIterator.next();
        BigDecimal secondThreshold = thresholdsIterator.next();
        Double currentMultiplier = awardPointsResource.get(firstThreshold);

        assertEquals(1, moneyDto.getAmount().compareTo(firstThreshold));
        assertEquals(-1, moneyDto.getAmount().compareTo(secondThreshold));

        Double awardPoints = awardingPointsService.calculateAwardPoints(moneyDto);
        Double expectedValue = moneyDto.getAmount().subtract(firstThreshold).doubleValue() * currentMultiplier;

        assertEquals(awardPoints, expectedValue);
    }

    @Test
    @DisplayName("Should return subtraction between amount and lower threshold by given multiplier")
    void testAwardPointsCalculatedAtSecondThreshold() {
        Iterator<BigDecimal> thresholdsIterator = sortedThresholds.iterator();
        BigDecimal firstThreshold = thresholdsIterator.next();
        BigDecimal secondThreshold = thresholdsIterator.next();
        MoneyDto moneyDto = new MoneyDto();
        moneyDto.setAmount(secondThreshold);
        Double currentMultiplier = awardPointsResource.get(firstThreshold);

        Double awardPoints = awardingPointsService.calculateAwardPoints(moneyDto);
        Double expectedValue = moneyDto.getAmount().subtract(firstThreshold).doubleValue() * currentMultiplier;

        assertEquals(awardPoints, expectedValue);
    }

    @Test
    @DisplayName("Should return a valid number for amount above second threshold")
    void testAwardPointsCalculatedAboveSecondThreshold() {
        Iterator<BigDecimal> thresholdsIterator = sortedThresholds.iterator();
        BigDecimal firstThreshold = thresholdsIterator.next();
        BigDecimal secondThreshold = thresholdsIterator.next();
        MoneyDto moneyDto = new MoneyDto();
        moneyDto.setAmount(secondThreshold.add(BigDecimal.valueOf(100)));
        Double firstMultiplier = awardPointsResource.get(firstThreshold);
        Double secondMultiplier = awardPointsResource.get(secondThreshold);

        Double awardPoints = awardingPointsService.calculateAwardPoints(moneyDto);
        Double expectedFirstThresholdPoints = secondThreshold
                .subtract(firstThreshold)
                .doubleValue()
                * firstMultiplier;
        Double expectedSecondThresholdPoints = moneyDto.getAmount()
                .subtract(secondThreshold)
                .doubleValue()
                * secondMultiplier;
        Double expectedValue = expectedFirstThresholdPoints + expectedSecondThresholdPoints;

        assertEquals(expectedValue, awardPoints);
    }

}
