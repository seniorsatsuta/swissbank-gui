package by.iba.bank.model.entity;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter

public class Transaction {
    private Integer id;

    private Account account;

    private BigDecimal amount;

    private String date;

    private String time;

    private String transactionType;


    private String comment;

}