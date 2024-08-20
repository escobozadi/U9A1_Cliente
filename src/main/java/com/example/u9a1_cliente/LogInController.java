package com.example.u9a1_cliente;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LogInController {
    @FXML
    private TextField username;

    public void initialize(){
    }

    public void logInButton(ActionEvent actionEvent) throws IOException {
        if (!username.getText().isEmpty() ){ // && username.getText().matches("[A-Za-z0-9]")) {
            Stage primaryStage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("clientView.fxml"));

            ClientController clientController = new ClientController();
            clientController.setClientName(username.getText()); // Set the parameter
            fxmlLoader.setController(clientController);

            primaryStage.setScene(new Scene(fxmlLoader.load()));
            primaryStage.setTitle(username.getText());
            primaryStage.setResizable(false);
            primaryStage.centerOnScreen();
            primaryStage.setOnCloseRequest(windowEvent -> {
                clientController.shutdown();
            });
            primaryStage.show();

            username.clear();
        } else {
            new Alert(Alert.AlertType.ERROR, "Please enter username").show();
        }
    }
}
