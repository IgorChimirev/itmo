package client.UI;

import CollectionObject.Coordinates;
import CollectionObject.LocationFrom;
import CollectionObject.LocationTo;
import CollectionObject.RouteModel;
import Exceptions.EmptyFieldException;
import Network.Request;
import Network.User;
import Utils.Client;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class AddWindowController {

    private Client client = ApplicationClient.getClient();
    private User user = client.getUser();
    private MainPageController mainPageController;
    private static Locale locale = MainPageController.locale;

    @FXML private ResourceBundle resources;
    @FXML private URL location;
    @FXML private AnchorPane addPane;
    @FXML private Button addSubmitButton;
    @FXML private TextField nameForm;
    @FXML private Label messageLabel;

    // Coordinates fields
    @FXML private TextField coordXForm;
    @FXML private TextField coordYForm;

    // LocationFrom fields
    @FXML private TextField fromXForm;
    @FXML private TextField fromYForm;
    @FXML private TextField fromZForm;
    @FXML private TextField fromNameForm;

    // LocationTo fields
    @FXML private TextField toXForm;
    @FXML private TextField toYForm;
    @FXML private TextField toZForm;
    @FXML private TextField toNameForm;

    // Distance field
    @FXML private TextField distanceFrom;

    @FXML
    void initialize() {
        try {
            resources = ResourceBundle.getBundle("locales", locale != null ? locale : Locale.getDefault());
        } catch (Exception e) {
            resources = ResourceBundle.getBundle("locales", Locale.ROOT);
        }
        setLocaleText();

        addSubmitButton.setOnAction(actionEvent -> {
            try {
                String name = validateNonEmpty(nameForm.getText(), "name");

                // Парсинг координат маршрута (если они нужны в БД)
                double coordX = parseDouble(coordXForm.getText(), "coord_x");
                double coordY = parseDouble(coordYForm.getText(), "coord_y");
                Coordinates coordinates = new Coordinates(coordX, coordY);

                LocationFrom from = createLocationFrom();
                LocationTo to = createLocationTo();

                double distance = parseDouble(distanceFrom.getText(), "distance");
                if (distance <= 0) throw new IllegalArgumentException(resources.getString("distance_positive"));

                RouteModel route = new RouteModel(name, coordinates, from, to, distance, user);
                var response = client.sendAndReceive(new Request(user, "add", "", route));
                mainPageController.printResponse(response.getMessage());
                mainPageController.refreshData();

            } catch (IllegalArgumentException | EmptyFieldException e) {
                messageLabel.setText(e.getMessage());
            } catch (IOException | ClassNotFoundException e) {
                messageLabel.setText(resources.getString("connection_error"));
            }
        });
    }

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

    public void setLocaleText() {
        nameForm.setPromptText(resources.getString("name"));
        coordXForm.setPromptText(resources.getString("coord_x"));
        coordYForm.setPromptText(resources.getString("coord_y"));
        distanceFrom.setPromptText(resources.getString("distance"));
        addSubmitButton.setText(resources.getString("submit"));

        fromXForm.setPromptText(resources.getString("from_x"));
        fromYForm.setPromptText(resources.getString("from_y"));
        fromZForm.setPromptText(resources.getString("from_z"));
        fromNameForm.setPromptText(resources.getString("from_name"));

        toXForm.setPromptText(resources.getString("to_x"));
        toYForm.setPromptText(resources.getString("to_y"));
        toZForm.setPromptText(resources.getString("to_z"));
        toNameForm.setPromptText(resources.getString("to_name"));
    }

    public void setParent(MainPageController mainPageController) {
        this.mainPageController = mainPageController;
    }
}