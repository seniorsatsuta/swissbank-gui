package by.iba.bank.controller;

import by.iba.bank.model.entity.Loan;
import by.iba.bank.model.entity.User;
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
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoansTableOfClientController implements Initializable {

    @FXML
    public TableView<Loan> table;
    @FXML
    private TableColumn<Loan, BigDecimal> amount;

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
    private TableColumn<Loan, String> date;

    @FXML
    private TableColumn<Loan, Integer> id;

    @FXML
    private Label labelAboutUsers;

    @FXML
    private TableColumn<Loan, BigDecimal> monthPayment;

    @FXML
    private TableColumn<Loan, BigDecimal> rate;

    @FXML
    private TableColumn<Loan, String> status;

    @FXML
    private TableColumn<Loan, Integer> term;

    private Loan loan;



    @FXML
    void transferControlToLoginWindow(ActionEvent event) throws IOException {

        Stage stage = (Stage) btnExit.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/by/iba/bank/login-scene.fxml"));
        stage.setScene(new Scene(root));
    }

    @FXML
    void transferControlToMenuConverter(ActionEvent event) {

    }

    @FXML
    void transferControlToMenuAccounts(ActionEvent event) throws IOException{
        Stage stage = (Stage) btnExit.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/by/iba/bank/crud-accounts.fxml"));
        stage.setScene(new Scene(root));
    }

    @FXML
    void transferControlToMenuPayment(ActionEvent event) throws IOException{
        Stage stage = (Stage) btnExit.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/by/iba/bank/payment-scene.fxml"));
        stage.setScene(new Scene(root));

    }

    @FXML
    void transferControlToMenuTransaction(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        uploadTable();
    }

    public void uploadTable(){
        table.getItems().clear();

        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        term.setCellValueFactory(new PropertyValueFactory<>("term"));
        rate.setCellValueFactory(new PropertyValueFactory<>("interestRate"));
        monthPayment.setCellValueFactory( new PropertyValueFactory<>("monthlyPayment"));
        date.setCellValueFactory(new PropertyValueFactory<>("issueDate"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));

        Request request = new Request();
        request.setRequestType(RequestType.GET_ALL_LOANS_OF_CLIENT);
        request.setRequestMessage( new Gson().toJson(ClientSocket.getInstance().getUser().getClient()));

        ClientSocket.getInstance().getOut().println(new Gson().toJson(request));

        ClientSocket.getInstance().getOut().flush();

        try {
            String answer = ClientSocket.getInstance().getInStream().readLine();
            Response response = new Gson().fromJson(answer, Response.class);

            if (response.getResponseStatus() == ResponseStatus.OK) {
                TypeToken<List<Loan>> token = new TypeToken<>() {};
                List<Loan> loans = new Gson().fromJson(response.getResponseData(), token.getType());
                table.getItems().addAll(loans);
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void cancelStatement(ActionEvent actionEvent) throws IOException{
        Request request = new Request();
        request.setRequestType(RequestType.PUT_LOAN);
        Loan loan = (Loan) table.getSelectionModel().getSelectedItem();
        loan.setStatus("CANCEL");
        request.setRequestMessage(new Gson().toJson(loan));

        ClientSocket.getInstance().getOut().println(new Gson().toJson(request));
        ClientSocket.getInstance().getOut().flush();

        String answer = ClientSocket.getInstance().getInStream().readLine();

        Response response = new Gson().fromJson(answer, Response.class);
        uploadTable();
    }

    public void openShedule(MouseEvent mouseEvent) throws IOException{
        if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 2){
            createDialogStage();
            uploadTable();
        }

    }

    public void createDialogStage() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/by/iba/bank/shedule-scene.fxml"));

        Scene scene = new Scene(loader.load());
        Stage dialogStage = new Stage();

        Image icon = new Image(getClass().getResourceAsStream("/by/iba/bank/images/logo.jpg"));
        dialogStage.getIcons().add(icon);

        dialogStage.setResizable(false);

        dialogStage.setTitle("График платежей");

        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(btnExit.getScene().getWindow());

        ScheduleController scheduleController = loader.getController();
        scheduleController.uploadTable( table.getSelectionModel().getSelectedItem());

        dialogStage.setScene(scene);
        dialogStage.showAndWait();

        dialogStage.close();

    }

}