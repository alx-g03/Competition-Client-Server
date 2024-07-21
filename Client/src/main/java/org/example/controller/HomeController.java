package org.example.controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.alerts.HomeControllerAlerts;
import org.example.model.Challenge;
import org.example.model.Participant;
import org.example.model.User;
import org.example.services.Services;
import org.example.services.UserObserver;

import java.util.List;

import static org.example.alerts.HomeControllerAlerts.showInvalidAgeOrMaximumChallengesAlert;

public class HomeController implements UserObserver {
    @FXML
    private TableView<Challenge> challengesTable;
    @FXML
    private TableColumn<Challenge, String> typeColumn;
    @FXML
    private TableColumn<Challenge, String> ageCategoryColumn;
    @FXML
    private TableColumn<Challenge, Integer> noParticipantsColumn;
    @FXML
    private TableView<Participant> participantsSearchTable;
    @FXML
    private TableColumn<Participant, String> nameSearchColumn;
    @FXML
    private TableColumn<Participant, Integer> ageSearchColumn;
    @FXML
    private Button logoutButton;
    @FXML
    private TextField typeSearchField;
    @FXML
    private TextField ageCategorySearchField;
    @FXML
    private TextField nameAddField;
    @FXML
    private TextField ageAddField;
    @FXML
    private TextField typeAddField;
    private Services server;
    private User user;
    private static final Logger logger = LogManager.getLogger();

    public void setServer(Services server,  User user, Stage stage) {
        this.server = server;
        this.user = user;
        setClient();
    }

    public void setClient() {
        server.changeClient(this);
    }

    protected void populateChallengesTable() {
        logger.debug("Populating challenges table...");

        List<Challenge> challenges = server.findAllChallenges();

        ObservableList<Challenge> challengeObservableList = FXCollections.observableArrayList(challenges);

        challengesTable.setItems(challengeObservableList);

        typeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType()));
        ageCategoryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAgeCategory()));
        noParticipantsColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getNoParticipants()).asObject());

        logger.debug("Challenges table populated.");
    }

    public void handleSearch() {
        String type = typeSearchField.getText();
        String ageCategory = ageCategorySearchField.getText();

        logger.debug("Searching for challenge with type: {}, age category: {}", type, ageCategory);

        try {
            participantsSearchTable.getItems().clear();
            ObservableList<Participant> participantObservableList = FXCollections.observableArrayList(server.findParticipantsByChallengeTypeAndAgeCategory(type, ageCategory));
            participantsSearchTable.setItems(participantObservableList);

            nameSearchColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
            ageSearchColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAge()).asObject());

            logger.debug("Participants table populated.");
        }
        catch(Exception e) {
            logger.warn("No challenge found for type: {}, age category: {}", type, ageCategory);
            HomeControllerAlerts.showNoChallengeFoundErrorAlert();
        }
    }

    public void handleAddParticipant() {
        logger.debug("Adding participant...");

        String name = nameAddField.getText();
        Integer age = Integer.valueOf(ageAddField.getText());
        String type = typeAddField.getText();

        try {
            server.addParticipant(name, age, type);
        }
        catch(Exception e) {
            showInvalidAgeOrMaximumChallengesAlert(e);
        }
    }

    public void handleLogout() {
        logger.info("Logging out...");

        try {
            server.logout(user.getUsername());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login-view.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            Stage currentStage = (Stage) logoutButton.getScene().getWindow();
            currentStage.close();

            logger.info("Logged out successfully.");
        } catch (Exception e) {
            logger.error("Error occurred during logout: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void participationAdded() {
        Platform.runLater(this::handleSearch);
    }
}
