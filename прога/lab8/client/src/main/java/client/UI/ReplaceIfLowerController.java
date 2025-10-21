package client.UI;

import Exceptions.EmptyFieldException;
import Network.Request;
import Network.User;
import Utils.Client;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class ReplaceIfLowerController {
    @FXML private TextField keyField;
    @FXML private TextField distanceField;
    @FXML private Label messageLabel;

    private Client client = ApplicationClient.getClient();
    private User user = client.getUser();
    private MainPageController mainPageController;
    private ResourceBundle resources;

    @FXML
    void initialize() {
        // Загрузка ресурсов через статическое поле MainPageController.locale
        try {
            resources = ResourceBundle.getBundle("locales", MainPageController.locale != null ? MainPageController.locale : Locale.getDefault());
        } catch (Exception e) {
            resources = ResourceBundle.getBundle("locales", Locale.ROOT);
        }
        setLocaleText();
    }

    public void setParent(MainPageController mainPageController) {
        this.mainPageController = mainPageController;
    }

    @FXML
    private void handleSubmit() {
        try {
            long key = parseLong(keyField.getText(), "key");
            double distance = parseDouble(distanceField.getText(), "distance");
            if (distance <= 0) throw new IllegalArgumentException(resources.getString("distance_positive"));

            Request request = new Request(user, "replace_if_lower", key + " " + distance, null);
            var response = client.sendAndReceive(request);

            mainPageController.printResponse(response.getMessage());
            mainPageController.refreshData();
            closeWindow();

        } catch (IllegalArgumentException | EmptyFieldException e) {
            messageLabel.setText(e.getMessage());
        } catch (IOException | ClassNotFoundException e) {
            messageLabel.setText(resources.getString("connection_error"));
        }
    }

    private long parseLong(String value, String resourceKey) {
        try {
            return Long.parseLong(value.trim());
        } catch (Exception e) {
            throw new IllegalArgumentException(resources.getString("invalid_long") + ": " + resources.getString(resourceKey));
        }
    }

    private double parseDouble(String value, String resourceKey) {
        try {
            return Double.parseDouble(value.trim());
        } catch (Exception e) {
            throw new IllegalArgumentException(resources.getString("invalid_number") + ": " + resources.getString(resourceKey));
        }
    }

    private void setLocaleText() {
        keyField.setPromptText(resources.getString("key"));
        distanceField.setPromptText(resources.getString("new_distance"));
    }

    private void closeWindow() {
        Stage stage = (Stage) keyField.getScene().getWindow();
        stage.close();
    }
}