package com.example.codingtask.controllers;


import com.example.codingtask.dtos.MoneyDto;
import com.example.codingtask.services.AwardingPointsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AwardingPointsController.class)
public class AwardingPointsControllerTests {
    @Autowired
    private MockMvc mvc;

    @MockBean
    AwardingPointsService awardingPointsService;

    @Test
    @DisplayName("Should return bad request in case of invalid amount")
    void Should_ReturnBadRequest_When_AmountIsNotPositive() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/award-points")
                        .content("{ \"amount\":-100 }").contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(
                        result -> assertTrue(
                                result.getResolvedException() instanceof MethodArgumentNotValidException
                        )
                );
    }

    @Test
    @DisplayName("Should return bad request in case of non-decimal amount")
    void Should_ReturnBadRequest_When_AmountIsNonDecimal() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/award-points")
                        .content("{ \"amount\":\"foo\" }").contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(
                        result -> assertTrue(
                                result.getResolvedException() instanceof HttpMessageNotReadableException
                        )
                );
    }

    @Test
    @DisplayName("Should call service method to calculate award points in case of positive money amount")
    void Should_CallAwardingPointsService_When_AmountIsPositive() throws Exception {
        MoneyDto moneyDto = new MoneyDto();
        moneyDto.setAmount(BigDecimal.valueOf(10));
        Double expectedValue = (double) 0;
        given(awardingPointsService.calculateAwardPoints(moneyDto)).willReturn(expectedValue);

        MockHttpServletResponse result = mvc.perform(MockMvcRequestBuilders.get("/award-points")
                .content("{ \"amount\":10 }").contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        assertEquals(result.getContentAsString(), String.valueOf(expectedValue));
    }

}
