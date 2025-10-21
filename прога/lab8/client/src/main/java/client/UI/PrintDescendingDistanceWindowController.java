package client.UI;

import Network.Request;
import Network.User;
import Utils.Client;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class PrintDescendingDistanceWindowController {
    private Client client = ApplicationClient.getClient();
    private User user = client.getUser();
    private MainPageController mainPageController;
    private Locale locale = MainPageController.locale;

    @FXML private ResourceBundle resources;
    @FXML private URL location;
    @FXML private Button submitButton;

    @FXML
    void initialize() {
        resources = ResourceBundle.getBundle("locales", locale);
        submitButton.setText(resources.getString("submit"));

        submitButton.setOnAction(actionEvent -> {
            try {
                var response = client.sendAndReceive(new Request(user, "PrintFieldDescendingDistance", ""));
                mainPageController.printResponse(response.getMessage());
            } catch (IOException | ClassNotFoundException e) {
                mainPageController.printResponse(resources.getString("connection_error"));
            }
        });
    }

    public void setParent(MainPageController mainPageController) {
        this.mainPageController = mainPageController;
    }
}