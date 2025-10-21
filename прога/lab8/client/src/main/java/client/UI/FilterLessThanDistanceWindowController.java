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

public class FilterLessThanDistanceWindowController {
    private Client client = ApplicationClient.getClient();
    private User user = client.getUser();
    private MainPageController mainPageController;
    private static Locale locale = MainPageController.locale;

    @FXML private ResourceBundle resources;
    @FXML private AnchorPane filterPane;
    @FXML private TextField distanceField;
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
                String distanceStr = distanceField.getText().trim();
                if (distanceStr.isEmpty()) {
                    throw new IllegalArgumentException(resources.getString("empty_distance"));
                }

                double distance = Double.parseDouble(distanceStr);
                if (distance <= 0) {
                    throw new IllegalArgumentException(resources.getString("distance_positive"));
                }

                Request request = new Request(user, "filter_less_than_distance", distanceStr);
                var response = client.sendAndReceive(request);

                mainPageController.printResponse(response.getMessage());
                mainPageController.RefreshObjectsTable(response.getCollection());
                closeWindow();

            } catch (NumberFormatException e) {
                messageLabel.setText(resources.getString("invalid_number"));
            } catch (IllegalArgumentException | IOException | ClassNotFoundException e) {
                messageLabel.setText(e.getMessage());
            }
        });
    }

    private void closeWindow() {
        if (filterPane != null && filterPane.getScene() != null) {
            Stage stage = (Stage) filterPane.getScene().getWindow();
            stage.close();
        }
    }

    public void setLocaleText() {
        submitButton.setText(resources.getString("submit"));
        distanceField.setPromptText(resources.getString("distance_prompt"));
    }

    public void setParent(MainPageController mainPageController) {
        this.mainPageController = mainPageController;
    }
}