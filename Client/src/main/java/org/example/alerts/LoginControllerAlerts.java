package org.example.alerts;

import javafx.scene.control.Alert;

public class LoginControllerAlerts {
    public static void showLoginErrorAlert() {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Invalid Credentials");
        errorAlert.setContentText("Please enter valid credentials!");
        errorAlert.showAndWait();
    }
}
