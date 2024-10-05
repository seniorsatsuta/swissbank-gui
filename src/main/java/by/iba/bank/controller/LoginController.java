package by.iba.bank.controller;

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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
/*import main.Enums.RequestType;
import main.Enums.ResponseStatus;
import main.Enums.Roles;
import main.Models.Entities.User;
import main.Models.TCP.Request;
import main.Models.TCP.Response;
import main.Utility.ClientSocket;*/

import java.io.IOException;

public class LoginController {
    @FXML
    private Button buttonLogin;

    @FXML
    private Button buttonRegister;

    @FXML
    private Label labelMessage;

    @FXML
    private ImageView logo;

    @FXML
    private PasswordField passwordfieldPassword;

    @FXML
    private TextField textfieldLogin;

    private static final String pathAdmin = "dashboard.fxml";
    private static final String pathUser = "crud-accounts.fxml";


    @FXML
    public void logInAction(ActionEvent actionEvent) throws IOException {

        if(textfieldLogin.getText().isEmpty() || passwordfieldPassword.getText().isEmpty()) {
            labelMessage.setVisible(true);
            return;
        }

        User user = new User();
        user.setUsername(textfieldLogin.getText());
        user.setPassword(HashPassword.getHash(passwordfieldPassword.getText()));


        Request requestModel = new Request();

        requestModel.setRequestMessage(new Gson().toJson(user));
        requestModel.setRequestType(RequestType.LOGIN);

        ClientSocket.getInstance().getOut().println(new Gson().toJson(requestModel));
        ClientSocket.getInstance().getOut().flush();

        String answer = ClientSocket.getInstance().getInStream().readLine();
        Response responseModel = new Gson().fromJson(answer, Response.class);

        if (responseModel.getResponseStatus() == ResponseStatus.OK) {
            labelMessage.setVisible(false);
            user = new Gson().fromJson(responseModel.getResponseData(), User.class);
            ClientSocket.getInstance().setUser(user);


            Stage stage = (Stage) buttonLogin.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/by/iba/bank/" + (user.getRole().equalsIgnoreCase("admin")? pathAdmin:pathUser)));
            stage.setScene(new Scene(root));
        } else {
            labelMessage.setVisible(true);
        }
    }

    @FXML
    public void registerAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) buttonLogin.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/by/iba/bank/user-scene.fxml"));
        Parent root = loader.load();
        UserController userController = loader.getController();
        userController.registerMode();
        stage.setScene(new Scene(root));




    }
}
