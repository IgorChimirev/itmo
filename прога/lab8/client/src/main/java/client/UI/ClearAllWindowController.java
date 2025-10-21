package client.UI;

import Network.Request;
import Network.User;
import Utils.Client;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.ResourceBundle;

public class ClearAllWindowController {
    private Client client = ApplicationClient.getClient();
    private User user = client.getUser();
    private MainPageController mainPageController;

    @FXML private Button confirmButton;
    @FXML private Button cancelButton;
    @FXML private Label messageLabel;

    @FXML
    void initialize() {
        // Локализация
        confirmButton.setText(ResourceBundle.getBundle("locales").getString("yes"));
        cancelButton.setText(ResourceBundle.getBundle("locales").getString("no"));

        confirmButton.setOnAction(actionEvent -> {
            try {
                var response = client.sendAndReceive(new Request(user, "clear_all", ""));
                mainPageController.printResponse(response.getMessage());
                mainPageController.RefreshObjectsTable(response.getCollection());
                closeWindow();
            } catch (Exception e) {
                messageLabel.setText("Ошибка: " + e.getMessage());
            }
        });

        cancelButton.setOnAction(actionEvent -> closeWindow());
    }

    private void closeWindow() {
        Stage stage = (Stage) confirmButton.getScene().getWindow();
        stage.close();
    }

    public void setParent(MainPageController mainPageController) {
        this.mainPageController = mainPageController;
    }
}