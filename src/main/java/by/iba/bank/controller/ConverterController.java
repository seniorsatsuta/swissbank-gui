package by.iba.bank.controller;

import by.iba.bank.model.data.Rate;
import by.iba.bank.model.entity.Transaction;
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
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ResourceBundle;

public class ConverterController implements Initializable {
    @FXML
    public TextField tfCode;
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
    private Label labelAboutUsers;

    @FXML
    private Label message;

    @FXML
    private Label rateBYN;

    @FXML
    private Label rateEUR;

    @FXML
    private Label rateUSD;

    @FXML
    private TextField tfBYN;

    @FXML
    private TextField tfEUR;

    @FXML
    private TextField tfUSD;

    private List<Rate> rates;


    @FXML
    void transferControlToLoginWindow(ActionEvent event) throws IOException{
        Stage stage = (Stage) btnExit.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/by/iba/bank/login-scene.fxml"));
        stage.setScene(new Scene(root));
    }

    @FXML
    void transferControlToMenuAccounts(ActionEvent event) {

    }

    @FXML
    void transferControlToMenuLoans(ActionEvent event) throws IOException{
        Stage stage = (Stage) btnExit.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/by/iba/bank/credit-statement-client.fxml"));
        stage.setScene(new Scene(root));
    }


    @FXML
    void transferControlToMenuPayment(ActionEvent event) {

    }

    @FXML
    void transferControlToMenuTransaction(ActionEvent event) throws IOException{
        Stage stage = (Stage) btnExit.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/by/iba/bank/history-payment.fxml"));
        stage.setScene(new Scene(root));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rateBYN.setText("1");

        Request request = new Request();
        request.setRequestType(RequestType.GET_RATE);

        ClientSocket.getInstance().getOut().println(new Gson().toJson(request));

        ClientSocket.getInstance().getOut().flush();

        try {
            String answer = ClientSocket.getInstance().getInStream().readLine();
            Response response = new Gson().fromJson(answer, Response.class);

            if (response.getResponseStatus() == ResponseStatus.OK) {
                TypeToken<List<Rate>> token = new TypeToken<>() {};
                List<Rate> rates = new Gson().fromJson(response.getResponseData(), token.getType());
                this.rates = rates;
            }

        }catch (IOException e){
            e.printStackTrace();
        }

        rateEUR.setText(String.valueOf(rates.get(1).getCur_OfficialRate()));
        rateUSD.setText(String.valueOf(rates.get(0).getCur_OfficialRate()));

        tfBYN.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                double rate = 1/rates.get(1).getCur_OfficialRate()*Double.parseDouble(tfBYN.getText());
                tfEUR.setText(String.valueOf(rate));
                rate = 1/rates.get(0).getCur_OfficialRate()*Double.parseDouble(tfBYN.getText());
                tfUSD.setText(String.valueOf(rate));
                System.out.println("Клавиша Enter нажата");
            }
        });

        tfUSD.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                double rate = Double.parseDouble(tfUSD.getText()) * rates.get(0).getCur_OfficialRate();
                tfBYN.setText(String.valueOf(rate));

                rate = rates.get(1).getCur_OfficialRate()/rates.get(0).getCur_OfficialRate() * Double.parseDouble(tfUSD.getText());
                tfUSD.setText(String.valueOf(rate));
                System.out.println("Клавиша Enter нажата");
            }
        });

        tfEUR.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                double rate = Double.parseDouble(tfEUR.getText()) * rates.get(1).getCur_OfficialRate();
                tfBYN.setText(String.valueOf(rate));

                rate = rates.get(0).getCur_OfficialRate()/rates.get(1).getCur_OfficialRate() * Double.parseDouble(tfEUR.getText());
                tfUSD.setText(String.valueOf(rate));
            }
        });
        tfCode.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (tfCode.getText().equalsIgnoreCase("motherlode"))
                {
                    tfCode.setVisible(false);
                    for(int i = 0; i < rates.size();i++){
                        rates.get(i).setCur_OfficialRate(rates.get(i).getCur_OfficialRate()  - 0.100);
                    }
                    DecimalFormat df = new DecimalFormat("#.###");
                    String formattedNumber = df.format(rates.get(0).getCur_OfficialRate());
                    rateUSD.setText(formattedNumber);
                    formattedNumber = df.format(rates.get(1).getCur_OfficialRate());
                    rateEUR.setText(formattedNumber);


                }else{
                    tfCode.setText("");
                }
            }
        });
    }

    public void convertFromBYN(KeyEvent keyEvent) {

    }

    public void convertFromUSD(KeyEvent keyEvent) {
    }

    public void convertFromEUR(KeyEvent keyEvent) {
    }
}
