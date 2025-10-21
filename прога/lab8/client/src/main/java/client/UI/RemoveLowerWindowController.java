package client.UI;

import Network.Request;
import Network.User;
import Utils.Client;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class RemoveLowerWindowController {
    private Client client = ApplicationClient.getClient();
    private User user = client.getUser();
    private MainPageController mainPageController;
    private Locale locale = MainPageController.locale;

    @FXML private ResourceBundle resources;
    @FXML private URL location;
    @FXML private TextField idField;
    @FXML private Button submitButton;
    @FXML private Label messageLabel;

    @FXML
    void initialize() {
        resources = ResourceBundle.getBundle("locales", locale);
        setLocaleText();

        submitButton.setOnAction(actionEvent -> {
            try {
                String id = idField.getText().trim();
                var response = client.sendAndReceive(new Request(user, "remove_lower", id));
                mainPageController.printResponse(response.getMessage());
            } catch (IOException | ClassNotFoundException e) {
                messageLabel.setText(resources.getString("connection_error"));
            }
        });
    }

    private void setLocaleText() {
        idField.setPromptText(resources.getString("id"));
        submitButton.setText(resources.getString("submit"));
    }

    public void setParent(MainPageController mainPageController) {
        this.mainPageController = mainPageController;
    }
}