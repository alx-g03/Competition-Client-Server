package org.example.alerts;

import javafx.scene.control.Alert;

public class HomeControllerAlerts {
    public static void showNoChallengeFoundErrorAlert() {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("No challenge found");
        errorAlert.setContentText("Please search for another challenge");
        errorAlert.showAndWait();
    }

    public static void showInvalidAgeOrMaximumChallengesAlert(Exception e) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Error");
        errorAlert.setContentText(e.getMessage());
        errorAlert.showAndWait();
    }
}
