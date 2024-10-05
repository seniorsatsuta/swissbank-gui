package by.iba.bank.model.entity;

import lombok.Getter;
import lombok.Setter;


import java.math.BigDecimal;

@Getter
@Setter

public class Deposit {
    private Integer id;

     private Account account;

    private BigDecimal amount;

     private Integer term;

    private BigDecimal interestRate;

    private String openDate;

   // private BigDecimal effectiveInterestRate;

}