package by.iba.bank.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor

public class Role {
    private Integer id;

    private String role;

    private User usersUser;

}