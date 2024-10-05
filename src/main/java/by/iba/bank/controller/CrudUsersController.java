package by.iba.bank.controller;

import by.iba.bank.model.entity.Client;
import by.iba.bank.model.entity.User;
import by.iba.bank.model.messages.Request;
import by.iba.bank.model.messages.RequestType;
import by.iba.bank.model.messages.Response;
import by.iba.bank.model.messages.ResponseStatus;
import by.iba.bank.utility.ClientSocket;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
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
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CrudUsersController implements Initializable {
    @FXML
    private TableColumn<User, String> adress;

    @FXML
    private Button brnDelete;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnDashboard;

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
    private TableColumn<User, String> email;

    @FXML
    private TableColumn<User, Integer> idClient;

    @FXML
    private Label labelAboutUsers;

    @FXML
    private TableColumn<User, String> name;

    @FXML
    private TableColumn<User, String> number;

    @FXML
    private TableColumn<User, String> role;

    @FXML
    private TableColumn<User, String> surname;

    @FXML
    private TableView<User> table;

    @FXML
    private TableColumn<User, String> username;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        labelAboutUsers.setText("Добро пожаловать, " + ClientSocket.getInstance().getUser().getClient().getFirstName());
        uploadTable();
    }

    public void uploadTable(){
        table.getItems().clear();

        username.setCellValueFactory(new PropertyValueFactory<>("username"));
        role.setCellValueFactory(new PropertyValueFactory<>("role"));

        // Настройка фабрики значений для колонки object2Column
        idClient.setCellValueFactory(cellData -> {
            return  new SimpleObjectProperty<>(cellData.getValue().getClient().getId());
        });

        name.setCellValueFactory(cellData -> {
            return  new SimpleObjectProperty<>(cellData.getValue().getClient().getFirstName());
        });

        surname.setCellValueFactory(cellData -> {
            return  new SimpleObjectProperty<>(cellData.getValue().getClient().getLastName());
        });

        number.setCellValueFactory(cellData -> {
            return  new SimpleObjectProperty<>(cellData.getValue().getClient().getPhone());
        });

        adress.setCellValueFactory(cellData -> {
            return  new SimpleObjectProperty<>(cellData.getValue().getClient().getAddress());
        });

        email.setCellValueFactory(cellData -> {
            return  new SimpleObjectProperty<>(cellData.getValue().getClient().getEmail());
        });


        Request request = new Request();
        request.setRequestType(RequestType.GET_ALL_USERS);

        ClientSocket.getInstance().getOut().println(new Gson().toJson(request));
        ClientSocket.getInstance().getOut().flush();



        try {
            String answer = ClientSocket.getInstance().getInStream().readLine();
            Response response = new Gson().fromJson(answer, Response.class);

            if (response.getResponseStatus() == ResponseStatus.OK) {
                TypeToken<List<User>> token = new TypeToken<>() {};
                List<User> users = new Gson().fromJson(response.getResponseData(), token.getType());
                table.getItems().addAll(users);
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    void transferControlToLoginWindow(ActionEvent event) throws IOException {

        Stage stage = (Stage) btnExit.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/by/iba/bank/login-scene.fxml"));
        stage.setScene(new Scene(root));
    }

    @FXML
    void transferControlToMenuDeposits(ActionEvent event) throws IOException{

    }

    @FXML
    void transferControlToMenuLoans(ActionEvent event) throws IOException {

    }

    @FXML
    void transferControlToMenuTransaction(ActionEvent event) {

    }

    public void transfetControlToMenuDashboard(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) btnExit.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/by/iba/bank/dashboard.fxml"));
        stage.setScene(new Scene(root));
    }

    public void deleteUser(ActionEvent actionEvent) throws IOException{
        Request request = new Request();
        request.setRequestType(RequestType.DELETE_USER);
        request.setRequestMessage(new Gson().toJson(table.getSelectionModel().getSelectedItem()));

        ClientSocket.getInstance().getOut().println(new Gson().toJson(request));
        ClientSocket.getInstance().getOut().flush();

        String answer = ClientSocket.getInstance().getInStream().readLine();

        Response response = new Gson().fromJson(answer, Response.class);
        uploadTable();

    }

    public void createDialogStage(String mode) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/by/iba/bank/user-scene.fxml"));

        Scene scene = new Scene(loader.load());
        Stage dialogStage = new Stage();

        Image icon = new Image(getClass().getResourceAsStream("/by/iba/bank/images/logo.jpg"));
        dialogStage.getIcons().add(icon);

        dialogStage.setResizable(false);

        dialogStage.setTitle(mode);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(btnAdd.getScene().getWindow());

        UserController userController = loader.getController();
        if(mode.equalsIgnoreCase("создать"))
            userController.saveMode();
        else
            userController.editMode(table.getSelectionModel().getSelectedItem());

        dialogStage.setScene(scene);
        dialogStage.showAndWait();

        dialogStage.close();

    }
    public void addUser(ActionEvent actionEvent) throws IOException{
        createDialogStage("Создать");
        uploadTable();
    }


    public void editUser(MouseEvent mouseEvent) throws IOException{
        if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 2){
            createDialogStage("Редактировать");
            uploadTable();
        }

    }
}
