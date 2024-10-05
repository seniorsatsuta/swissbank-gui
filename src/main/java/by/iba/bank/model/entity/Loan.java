package by.iba.bank.model.entity;

import lombok.Getter;
import lombok.Setter;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter

public class Loan {
    private Integer id;

    private Account account;

    private BigDecimal amount;

    private Integer term;

    private BigDecimal interestRate;

    private BigDecimal monthlyPayment;

    private String issueDate;

    private String status;

  //  private List<LoanInterestRate> loanInterestRates = new ArrayList<>();
}