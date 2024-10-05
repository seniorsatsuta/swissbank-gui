package by.iba.bank.component;

import by.iba.bank.model.entity.Account;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

@Getter
public class AccountComponent extends Pane {

    @FXML
    private Label balance;

    @FXML
    private Label currency;

    @FXML
    private Label lName;

    @FXML
    private Color color;

    private Account account;



    public AccountComponent(){
        FXMLLoader loader =  new FXMLLoader(getClass().getResource("/by/iba/bank/cart.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try{
            loader.load();
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    public void setAccount(Account account) {
        this.account = account;
        balance.setText(account.getBalance().toString());
        currency.setText(account.getCurrency());
        lName.setText(account.getName());
    }
}
