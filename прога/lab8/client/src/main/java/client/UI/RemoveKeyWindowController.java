package client.UI;

import Network.Request;
import Network.User;
import Utils.Client;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RemoveKeyWindowController {
    private Client client = ApplicationClient.getClient();
    private User user = client.getUser();
    private MainPageController mainPageController;

    @FXML private TextField keyField;
    @FXML private Button submitButton;
    @FXML private Label messageLabel;

    @FXML
    void initialize() {
        submitButton.setOnAction(actionEvent -> {
            try {
                long key = Long.parseLong(keyField.getText().trim());
                var response = client.sendAndReceive(new Request(user, "remove_key", String.valueOf(key)));
                mainPageController.printResponse(response.getMessage());
                mainPageController.refreshData();
                closeWindow();
            } catch (NumberFormatException e) {
                messageLabel.setText("Ошибка: ключ должен быть числом");
            } catch (Exception e) {
                messageLabel.setText("Ошибка: " + e.getMessage());
            }
        });
    }

    private void closeWindow() {
        Stage stage = (Stage) submitButton.getScene().getWindow();
        stage.close();
    }

    public void setParent(MainPageController mainPageController) {
        this.mainPageController = mainPageController;
    }
}