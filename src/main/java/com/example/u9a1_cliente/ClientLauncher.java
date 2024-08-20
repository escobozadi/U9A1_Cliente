package com.example.u9a1_cliente;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ClientLauncher extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("client-username.fxml"))));
        stage.setTitle("LogIn");
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.show();
        /*
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("clientView.fxml"));
        ClientController controller = new ClientController();
        fxmlLoader.setController(controller);
        stage.setScene(new Scene(fxmlLoader.load()));

        Stage primaryStage = new Stage();
        primaryStage.initModality(Modality.WINDOW_MODAL);
        primaryStage.initOwner(stage.getScene().getWindow());
        primaryStage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("client-username.fxml")))));
        primaryStage.setTitle("Login");
        primaryStage.centerOnScreen();
        primaryStage.setResizable(false);
        primaryStage.show();
        */
    }

    public static void main(String[] args) {
        launch(args);
    }
}
