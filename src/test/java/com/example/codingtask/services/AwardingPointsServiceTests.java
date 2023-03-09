package com.example.codingtask.services;


import com.example.codingtask.dtos.MoneyDto;
import com.example.codingtask.repositories.TransactionRepository;
import com.example.codingtask.util.AwardingPointsResource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class AwardingPointsServiceTests {

    @Mock
    TransactionRepository transactionRepository;

    AwardingPointsService awardingPointsService = new AwardingPointsService(transactionRepository);
    private static final Map<BigDecimal, Double> awardPointsResource = AwardingPointsResource.getThresholdsWithMultipliers();
    private final SortedSet<BigDecimal> sortedThresholds = new TreeSet<>(awardPointsResource.keySet());


    @Test
    @DisplayName("Should return zero for money amount lower than lowest threshold")
    void testAwardPointsCalculatedBelowFirstThreshold() {
        // given
        MoneyDto moneyDto = new MoneyDto();
        moneyDto.setAmount(BigDecimal.valueOf(1));
        assertEquals(sortedThresholds.first().compareTo(moneyDto.getAmount()),1);

        // when
        Double awardPoints = awardingPointsService.calculateAwardPoints(moneyDto);

        // then
        assertEquals(awardPoints, Double.valueOf(0));
    }

    @Test
    @DisplayName("Should return zero for money amount exactly at first threshold")
    void testAwardPointsCalculatedAtFirstThreshold() {
        // given
        MoneyDto moneyDto = new MoneyDto();
        moneyDto.setAmount(sortedThresholds.first());

        // when
        Double awardPoints = awardingPointsService.calculateAwardPoints(moneyDto);

        //then
        assertEquals(awardPoints, Double.valueOf(0));
    }

    @Test
    @DisplayName("Should return subtraction between amount and lower threshold by given multiplier")
    void testAwardPointsCalculatedBetweenThresholds() {
        // given
        MoneyDto moneyDto = new MoneyDto();
        moneyDto.setAmount(BigDecimal.valueOf(75));
        Iterator<BigDecimal> thresholdsIterator = sortedThresholds.iterator();
        BigDecimal firstThreshold = thresholdsIterator.next();
        BigDecimal secondThreshold = thresholdsIterator.next();
        Double currentMultiplier = awardPointsResource.get(firstThreshold);

        assertEquals(1, moneyDto.getAmount().compareTo(firstThreshold));
        assertEquals(-1, moneyDto.getAmount().compareTo(secondThreshold));

        // when
        Double awardPoints = awardingPointsService.calculateAwardPoints(moneyDto);
        Double expectedValue = moneyDto.getAmount().subtract(firstThreshold).doubleValue() * currentMultiplier;

        // then
        assertEquals(awardPoints, expectedValue);
    }

    @Test
    @DisplayName("Should return subtraction between amount and lower threshold by given multiplier")
    void testAwardPointsCalculatedAtSecondThreshold() {
        // given
        Iterator<BigDecimal> thresholdsIterator = sortedThresholds.iterator();
        BigDecimal firstThreshold = thresholdsIterator.next();
        BigDecimal secondThreshold = thresholdsIterator.next();
        MoneyDto moneyDto = new MoneyDto();
        moneyDto.setAmount(secondThreshold);
        Double currentMultiplier = awardPointsResource.get(firstThreshold);

        // when
        Double awardPoints = awardingPointsService.calculateAwardPoints(moneyDto);
        Double expectedValue = moneyDto.getAmount().subtract(firstThreshold).doubleValue() * currentMultiplier;

        // then
        assertEquals(awardPoints, expectedValue);
    }

    @Test
    @DisplayName("Should return a valid number for amount above second threshold")
    void testAwardPointsCalculatedAboveSecondThreshold() {
        // given
        Iterator<BigDecimal> thresholdsIterator = sortedThresholds.iterator();
        BigDecimal firstThreshold = thresholdsIterator.next();
        BigDecimal secondThreshold = thresholdsIterator.next();
        MoneyDto moneyDto = new MoneyDto();
        moneyDto.setAmount(secondThreshold.add(BigDecimal.valueOf(100)));
        Double firstMultiplier = awardPointsResource.get(firstThreshold);
        Double secondMultiplier = awardPointsResource.get(secondThreshold);

        // when
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

        // then
        assertEquals(expectedValue, awardPoints);
    }

}
