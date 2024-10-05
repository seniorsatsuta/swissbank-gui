package by.iba.bank;

import by.iba.bank.utility.ClientSocket;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class BankApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

       // ClientSocket.getInstance().getSocket();

        FXMLLoader fxmlLoader = new FXMLLoader(BankApplication.class.getResource("login-scene.fxml"));

        Image icon = new Image(getClass().getResourceAsStream("/by/iba/bank/images/logo.jpg"));

        // Установка иконки
        stage.getIcons().add(icon);

        stage.setResizable(false);
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("SWISS BANK");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}