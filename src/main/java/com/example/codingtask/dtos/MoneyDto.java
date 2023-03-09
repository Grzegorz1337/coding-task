package com.example.codingtask.dtos;

import com.example.codingtask.models.Transaction;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Date;

@Validated
public class MoneyDto {
    @Positive(message = "Money amount must be a positive number")
    private BigDecimal amount;
    private Double awardPoints;
    private Date transactionDate;

    public Double getAwardPoints() {
        return awardPoints;
    }

    public void setAwardPoints(Double awardPoints) {
        this.awardPoints = awardPoints;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Transaction toTransaction(){
        Transaction transaction = new Transaction();
        transaction.setMoneySpent(this.amount);
        transaction.setAwardPoints(this.awardPoints);
        transaction.setTransactionDate(this.transactionDate);
        return transaction;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
