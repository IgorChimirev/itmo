package client.UI;

import Network.Request;
import Network.User;
import Utils.Client;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class FilterStartsWithNameWindowController {
    private Client client = ApplicationClient.getClient();
    private User user = client.getUser();
    private MainPageController mainPageController;
    private static Locale locale = MainPageController.locale;

    @FXML private ResourceBundle resources;
    @FXML private AnchorPane filterPane;
    @FXML private TextField prefixField;
    @FXML private Button submitButton;
    @FXML private Label messageLabel;

    @FXML
    void initialize() {
        try {
            resources = ResourceBundle.getBundle("locales", locale != null ? locale : Locale.getDefault());
        } catch (Exception e) {
            resources = ResourceBundle.getBundle("locales", Locale.ROOT);
        }
        setLocaleText();

        submitButton.setOnAction(actionEvent -> {
            try {
                String prefix = prefixField.getText().trim();
                if (prefix.isEmpty()) {
                    throw new IllegalArgumentException(resources.getString("empty_prefix"));
                }

                Request request = new Request(user, "filterStartsWithName", prefix);
                var response = client.sendAndReceive(request);

                mainPageController.printResponse(response.getMessage());
                mainPageController.RefreshObjectsTable(response.getCollection());
                closeWindow();

            } catch (IllegalArgumentException e) {
                messageLabel.setText(e.getMessage());
            } catch (IOException | ClassNotFoundException e) {
                messageLabel.setText(resources.getString("connection_error"));
            }
        });
    }

    private void closeWindow() {
        Stage stage = (Stage) filterPane.getScene().getWindow();
        stage.close();
    }

    public void setLocaleText() {
        submitButton.setText(resources.getString("submit"));
        prefixField.setPromptText(resources.getString("prefix_prompt"));
    }

    public void setParent(MainPageController mainPageController) {
        this.mainPageController = mainPageController;
    }
}