package com.example.codingtask.dtos;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Validated
public class MoneyDto {
    @Positive(message = "Money amount must be a positive number")
    private BigDecimal amount;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
