package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.example.model.User;
import org.example.services.Services;
import org.example.services.UserObserver;

import java.io.IOException;
import java.util.Optional;

import static org.example.alerts.LoginControllerAlerts.showLoginErrorAlert;

public class LoginController implements UserObserver {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    private Services server;
    private User user;

    public void setServer(Services server){
        this.server = server;
    }

    public void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        Optional<User> optional = Optional.empty();
        try {
            optional = server.login(username, password, this);
            if (optional.isEmpty()) {
                showLoginErrorAlert();
            } else {
                user = optional.get();
                loadHomeScene();
            }
        } catch (Exception e) {
            showLoginErrorAlert();
        }
    }

    private void loadHomeScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/home-view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) loginButton.getScene().getWindow();
            HomeController homeController = loader.getController();
            homeController.setServer(server, user, stage);
            homeController.populateChallengesTable();

            Stage window = (Stage) loginButton.getScene().getWindow();
            window.setTitle("Home");

            double width = 1080;
            double height = 720;

            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            window.setX((screenBounds.getWidth() - width) / 2);
            window.setY((screenBounds.getHeight() - height) / 2);

            window.setScene(new Scene(root, width, height));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void participationAdded(){}
}
