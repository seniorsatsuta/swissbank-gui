package by.iba.bank.component;

import by.iba.bank.model.entity.Account;
import by.iba.bank.model.entity.Loan;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import lombok.Getter;

import java.io.IOException;

@Getter
public class CreditAccountComponent extends Pane {

    @FXML
    private Label balance;

    @FXML
    private Label currency;

    @FXML
    private Label lName;

    @FXML
    private Color color;

    @FXML
    private ImageView clock;

    private Loan loan;



    public CreditAccountComponent(){
        FXMLLoader loader =  new FXMLLoader(getClass().getResource("/by/iba/bank/crcart.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try{
            loader.load();
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
        currency.setText(loan.getAccount().getCurrency());
        lName.setText(loan.getAccount().getName());

        if(loan.getStatus().equalsIgnoreCase("wait"))
            clock.setVisible(true);
        else
        {
            clock.setVisible(false);
            balance.setText(loan.getAmount().toString());
        }

    }
}
