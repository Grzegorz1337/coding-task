package com.example.codingtask.models;


import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


@Entity
@Table
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique=true, nullable = false)
    private Integer id;

    @Column
    private BigDecimal moneySpent;

    @Column
    private Double awardPoints;

    @Column
    private Date transactionDate;

    public BigDecimal getMoneySpent() {
        return moneySpent;
    }

    public void setMoneySpent(BigDecimal moneySpent) {
        this.moneySpent = moneySpent;
    }

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
}
