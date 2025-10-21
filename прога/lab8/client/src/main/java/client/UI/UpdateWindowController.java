package client.UI;

import CollectionObject.Coordinates;
import CollectionObject.LocationFrom;
import CollectionObject.LocationTo;
import CollectionObject.RouteModel;
import CollectionObject.Route;
import Exceptions.EmptyFieldException;
import Exceptions.NegativeFieldException;
import Network.Request;
import Network.Response;
import Network.User;
import Utils.Client;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Stack;

public class UpdateWindowController {
    private final Client client = ApplicationClient.getClient();
    private final User user = client.getUser();
    private Stack<Route> routes = MainPageController.vehicles;
    private Long id;
    private MainPageController mainPageController;

    // Элементы FXML
    @FXML private ResourceBundle resources;
    @FXML private URL location;
    @FXML private TextField nameForm;
    @FXML private Label messageLabel;
    @FXML private MenuButton idChoiceField;
    @FXML private Button updateSubmitButton;
    @FXML private TextField coordXForm;
    @FXML private TextField coordYForm;
    @FXML private TextField fromXForm;
    @FXML private TextField fromYForm;
    @FXML private TextField fromZForm;
    @FXML private TextField fromNameForm;
    @FXML private TextField toXForm;
    @FXML private TextField toYForm;
    @FXML private TextField toZForm;
    @FXML private TextField toNameForm;
    @FXML private TextField distanceForm;

    @FXML
    void initialize() {
        resources = ResourceBundle.getBundle("locales", MainPageController.locale);
        setLocaleText();

        // Если ID передан, заполнить форму
        if (id != null) {
            loadDataFromId();
        } else {
            // Заполнить выпадающий список ID
            populateIdMenu();
        }

        updateSubmitButton.setOnAction(e -> handleUpdate());
    }

    // Инициализация контроллера
    public void init(Long id, MainPageController parent) {
        this.id = id;
        this.mainPageController = parent;
        loadDataFromId(); // Загрузить данные сразу после установки ID
    }

    // Заполнение выпадающего списка ID
    private void populateIdMenu() {
        idChoiceField.getItems().clear();
        for (Route element : routes) {
            MenuItem menuItem = new MenuItem(element.getId().toString());
            menuItem.setOnAction(e -> {
                this.id = element.getId();
                idChoiceField.setText(id.toString());
                fillFormWithData(convertToRouteModel(element));
            });
            idChoiceField.getItems().add(menuItem);
        }
    }

    // Загрузка данных по ID
    private void loadDataFromId() {
        try {
            Optional<Route> routeOpt = routes.stream()
                    .filter(r -> r.getId().equals(id))
                    .findFirst();

            if (routeOpt.isPresent()) {
                Route route = routeOpt.get();
                fillFormWithData(convertToRouteModel(route));
                idChoiceField.setText(id.toString());
            } else {
                messageLabel.setText("Объект с ID " + id + " не найден!");
            }
        } catch (Exception e) {
            messageLabel.setText("Ошибка загрузки данных: " + e.getMessage());
        }
    }

    // Обработка обновления
    private void handleUpdate() {
        try {
            if (id == null) {
                throw new EmptyFieldException("ID не выбран");
            }

            RouteModel updatedRoute = validateAndCreateRoute();

            // Формируем команду как "update", ID передаем в strArgument
            String commandName = "update";
            String strArgument = String.valueOf(id); // ID как строковый аргумент
            Request request = new Request(user, commandName, strArgument, updatedRoute); // Обновленный запрос

            Response response = client.sendAndReceive(request);

            // Обновление коллекции и интерфейса
            if (response.getCollection() != null) {
                MainPageController.vehicles = response.getCollection();
                mainPageController.RefreshObjectsTable(MainPageController.vehicles);
            }

            mainPageController.printResponse(response.getMessage());
            closeWindow();
        } catch (Exception e) {
            messageLabel.setText("Ошибка: " + e.getMessage());
        }
    }
    // Закрытие окна
    private void closeWindow() {
        Stage stage = (Stage) updateSubmitButton.getScene().getWindow();
        stage.close();
    }

    // Конвертация Route -> RouteModel с установкой ID
    private RouteModel convertToRouteModel(Route route) {
        RouteModel model = new RouteModel(
                route.getName(),
                route.getCoordinates(),
                route.getFrom(),
                route.getTo(),
                route.getDistance(),
                user
        );
        model.setId(route.getId()); // Установка ID
        return model;
    }

    // Валидация и создание модели
    private RouteModel validateAndCreateRoute() {
        String name = validateField(nameForm.getText(), "name");
        Coordinates coordinates = new Coordinates(
                parseDouble(coordXForm.getText(), "coord_x"),
                parseDouble(coordYForm.getText(), "coord_y")
        );
        LocationFrom from = new LocationFrom(
                parseDouble(fromXForm.getText(), "from_x"),
                parseDouble(fromYForm.getText(), "from_y"),
                parseInt(fromZForm.getText(), "from_z"),
                validateField(fromNameForm.getText(), "from_name")
        );
        LocationTo to = new LocationTo(
                parseDouble(toXForm.getText(), "to_x"),
                parseDouble(toYForm.getText(), "to_y"),
                parseInt(toZForm.getText(), "to_z"),
                validateField(toNameForm.getText(), "to_name")
        );
        double distance = parseDouble(distanceForm.getText(), "distance");
        if (distance <= 0) throw new NegativeFieldException("Дистанция должна быть больше 0");

        RouteModel model = new RouteModel(name, coordinates, from, to, distance, user);
        model.setId(id);
        return model;
    }

    // Вспомогательные методы валидации
    private String validateField(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new EmptyFieldException("Поле '" + fieldName + "' не может быть пустым");
        }
        return value.trim();
    }

    private double parseDouble(String value, String fieldName) {
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Некорректное значение в поле '" + fieldName + "'");
        }
    }

    private int parseInt(String value, String fieldName) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Некорректное значение в поле '" + fieldName + "'");
        }
    }

    // Заполнение формы данными
    private void fillFormWithData(RouteModel route) {
        nameForm.setText(route.getName());
        coordXForm.setText(String.valueOf(route.getCoordinates().getX()));
        coordYForm.setText(String.valueOf(route.getCoordinates().getY()));
        fromXForm.setText(String.valueOf(route.getFrom().getX()));
        fromYForm.setText(String.valueOf(route.getFrom().getY()));
        fromZForm.setText(String.valueOf(route.getFrom().getZ()));
        fromNameForm.setText(route.getFrom().getName());
        toXForm.setText(String.valueOf(route.getTo().getX()));
        toYForm.setText(String.valueOf(route.getTo().getY()));
        toZForm.setText(String.valueOf(route.getTo().getZ()));
        toNameForm.setText(route.getTo().getName());
        distanceForm.setText(String.valueOf(route.getDistance()));
    }

    // Локализация
    public void setLocaleText() {
        nameForm.setPromptText(resources.getString("name"));
        distanceForm.setPromptText(resources.getString("distance"));
        updateSubmitButton.setText(resources.getString("submit"));

        coordXForm.setPromptText(resources.getString("coord_x"));
        coordYForm.setPromptText(resources.getString("coord_y"));

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