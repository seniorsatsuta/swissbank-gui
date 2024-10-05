package by.iba.bank.controller;


import by.iba.bank.model.entity.Loan;
import by.iba.bank.model.entity.Transaction;
import by.iba.bank.model.messages.Request;
import by.iba.bank.model.messages.RequestType;
import by.iba.bank.model.messages.Response;
import by.iba.bank.model.messages.ResponseStatus;
import by.iba.bank.utility.ClientSocket;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class HistoryPaymentController implements Initializable {

    @FXML
    private TableColumn<Transaction, String> account;

    @FXML
    private TableColumn<Transaction, BigDecimal> amount;

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
    private TableColumn<Transaction, String> comment;

    @FXML
    private TableColumn<Transaction, String> date;

    @FXML
    private TableColumn<Transaction, Integer> id;

    @FXML
    private Label labelAboutUsers;

    @FXML
    private Label message;

    @FXML
    private TableView<Transaction> table;

    @FXML
    private TableColumn<Transaction, String> time;

    @FXML
    private TableColumn<Transaction, String> type;


    public void uploadTable(){
        table.getItems().clear();

        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        type.setCellValueFactory(new PropertyValueFactory<>("transactionType"));
        time.setCellValueFactory( new PropertyValueFactory<>("time"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        comment.setCellValueFactory(new PropertyValueFactory<>("comment"));

        account.setCellValueFactory(cellData -> {
            return  new SimpleObjectProperty<>(cellData.getValue().getAccount().getName());
        });


        Request request = new Request();
        request.setRequestType(RequestType.GET_ALL_TRANSACTIONS_OF_CLIENT);
        request.setRequestMessage( new Gson().toJson(ClientSocket.getInstance().getUser().getClient()));

        ClientSocket.getInstance().getOut().println(new Gson().toJson(request));

        ClientSocket.getInstance().getOut().flush();

        try {
            String answer = ClientSocket.getInstance().getInStream().readLine();
            Response response = new Gson().fromJson(answer, Response.class);

            if (response.getResponseStatus() == ResponseStatus.OK) {
                TypeToken<List<Transaction>> token = new TypeToken<>() {};
                List<Transaction> transactions = new Gson().fromJson(response.getResponseData(), token.getType());
                table.getItems().addAll(transactions);
            }

        }catch (IOException e){
            e.printStackTrace();
        }
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
    void transferControlToMenuConverter(ActionEvent event) throws IOException{
        Stage stage = (Stage) btnExit.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/by/iba/bank/converter-scene.fxml"));
        stage.setScene(new Scene(root));
    }

    @FXML
    void transferControlToMenuPayment(ActionEvent event) throws IOException{
        Stage stage = (Stage) btnExit.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/by/iba/bank/payment-scene.fxml"));
        stage.setScene(new Scene(root));
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        uploadTable();
    }
}
