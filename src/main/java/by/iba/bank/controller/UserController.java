package by.iba.bank.controller;

import by.iba.bank.model.entity.Client;
import by.iba.bank.model.entity.User;
import by.iba.bank.model.messages.Request;
import by.iba.bank.model.messages.RequestType;
import by.iba.bank.model.messages.Response;
import by.iba.bank.model.messages.ResponseStatus;
import by.iba.bank.utility.ClientSocket;
import by.iba.bank.utility.HashPassword;
import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class UserController implements Initializable {

    @FXML
    public ComboBox<String> comboBox;
    @FXML
    private Button action;

    @FXML
    private ImageView logo;

    @FXML
    private TextField tfAdress;

    @FXML
    private TextField tfLastName;

    @FXML
    private TextField tfLogin;

    @FXML
    private TextField tfMail;

    @FXML
    private TextField tfName;

    @FXML
    private TextField tfNumber;

    @FXML
    private PasswordField tfPassword;

    @FXML
    private PasswordField tfPasswordRepeat;

    @FXML
    private Label message;

    @FXML
    private Label labelRole;

    private User user;

    private RequestType requestType;
    public String path;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        labelRole.setVisible(false);
        comboBox.setVisible(false);
        comboBox.getItems().addAll("ADMIN", "USER");
        user = new User();
        user.setClient(new Client());
    }

    public void saveMode(){
        comboBox.setVisible(true);
        labelRole.setVisible(true);
        action.setText("Добавить");
        requestType = RequestType.REGISTER_NEW_USER;
        this.path = "/by/iba/bank/crud-users.fxml";
    }

    public void editMode(User edituser){
        this.user = edituser;
        labelRole.setVisible(true);
        comboBox.setVisible(true);

        comboBox.setValue(user.getRole());
        tfAdress.setText(user.getClient().getAddress());
        tfLogin.setText(user.getUsername());
        tfMail.setText(user.getClient().getEmail());
        tfName.setText(user.getClient().getFirstName());
        tfLastName.setText(user.getClient().getLastName());
        tfNumber.setText(user.getClient().getPhone());




        action.setText("Редактировать");
        requestType = RequestType.PUT_NEW_USER;
        this.path = "/by/iba/bank/crud-users.fxml";
    }

    public void registerMode(){
        requestType = RequestType.REGISTER_NEW_USER;
        this.path = "/by/iba/bank/login-scene.fxml";
    }


    public boolean makeUser(){
        message.setVisible(false);

        if (tfLogin.getText().isEmpty() || tfPassword.getText().isEmpty() || tfPasswordRepeat.getText().isEmpty()
                || tfNumber.getText().isEmpty() || tfAdress.getText().isEmpty() || tfName.getText().isEmpty() ||
                tfLastName.getText().isEmpty() || tfMail.getText().isEmpty()) {
            message.setText("Не все поля заполнены.");
            message.setVisible(true);
            return false;
        }

        if (!Arrays.equals(HashPassword.getHash(tfPassword.getText()),(HashPassword.getHash(tfPasswordRepeat.getText())))) {
            message.setText("Пароли не совпадают");
            message.setVisible(true);
            return false;
        }


        user.setUsername(tfLogin.getText());
        user.setPassword(HashPassword.getHash(tfPassword.getText()));
        user.setRole(comboBox.getSelectionModel().getSelectedItem());

        user.getClient().setFirstName(tfName.getText());
        user.getClient().setLastName(tfLastName.getText());
        user.getClient().setAddress(tfAdress.getText());
        user.getClient().setEmail(tfMail.getText());
        user.getClient().setPhone(tfNumber.getText());


        return true;
    }

    @FXML
    public void execute(ActionEvent actionEvent) throws IOException {

        if(!makeUser()) return;
        Request request = new Request();
        request.setRequestMessage(new Gson().toJson(user));
        request.setRequestType(requestType);

        ClientSocket.getInstance().getOut().println(new Gson().toJson(request));
        ClientSocket.getInstance().getOut().flush();

        String answer = ClientSocket.getInstance().getInStream().readLine();
        Response response = new Gson().fromJson(answer, Response.class);

        Stage stage = (Stage) action.getScene().getWindow();
        if (response.getResponseStatus() == ResponseStatus.OK) {
            message.setVisible(false);
            if(!labelRole.isVisible())
            {
                ClientSocket.getInstance().setUser(new Gson().fromJson(response.getResponseData(), User.class));

                Parent root = FXMLLoader.load(getClass().getResource(path));
                stage.setScene(new Scene(root));
            }else{
                stage.close();
            }
        } else {
            message.setText(response.getResponseMessage());
            message.setVisible(true);
        }
    }
}


