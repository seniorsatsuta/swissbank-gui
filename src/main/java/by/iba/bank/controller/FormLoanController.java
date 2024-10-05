package by.iba.bank.controller;

import by.iba.bank.model.entity.Account;
import by.iba.bank.model.entity.Loan;
import by.iba.bank.model.entity.LoanInterestRate;
import by.iba.bank.model.messages.Request;
import by.iba.bank.model.messages.RequestType;
import by.iba.bank.model.messages.Response;
import by.iba.bank.model.messages.ResponseStatus;
import by.iba.bank.utility.ClientSocket;
import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class FormLoanController {
    @FXML
    public DatePicker myDatePicker;
    @FXML
    private Button cancel;
    @FXML
    private Button ok;

    @FXML
    private TextField tfAmount;

    @FXML
    private TextField tfTerm;

    @FXML
    public void initialize() {
        TextFormatter<Integer> textFormatter = new TextFormatter<>(new IntegerStringConverter(), null,
                c -> {
                    if (c.getControlNewText().isEmpty() || c.getControlNewText().matches("^[1-9][0-9]*$")) {
                        return c;
                    }


                    return null;
                });

        TextFormatter<Integer> textFormatterTerm = new TextFormatter<>(new IntegerStringConverter(), null,
                c -> {
                    if (c.getControlNewText().isEmpty() || c.getControlNewText().matches("^[1-9][0-9]{0,1}$|50$")) {
                        return c;
                    }

                    return null;
                });
        tfAmount.setTextFormatter(textFormatter);
        tfTerm.setTextFormatter(textFormatterTerm);
    }


    @FXML
    void returnBack(ActionEvent event) throws IOException {
        Stage stage = (Stage) cancel.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/by/iba/bank/crud-accounts.fxml"));
        stage.setScene(new Scene(root));

    }

    @FXML
    void sendRequest(ActionEvent event) throws IOException{
        Loan loan = new Loan();
        loan.setAccount(new Account());
        loan.getAccount().setAccountType("CREDIT");
        loan.getAccount().setName("CREDIT");
        loan.getAccount().setBalance(BigDecimal.valueOf(0));
        loan.getAccount().setCurrency("BYN");
        loan.getAccount().setClient(ClientSocket.getInstance().getUser().getClient());
        //loan.setLoanInterestRates(new ArrayList<>());

        loan.setAmount(new BigDecimal(tfAmount.getText()));
        loan.setTerm(Integer.parseInt(tfTerm.getText()));
        loan.setIssueDate(myDatePicker.getValue().format(DateTimeFormatter.ISO_LOCAL_DATE));


        Request request = new Request();
        request.setRequestMessage(new Gson().toJson(loan));
        request.setRequestType(RequestType.POST_LOAN_STATEMENT);

        ClientSocket.getInstance().getOut().println(new Gson().toJson(request));
        ClientSocket.getInstance().getOut().flush();



        Stage stage = (Stage) ok.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/by/iba/bank/form-loan-scene-2.fxml"));
        stage.setScene(new Scene(root));
    }

    public Callback<DatePicker, DateCell> getDayCellFactory() {
        return new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);


                        if (item.isBefore(LocalDate.now())) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                    }
                };
            }
        };
    }

}
