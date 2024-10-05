package by.iba.bank.controller;

import by.iba.bank.component.AccountComponent;
import by.iba.bank.model.entity.Account;
import by.iba.bank.model.entity.Transaction;
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
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.converter.BigDecimalStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.BiFunction;

import static by.iba.bank.controller.MainClientController.getAllAccounts;

public class PaymentController implements Initializable {

    @FXML
    public TextField tfAmountTranslate;
    @FXML
    public Label message;
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
    private Pane cardRecevier;

    @FXML
    private Pane cardSender;

    @FXML
    private Pane cart;

    @FXML
    private ComboBox<Integer> cbAccount;

    @FXML
    private ComboBox<Integer> cbAccountReceiver;

    @FXML
    private ComboBox<Integer> cmAccountSender;

    @FXML
    private Pane infoPayment;

    @FXML
    private Pane infoTransalte;

    @FXML
    private Label labelAboutUsers;

    @FXML
    private TextField tfAmount;

    private Account selectedAccount;
    private Account receiverAccount;
    private List<Account> accountList;

    @FXML
    void pay(ActionEvent event) throws IOException{
        if(infoTransalte.isVisible()){
            infoPayment.setVisible(true);
            infoTransalte.setVisible(false);
            return;
        }else{
            BigDecimalStringConverter bigDecimalStringConverter = new BigDecimalStringConverter();
            BigDecimal bigDecimalAmount = bigDecimalStringConverter.fromString(tfAmount.getText());
            if(checkAmountOfOperation(bigDecimalAmount, selectedAccount.getBalance())){
                Request request = new Request();
                request.setRequestMessage(new Gson().toJson(createTransaction("DEBIT", bigDecimalAmount)));
                request.setRequestType(RequestType.POST_NEW_TRANSACTION);

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
        }
    }

    private void transactionRequest(String purpose) throws IOException {



    }

    @FXML
    void replenish(ActionEvent event) throws IOException{
        if(infoTransalte.isVisible()){
            infoPayment.setVisible(true);
            infoTransalte.setVisible(false);
            return;
        }else{
            BigDecimalStringConverter bigDecimalStringConverter = new BigDecimalStringConverter();
            BigDecimal bigDecimalAmount = bigDecimalStringConverter.fromString(tfAmount.getText());

            Request request = new Request();
            request.setRequestMessage(new Gson().toJson(createTransaction("REPLENISH", bigDecimalAmount)));
            request.setRequestType(RequestType.POST_NEW_TRANSACTION);

            ClientSocket.getInstance().getOut().println(new Gson().toJson(request));
            ClientSocket.getInstance().getOut().flush();

            String answer = ClientSocket.getInstance().getInStream().readLine();
            Response response = new Gson().fromJson(answer, Response.class);

            Stage stage = (Stage) btnExit.getScene().getWindow();
            if (response.getResponseStatus() == ResponseStatus.OK) {
                Parent root = FXMLLoader.load(getClass().getResource("/by/iba/bank/crud-accounts.fxml"));
                stage.setScene(new Scene(root));
            } else {
                message.setText("ОШИБКА");
                message.setVisible(true);
            }
            }

    }

    @FXML
    void transferControlToMenuLoans(ActionEvent event) throws IOException{
        Stage stage = (Stage) btnExit.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/by/iba/bank/credit-statements-client.fxml"));
        stage.setScene(new Scene(root));
    }

    @FXML
    void transferControlToLoginWindow(ActionEvent event) throws IOException{
        Stage stage = (Stage) btnExit.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/by/iba/bank/login-scene.fxml"));
        stage.setScene(new Scene(root));
    }

    @FXML
    void transferControlToMenuAccounts(ActionEvent event) throws IOException{
        Stage stage = (Stage) btnExit.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/by/iba/bank/crud-accounts.fxml"));
        stage.setScene(new Scene(root));
    }

    @FXML
    void transferControlToMenuConverter(ActionEvent event) {

    }

    @FXML
    void transferControlToMenuPayment(ActionEvent event) {

    }

    @FXML
    void transferControlToMenuTransaction(ActionEvent event) {

    }

    @FXML
    void translate(ActionEvent event) throws IOException {//liuytd
        if(infoPayment.isVisible()){
            infoPayment.setVisible(false);
            infoTransalte.setVisible(true);
            return;
        }else{
            BigDecimalStringConverter bigDecimalStringConverter = new BigDecimalStringConverter();
            BigDecimal bigDecimalAmount = bigDecimalStringConverter.fromString(tfAmountTranslate.getText());

            Request request = new Request();
            request.setRequestMessage(new Gson().toJson(createTransaction("DEBIT", bigDecimalAmount)));
            request.setRequestType(RequestType.POST_NEW_TRANSACTION);

            ClientSocket.getInstance().getOut().println(new Gson().toJson(request));
            ClientSocket.getInstance().getOut().flush();

            String answer = ClientSocket.getInstance().getInStream().readLine();
            Response response = new Gson().fromJson(answer, Response.class);

            Transaction transaction = createTransaction("REPLENISH", bigDecimalAmount);
            transaction.setAccount(receiverAccount);

            request.setRequestMessage(new Gson().toJson(transaction));
            request.setRequestType(RequestType.POST_NEW_TRANSACTION);

            ClientSocket.getInstance().getOut().println(new Gson().toJson(request));
            ClientSocket.getInstance().getOut().flush();

            answer = ClientSocket.getInstance().getInStream().readLine();
            response = new Gson().fromJson(answer, Response.class);

            Stage stage = (Stage) btnExit.getScene().getWindow();
            if (response.getResponseStatus() == ResponseStatus.OK) {
                Parent root = FXMLLoader.load(getClass().getResource("/by/iba/bank/crud-accounts.fxml"));
                stage.setScene(new Scene(root));
            } else {
                message.setVisible(true);
            }
        }
    }


    public boolean checkAmountOfOperation(BigDecimal amount, BigDecimal balance){
        int comparisonResult = amount.compareTo(balance);

        return comparisonResult > 0 || comparisonResult == 0;
    }

    public Transaction createTransaction(String purpose, BigDecimal amount){
        Transaction transaction = new Transaction();
        transaction.setAccount(selectedAccount);
        transaction.setAmount(amount);
        transaction.setTransactionType(purpose);

        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(new Date());


        LocalDateTime dateTime = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedDateTime = dateTime.format(formatter);

        System.out.println("Форматированное время: " + formattedDateTime);
        transaction.setDate(date);
        transaction.setTime(formattedDateTime);
        return transaction;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        TextFormatter<Integer> textFormatter = new TextFormatter<>(new IntegerStringConverter(), null,
                c -> {
                    if (c.getControlNewText().isEmpty() || c.getControlNewText().matches("^[1-9][0-9]*$")) {
                        return c;
                    }


                    return null;
                });

        tfAmount.setTextFormatter(textFormatter);

        TextFormatter<Integer> textFormatterTranslate = new TextFormatter<>(new IntegerStringConverter(), null,
                c -> {
                    if (c.getControlNewText().isEmpty() || c.getControlNewText().matches("^[1-9][0-9]*$")) {
                        return c;
                    }


                    return null;
                });

        tfAmountTranslate.setTextFormatter(textFormatterTranslate);


        this.accountList = getAllAccounts();

        for(Account account : accountList){
            if(account.getAccountType().equalsIgnoreCase("accumalate")) {
                cbAccount.getItems().add(account.getId());
                cmAccountSender.getItems().add(account.getId());
                cbAccountReceiver.getItems().add(account.getId());
            }


        }

        infoPayment.setVisible(true);
        infoTransalte.setVisible(false);

        AccountComponent accountComponent = new AccountComponent();
        cart.getChildren().add(accountComponent);

        AccountComponent accountComponentReceiver = new AccountComponent();
        cardRecevier.getChildren().add(accountComponentReceiver);

        AccountComponent accountComponentSender = new AccountComponent();
        cardSender.getChildren().add(accountComponentSender);

    }

    public void chooseAccount(ActionEvent actionEvent) {
        this.selectedAccount = finder(cbAccount, cart);

    }

    public void chooseAccountSender(ActionEvent actionEvent) {
        this.selectedAccount = finder(cmAccountSender, cardSender);
    }

    public void chooseAccountReceiver(ActionEvent actionEvent) {
        this.receiverAccount = finder(cbAccountReceiver, cardRecevier);
    }

    private Account finder(ComboBox<Integer> cbAccountReceiver, Pane cardRecevier) {
        Optional<Account> oExp = accountList.stream()
                .filter(account -> account.getId().equals(cbAccountReceiver.getSelectionModel().getSelectedItem()))
                .findFirst();

        if (oExp.isPresent()) {
            Account account = oExp.get();


            AccountComponent accountComponent = (AccountComponent) cardRecevier.getChildren().get(0);
            accountComponent.setAccount(account);
            return account;
        } else {
            System.out.println("Объект accc с номером  не найден.");
        }
        return null;
    }
}
