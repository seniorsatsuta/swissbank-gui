package by.iba.bank.model.entity;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter

public class Account {
    private Integer id;

    private String accountType;

    private BigDecimal balance;

    private String currency;

    private String openDate;

    private Client client;

    private String name;

  /*  private Set<Deposit> deposits = new LinkedHashSet<>();

    private Set<Transaction> transactions = new LinkedHashSet<>();

    private Set<Loan> loans = new LinkedHashSet<>();
*/
}