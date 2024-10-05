package by.iba.bank.model.entity;

import lombok.Getter;
import lombok.Setter;


import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
public class User {
    private Integer id;

    private String username;

    private byte[] password;

    private Client client;

    private String role;

    //private Set<Role> roles = new LinkedHashSet<>();

}