package by.iba.bank.controller;

import by.iba.bank.component.AccountComponent;
import by.iba.bank.model.entity.Account;
import by.iba.bank.model.entity.User;
import by.iba.bank.model.messages.Request;
import by.iba.bank.model.messages.RequestType;
import by.iba.bank.model.messages.Response;
import by.iba.bank.model.messages.ResponseStatus;
import by.iba.bank.utility.ClientSocket;
import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class OpenAccountController implements Initializable {

    @FXML
    private Button btnConverter;

    @FXML
    private Button btnExit;

    @FXML
    private Button btnLoans;

    @FXML
    private Button btnOpenAccount;

    @FXML
    private Button btnPayment;

    @FXML
    private Button btnTransaction;

    @FXML
    private ComboBox<String> comboCurrency;

    @FXML
    private Pane forCard;

    @FXML
    private Label labelAboutUsers;

    @FXML
    private TextField tfName;

    @FXML
    private  Label message;

    private Account account;
    AccountComponent accountComponent;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        message.setVisible(false);
        comboCurrency.getItems().addAll("BYN", "EUR", "USD");
        account = new Account();

        accountComponent = new AccountComponent();
        accountComponent.getBalance().setText("");
        accountComponent.getCurrency().setText("");
        forCard.getChildren().add(accountComponent);
    }

    @FXML
    void chooseCurrency(ActionEvent event) {
        accountComponent.getCurrency().setText(comboCurrency.getSelectionModel().getSelectedItem());
    }

    @FXML
    void createName(KeyEvent event) {
        accountComponent.getLName().setText(tfName.getText());
    }

    @FXML
    void openAccount(ActionEvent event) throws IOException {
        account.setCurrency(comboCurrency.getSelectionModel().getSelectedItem());
        account.setName(tfName.getText());
        account.setAccountType("ACCUMALATE");
        account.setBalance(BigDecimal.valueOf(0));
        account.setClient(ClientSocket.getInstance().getUser().getClient());
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(new Date());
        account.setOpenDate(date);

        Request request = new Request();
        request.setRequestMessage(new Gson().toJson(account));
        request.setRequestType(RequestType.POST_NEW_ACCOUNT);

        ClientSocket.getInstance().getOut().println(new Gson().toJson(request));
        ClientSocket.getInstance().getOut().flush();

        String answer = ClientSocket.getInstance().getInStream().readLine();
        Response response = new Gson().fromJson(answer, Response.class);

        Stage stage = (Stage) btnExit.getScene().getWindow();
        if (response.getResponseStatus() == ResponseStatus.OK) {
            Parent root = FXMLLoader.load(getClass().getResource("/by/iba/bank/crud-accounts.fxml"));
            stage.setScene(new Scene(root));
        } else {
            message.setVisible(true);
        }
    }

    @FXML
    void transferControlToLoginWindow(ActionEvent event) throws IOException{
        Stage stage = (Stage) btnExit.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/by/iba/bank/login-scene.fxml"));
        stage.setScene(new Scene(root));
    }

    @FXML
    void transferControlToMenuConverter(ActionEvent event) {

    }

    @FXML
    void transferControlToMenuLoans(ActionEvent event) {

    }

    @FXML
    void transferControlToMenuPayment(ActionEvent event) {

    }

    @FXML
    void transferControlToMenuTransaction(ActionEvent event) {

    }

}
