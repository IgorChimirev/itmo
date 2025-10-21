package client.UI;

import Network.Request;
import Network.User;
import Utils.Client;
import CollectionObject.*;
import Exceptions.EmptyFieldException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class InsertNullWindowController {
    private Client client = ApplicationClient.getClient();
    private User user = client.getUser();
    private MainPageController mainPageController;

    @FXML private ResourceBundle resources;
    @FXML private URL location;
    @FXML private AnchorPane insertNullPane;
    @FXML private Label messageLabel;
    @FXML private TextField keyForm;
    @FXML private TextField nameForm;
    @FXML private TextField coordXForm;
    @FXML private TextField coordYForm;
    @FXML private TextField distanceFrom;
    @FXML private TextField fromXForm;
    @FXML private TextField fromYForm;
    @FXML private TextField fromZForm;
    @FXML private TextField fromNameForm;
    @FXML private TextField toXForm;
    @FXML private TextField toYForm;
    @FXML private TextField toZForm;
    @FXML private TextField toNameForm;
    @FXML private Button insertSubmitButton;

    @FXML
    void initialize() {
        try {
            resources = ResourceBundle.getBundle("locales", MainPageController.locale);
        } catch (Exception e) {
            resources = ResourceBundle.getBundle("locales", Locale.ROOT);
        }

        // Установка подсказок
        keyForm.setPromptText(resources.getString("enter_key"));
        nameForm.setPromptText(resources.getString("name"));
        coordXForm.setPromptText(resources.getString("coord_x"));
        coordYForm.setPromptText(resources.getString("coord_y"));
        distanceFrom.setPromptText(resources.getString("distance"));
        fromXForm.setPromptText(resources.getString("from_x"));
        fromYForm.setPromptText(resources.getString("from_y"));
        fromZForm.setPromptText(resources.getString("from_z"));
        fromNameForm.setPromptText(resources.getString("from_name"));
        toXForm.setPromptText(resources.getString("to_x"));
        toYForm.setPromptText(resources.getString("to_y"));
        toZForm.setPromptText(resources.getString("to_z"));
        toNameForm.setPromptText(resources.getString("to_name"));
        insertSubmitButton.setText(resources.getString("submit"));

        insertSubmitButton.setOnAction(actionEvent -> {
            try {
                // Валидация и сбор данных
                String name = validateNonEmpty(nameForm.getText(), "name");
                Coordinates coordinates = new Coordinates(
                        parseDouble(coordXForm.getText(), "coord_x"),
                        parseDouble(coordYForm.getText(), "coord_y")
                );
                LocationFrom from = createLocationFrom();
                LocationTo to = createLocationTo();
                double distance = parseDouble(distanceFrom.getText(), "distance");

                if (distance <= 0) throw new IllegalArgumentException(resources.getString("distance_positive"));

                // Создание модели и отправка запроса
                long key = Long.parseLong(keyForm.getText());
                RouteModel model = new RouteModel(name, coordinates, from, to, distance, user);
                var response = client.sendAndReceive(new Request(user, "insert_null", String.valueOf(key), model));

                mainPageController.printResponse(response.getMessage());
                if (response.getCollection() != null) {
                    mainPageController.RefreshObjectsTable(response.getCollection());
                }

            } catch ( IllegalArgumentException | EmptyFieldException e) {
                messageLabel.setText(e.getMessage());
            } catch (IOException | ClassNotFoundException e) {
                messageLabel.setText(resources.getString("connection_error"));
            }
        });
    }

    // Методы из AddWindowController
    private LocationFrom createLocationFrom() {
        double x = parseDouble(fromXForm.getText(), "from_x");
        double y = parseDouble(fromYForm.getText(), "from_y");
        int z = parseInt(fromZForm.getText(), "from_z");
        String name = validateNonEmpty(fromNameForm.getText(), "from_name");
        return new LocationFrom(x, y, z, name);
    }

    private LocationTo createLocationTo() {
        double x = parseDouble(toXForm.getText(), "to_x");
        double y = parseDouble(toYForm.getText(), "to_y");
        int z = parseInt(toZForm.getText(), "to_z");
        String name = validateNonEmpty(toNameForm.getText(), "to_name");
        return new LocationTo(x, y, z, name);
    }

    private String validateNonEmpty(String value, String resourceKey) {
        if (value == null || value.trim().isEmpty()) {
            throw new EmptyFieldException(resources.getString(resourceKey));
        }
        return value.trim();
    }

    private double parseDouble(String value, String resourceKey) {
        try {
            return Double.parseDouble(value.trim());
        } catch (Exception e) {
            throw new IllegalArgumentException(resources.getString("invalid_number") + ": " + resources.getString(resourceKey));
        }
    }

    private int parseInt(String value, String resourceKey) {
        try {
            return Integer.parseInt(value.trim());
        } catch (Exception e) {
            throw new IllegalArgumentException(resources.getString("invalid_integer") + ": " + resources.getString(resourceKey));
        }
    }

    public void setParent(MainPageController mainPageController) {
        this.mainPageController = mainPageController;
    }
}