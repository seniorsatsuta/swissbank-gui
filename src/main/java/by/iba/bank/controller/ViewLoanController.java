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
import com.google.gson.reflect.TypeToken;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import lombok.SneakyThrows;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.URL;
import java.util.*;

public class ViewLoanController implements Initializable {

    @FXML
    public Button buttonRates;
    @FXML
    public Button btnInfo;
    @FXML
    public Button btnApply;
    @FXML
    private Label amount;

    @FXML
    private BarChart<String, BigDecimal> chartRates;

    @FXML
    private Label eiler;

    @FXML
    private Label erdash;

    @FXML
    private Pane infoAboutLoan;

    @FXML
    private Pane infoRates;

    @FXML
    private Label libor;

    @FXML
    private Label monthPay;

    @FXML
    private Label rate;

    @FXML
    private Label term;

    @FXML
    private Label message;

    private List<LoanInterestRate> rates;
    private Loan loan;

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String answer = ClientSocket.getInstance().getInStream().readLine();
        Response response = new Gson().fromJson(answer, Response.class);

        TypeToken<List<LoanInterestRate>> token = new TypeToken<>() {};
        this.rates = new Gson().fromJson(response.getResponseData(), token.getType());

        this.loan = rates.get(0).getLoan();


        if (response.getResponseStatus() == ResponseStatus.OK) {

            amount.setText(loan.getAmount().toString());
            term.setText(loan.getTerm().toString());
            monthPay.setText(loan.getMonthlyPayment().toString());
            rate.setText(loan.getInterestRate().multiply(BigDecimal.valueOf(100)) + "%");

            eiler.setText(rates.get(0).getEffectiveInterestRate().multiply(BigDecimal.valueOf(100)) + "%");
            erdash.setText(rates.get(1).getEffectiveInterestRate().multiply(BigDecimal.valueOf(100)) + "%");
            libor.setText(rates.get(2).getEffectiveInterestRate().multiply(BigDecimal.valueOf(100)) + "%");

            XYChart.Series<String, BigDecimal> series = new XYChart.Series<>();
            series.getData().add(new XYChart.Data<>("Euler", rates.get(0).getEffectiveInterestRate()));
            series.getData().add(new XYChart.Data<>("Erdash", rates.get(1).getEffectiveInterestRate()));
            series.getData().add(new XYChart.Data<>("Libor", rates.get(2).getEffectiveInterestRate()));
            series.getData().add(new XYChart.Data<>("Rate", loan.getInterestRate()));


            chartRates.getData().add(series);

            for (XYChart.Data<String, BigDecimal> data : series.getData()) {
                Node node = data.getNode();
                String barColor = "-fx-bar-fill: green;";
                node.setStyle(barColor);
            }



            infoAboutLoan.setVisible(true);
        } else {
            message.setVisible(true);
        }
    }

    @FXML
    void applyStatement(ActionEvent event) throws IOException{
        Request request = new Request();
        request.setRequestMessage(new Gson().toJson(rates));
        request.setRequestType(RequestType.POST_LOAN);

        ClientSocket.getInstance().getOut().println(new Gson().toJson(request));
        ClientSocket.getInstance().getOut().flush();

        String answer = ClientSocket.getInstance().getInStream().readLine();
        Response response = new Gson().fromJson(answer, Response.class);

        TypeToken<List<LoanInterestRate>> token = new TypeToken<>() {};
        List<LoanInterestRate> rates = new Gson().fromJson(response.getResponseData(), token.getType());

        if (response.getResponseStatus() == ResponseStatus.OK) {
            Stage stage = (Stage) term.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/by/iba/bank/crud-accounts.fxml"));
            stage.setScene(new Scene(root));
        }else{
            infoRates.setVisible(false);
            infoAboutLoan.setVisible(false);
            btnInfo.setVisible(false);
            buttonRates.setVisible(false);
            btnApply.setVisible(false);
            message.setVisible(true);
        }

    }

    @FXML
    void returnBack(ActionEvent event) throws IOException {
        Stage stage = (Stage) term.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/by/iba/bank/crud-accounts.fxml"));
        stage.setScene(new Scene(root));
    }

    @FXML
    void showInfoAboutEfficientRates(ActionEvent event) {
        infoAboutLoan.setVisible(false);
        infoRates.setVisible(true);

        buttonRates.getStyleClass().clear();
        buttonRates.getStyleClass().add("button-statement-selected");
        btnInfo.getStyleClass().clear();
        btnInfo.getStyleClass().add("button-statement");
    }

    @FXML
    void showInfoAboutLoan(ActionEvent event) {
        infoRates.setVisible(false);
        infoAboutLoan.setVisible(true);

        btnInfo.getStyleClass().clear();
        btnInfo.getStyleClass().add("button-statement-selected");
        buttonRates.getStyleClass().clear();
        buttonRates.getStyleClass().add("button-statement");

    }


}
