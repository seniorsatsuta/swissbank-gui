package by.iba.bank.model.entity;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class LoanInterestRate {
    private Integer id;
    private String typeOfCount;
    private BigDecimal effectiveInterestRate;
    private Loan loan;

}