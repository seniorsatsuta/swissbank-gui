package by.iba.bank.model.entity;

import lombok.Getter;
import lombok.Setter;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter

public class Client {
    private Integer id;

    private String firstName;

    private String lastName;

    private String address;

    private String phone;

    private String email;

 /*   private Set<Account> accounts = new LinkedHashSet<>();

    private Set<User> bankUsers = new LinkedHashSet<>();
*/
}