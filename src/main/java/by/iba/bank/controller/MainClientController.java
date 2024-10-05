package by.iba.bank.controller;

import by.iba.bank.component.AccountComponent;
import by.iba.bank.component.CreditAccountComponent;
import by.iba.bank.model.entity.Account;

import by.iba.bank.model.entity.Loan;
import by.iba.bank.model.messages.Request;
import by.iba.bank.model.messages.RequestType;
import by.iba.bank.model.messages.Response;
import by.iba.bank.model.messages.ResponseStatus;
import by.iba.bank.utility.ClientSocket;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;

import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class MainClientController implements Initializable {
    @FXML
    private Button btnAddAccountAccumalate;

    @FXML
    private Button btnAddLoanAccount;

    @FXML
    private Button btnConverter;

    @FXML
    private Button btnExit;

    @FXML
    private Button btnLoans;

    @FXML
    private Button btnPayment;

    @FXML
    private Button btnTransaction;

    @FXML
    private ScrollPane credit_accounts;

    @FXML
    private ScrollPane cumulative_accounts;

    @FXML
    private HBox hBoxAcAccounts;

    @FXML
    private HBox hBoxCreditAcc;

    @FXML
    private Label labelAboutUsers;



    @FXML
    void addAccountAccumalate(ActionEvent event) throws IOException {
        Stage stage = (Stage) btnExit.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/by/iba/bank/data-account.fxml"));
        stage.setScene(new Scene(root));

    }

    @FXML
    void addLoan(ActionEvent event) throws IOException{
        Stage stage = (Stage) btnExit.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/by/iba/bank/form-loan-scene.fxml"));
        stage.setScene(new Scene(root));
    }

    @FXML
    void transferControlToLoginWindow(ActionEvent event) throws IOException{
        Stage stage = (Stage) btnExit.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/by/iba/bank/login-scene.fxml"));
        stage.setScene(new Scene(root));
    }

    @FXML
    void transferControlToMenuConverter(ActionEvent event) throws IOException{
        Stage stage = (Stage) btnExit.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/by/iba/bank/converter-scene.fxml"));
        stage.setScene(new Scene(root));
    }

    @FXML
    void transferControlToMenuLoans(ActionEvent event) throws IOException{
        Stage stage = (Stage) btnExit.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/by/iba/bank/credit-statements-client.fxml"));
        stage.setScene(new Scene(root));
    }

    @FXML
    void transferControlToMenuPayment(ActionEvent event) throws IOException{
        Stage stage = (Stage) btnExit.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/by/iba/bank/payment-scene.fxml"));
        stage.setScene(new Scene(root));
    }

    @FXML
    void transferControlToMenuTransaction(ActionEvent event) throws IOException{
        Stage stage = (Stage) btnExit.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/by/iba/bank/history-payment.fxml"));
        stage.setScene(new Scene(root));
    }

    public static List<Account> getAllAccounts(){
        Request request = new Request();
        request.setRequestType(RequestType.GET_ALL_ACCOUNTS_OF_CLIENT);
        request.setRequestMessage(new Gson().toJson(ClientSocket.getInstance().getUser().getClient()));

        ClientSocket.getInstance().getOut().println(new Gson().toJson(request));

        ClientSocket.getInstance().getOut().flush();


        try {
            String answer = ClientSocket.getInstance().getInStream().readLine();
            Response response = new Gson().fromJson(answer, Response.class);

            if (response.getResponseStatus() == ResponseStatus.OK) {
                TypeToken<List<Account>> token = new TypeToken<>() {};
                return new Gson().fromJson(response.getResponseData(), token.getType());

            } else {

            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return new ArrayList<>();
    };

    public List<Loan> getAllLoans(){
        Request request = new Request();
        request.setRequestType(RequestType.GET_ALL_LOANS_OF_CLIENT);
        request.setRequestMessage(new Gson().toJson(ClientSocket.getInstance().getUser().getClient()));

        ClientSocket.getInstance().getOut().println(new Gson().toJson(request));

        ClientSocket.getInstance().getOut().flush();


        try {
            String answer = ClientSocket.getInstance().getInStream().readLine();
            Response response = new Gson().fromJson(answer, Response.class);

            if (response.getResponseStatus() == ResponseStatus.OK) {
                TypeToken<List<Loan>> token = new TypeToken<>() {};
                return new Gson().fromJson(response.getResponseData(), token.getType());

            } else {

            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public void createCartsOfAccounts(){

        for(Account account : getAllAccounts()){
            if(account.getAccountType().equalsIgnoreCase("accumalate")){
                AccountComponent accountComponent = new AccountComponent();
                accountComponent.setAccount(account);
                hBoxAcAccounts.getChildren().add(accountComponent);
            }
        }

        for(Loan loan : getAllLoans()){
           if(loan.getStatus().equalsIgnoreCase("wait")){
                CreditAccountComponent creditAccountComponent = new CreditAccountComponent();
                creditAccountComponent.setLoan(loan);
                hBoxCreditAcc.getChildren().add(creditAccountComponent);
          }else if(loan.getStatus().equalsIgnoreCase("ok")){
               CreditAccountComponent creditAccountComponent = new CreditAccountComponent();
               creditAccountComponent.setLoan(loan);
               hBoxCreditAcc.getChildren().add(creditAccountComponent);
           }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createCartsOfAccounts();
    }
}
