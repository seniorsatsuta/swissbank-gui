package by.iba.bank.controller;

import by.iba.bank.utility.ClientSocket;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    @FXML
    private Button btnDeposits;

    @FXML
    private Button btnExit;

    @FXML
    private Button btnLoans;

    @FXML
    private Button btnTransaction;

    @FXML
    private Button btnUsers;

    @FXML
    private Label labelAboutUsers;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        labelAboutUsers.setText("Добро пожаловать, " + ClientSocket.getInstance().getUser().getClient().getFirstName());
    }

    @FXML
    void transferControlToLoginWindow(ActionEvent event) throws IOException {

        Stage stage = (Stage) btnExit.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/by/iba/bank/login-scene.fxml"));
        stage.setScene(new Scene(root));
    }

    @FXML
    void transferControlToMenuDeposits(ActionEvent event) {

    }

    @FXML
    void transferControlToMenuLoans(ActionEvent event) {

    }

    @FXML
    void transferControlToMenuTransaction(ActionEvent event) {

    }

    @FXML
    void transferControlToMenuUsers(ActionEvent event) throws IOException {
        Stage stage = (Stage) btnUsers.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/by/iba/bank/crud-users.fxml"));
        stage.setScene(new Scene(root));
    }


}
