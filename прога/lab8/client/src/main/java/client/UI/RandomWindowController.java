package client.UI;

import Network.Request;
import Network.User;
import Utils.Client;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class RandomWindowController {
    private Client client = ApplicationClient.getClient();
    private User user = client.getUser();
    private MainPageController mainPageController;

    @FXML private Button confirmButton;
    @FXML private Label messageLabel;

    @FXML
    void initialize() {
        confirmButton.setOnAction(actionEvent -> {
            try {
                var response = client.sendAndReceive(new Request(user, "add_random", ""));
                mainPageController.printResponse(response.getMessage());
                mainPageController.refreshData();
                closeWindow();
            } catch (Exception e) {
                messageLabel.setText("Ошибка: " + e.getMessage());
            }
        });
    }

    private void closeWindow() {
        Stage stage = (Stage) confirmButton.getScene().getWindow();
        stage.close();
    }

    public void setParent(MainPageController mainPageController) {
        this.mainPageController = mainPageController;
    }
}