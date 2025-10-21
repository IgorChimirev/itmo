package client.UI;

import Network.Request;
import Network.Response;
import Network.User;
import Utils.Client;
import CollectionObject.Route;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class UserElementsWindowController {
    private Client client = ApplicationClient.getClient();
    private User user = client.getUser();
    private MainPageController mainPageController;

    @FXML private TableView<Route> elementsTable;
    @FXML private Label messageLabel;

    @FXML
    public void initialize() {
        loadUserElements();
    }

    // Загрузка элементов пользователя
    private void loadUserElements() {
        try {
            Request request = new Request(user, "show_user_elements", user.getUsername());
            Response response = client.sendAndReceive(request);

            // Проверяем, есть ли элементы в коллекции ответа
            if (!response.getCollection().isEmpty()) {
                ObservableList<Route> userElements =
                        FXCollections.observableList(response.getCollection());
                elementsTable.setItems(userElements);
            } else {
                messageLabel.setText(response.getMessage());
            }
        } catch (Exception e) {
            messageLabel.setText("Ошибка: " + e.getMessage());
        }
    }

    // Закрытие окна
    @FXML
    private void handleClose() {
        Stage stage = (Stage) elementsTable.getScene().getWindow();
        stage.close();
    }

    // Связь с главным окном
    public void setParent(MainPageController mainPageController) {
        this.mainPageController = mainPageController;
    }
}