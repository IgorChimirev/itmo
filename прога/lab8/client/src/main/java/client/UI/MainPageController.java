package client.UI;
import CollectionObject.LocationFrom;
import CollectionObject.LocationTo;
import CollectionObject.Route;
import Network.Request;
import Network.Response;
import Network.User;
import Utils.Client;
import CollectionObject.Coordinates;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.InvalidPathException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MainPageController {

    private final Client client = ApplicationClient.getClient();

    private final User user = client.getUser();


    @FXML
    public Label welcomeLabel;

    private CountGreaterThanEnginePowerWindowController CGTEPController;

    private FilterStartsWithNameWindowController FSWNController;
    private InsertNullWindowController insertNullWindowController;

    private AddWindowController addWindowController;

    private UpdateWindowController updateWindowController;

    private RemoveByIdWindowController removeByIdWindowController;

    private Timeline pingServerTimeline; // Делаем поле доступным для управления
    private boolean isManualUpdate = false;
    private RemoveGreaterWindowController removeGreaterWindowController;

    public static Stack<Route> vehicles;

    private static Stack<Route> vehiclesCopy;

    {
        vehiclesCopy = new Stack<>();
        try {
            vehicles = client.sendAndReceive(new Request(user, "ping", "")).getCollection();
            vehiclesCopy.addAll(vehicles);
        } catch (IOException | ClassNotFoundException e) {
            printResponse("Ошибка подключения: " + e.getMessage());
        }
    }

    private ObservableList<Route> collection = FXCollections.observableList(vehicles);

    protected static Locale locale = new Locale("ru", "RU");

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableColumn<Route, Long> idColumn;


    @FXML
    private TableColumn<Route, String> nameColumn;


    @FXML
    private Separator CanvasSep;

    @FXML
    private Tab CanvasTab;

    @FXML
    private AnchorPane CanvasTabAnchor;



    @FXML
    private TextArea InfoArea;

    @FXML
    private AnchorPane MainPageAnchor;

    @FXML
    private Tab MainTab;

    @FXML
    private Pane ObjectCanvas;

    @FXML
    private TableView<Route> ObjectTable;

    @FXML
    private TabPane TabPanel;

    @FXML
    private MenuItem addButton;

    @FXML
    private ImageView avatar;

    @FXML
    private MenuItem clearButton;

    @FXML
    private Menu commandMenu;


    @FXML
    private TableColumn<Route, String> creatorColumn;

    @FXML
    private TableColumn<Route, Date> dateColumn;

    @FXML
    private Menu executeScriptMenu;

    @FXML
    private TableColumn<Route, Double> distanceColumn;

    @FXML
    private MenuItem helpButton;
    @FXML
    private MenuItem filterLessDistanceButton;

    @FXML
    private Button exitButton;
    @FXML
    private MenuItem insertNullButton;

    @FXML
    private MenuItem randomButton;
    @FXML
    private MenuItem clearAllButton;
    @FXML
    private MenuItem replaceIfLowerButton;

    @FXML
    private MenuItem historyButton;
    @FXML
    private TextField userSearchField;

    @FXML
    private MenuItem infoButton;

    @FXML
    private AnchorPane mainAnchor;

    @FXML
    private Separator mainPageSep;
    @FXML
    private Label enterUsernameLabel;

    @FXML
    private Button searchUserButton;
    @FXML
    private MenuBar menuBar;
    @FXML
    private AnchorPane filterPane;

    @FXML
    private MenuItem nameFilterButton;

    @FXML
    private Label prikolLabel;

    @FXML
    private TableColumn<Route, String> fromColumn;

    @FXML
    private TableColumn<Route, String> toColumn;
    @FXML
    private MenuItem removeByIdButton;
    @FXML
    private MenuItem removeKeyButton;
    @FXML
    private MenuItem removeGreaterButton;



    @FXML
    private TextArea responseArea;

    @FXML
    private MenuItem scriptButton;

    @FXML
    private MenuItem showButton;

    @FXML
    private MenuItem updateButton;

    @FXML
    private TextField usernameBar;

    @FXML
    private TableColumn<Route, Double> coordXColumn;

    @FXML
    private TableColumn<Route, Double> coordYColumn;

    private Window mainWindow;
    @FXML
    private ImageView gifImageView;
    @FXML
    private Button russianButton;

    @FXML
    private Button slovakianButton;

    @FXML
    private Button spainButton;

    @FXML
    private Button swedenButton;


    @FXML
    void initialize() {
        Image image = new Image("avatar.jpg");
        avatar.setImage(image);
        usernameBar.setText(user.getUsername());
        initializeTable(collection);
        pingServerTimeline = getTimeline(); // Инициализируем поле
        pingServerTimeline.play();
        Timeline pingServerTimeline = getTimeline();
        pingServerTimeline.play();

        russianButton.setOnAction(actionEvent -> {
            locale = new Locale("ru", "RU");
            setLocaleText();
        });

        slovakianButton.setOnAction(actionEvent -> {
            locale = new Locale("et", "ET");
            setLocaleText();
        });

        swedenButton.setOnAction(actionEvent -> {
            locale = new Locale("es", "ES");
            setLocaleText();
        });

        spainButton.setOnAction(actionEvent -> {
            locale = new Locale("ca", "CA");
            setLocaleText();
        });

        filterLessDistanceButton.setOnAction(actionEvent -> {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(FilterLessThanDistanceWindowController.class.getResource("FilterLessThanDistanceWindow.fxml"));
            fxmlLoader.setResources(ResourceBundle.getBundle("locales"));

            try {
                Parent root = fxmlLoader.load();
                Stage stage = new Stage();
                stage.setTitle(resources.getString("filter_distance_window_title"));
                stage.setScene(new Scene(root));
                stage.setResizable(false);
                stage.show();

                FilterLessThanDistanceWindowController controller = fxmlLoader.getController();
                controller.setParent(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        insertNullButton.setOnAction(actionEvent -> {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(InsertNullWindowController.class.getResource("InsertNullWindow.fxml"));
            fxmlLoader.setResources(ResourceBundle.getBundle("locales"));

            try {
                fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Parent root = fxmlLoader.getRoot();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("InsertNullWindow.fxml".replace(".fxml", ""));
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

            insertNullWindowController = fxmlLoader.getController();
            insertNullWindowController.setParent(this);
        });
        addButton.setOnAction(actionEvent -> {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(AddWindowController.class.getResource("AddWindow.fxml"));
            fxmlLoader.setResources(ResourceBundle.getBundle("locales"));

            try {
                fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Parent root = fxmlLoader.getRoot();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("AddWindow.fxml".replace(".fxml", ""));
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

            addWindowController = fxmlLoader.getController();
            addWindowController.setParent(this);
        });
        clearAllButton.setOnAction(actionEvent -> {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(ClearAllWindowController.class.getResource("ClearAllWindow.fxml"));
            fxmlLoader.setResources(ResourceBundle.getBundle("locales"));

            try {
                fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Parent root = fxmlLoader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();

            ClearAllWindowController controller = fxmlLoader.getController();
            controller.setParent(this);
        });

        updateButton.setOnAction(actionEvent -> {
            // Получаем выбранный элемент из таблицы
            Route selectedRoute = ObjectTable.getSelectionModel().getSelectedItem();

            if (selectedRoute != null) {
                // Передаем ID выбранного элемента в метод
                loadUpdateWindow(selectedRoute.getId());
            } else {
                printResponse("Ошибка: элемент не выбран. Выберите объект из таблицы.");
            }
        });


        removeByIdButton.setOnAction(actionEvent -> {
            loadRemoveByIdWindow();
        });
        removeKeyButton.setOnAction(actionEvent -> {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(RemoveKeyWindowController.class.getResource("RemoveKeyWindow.fxml"));
            fxmlLoader.setResources(ResourceBundle.getBundle("locales"));

            try {
                fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Parent root = fxmlLoader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();

            RemoveKeyWindowController controller = fxmlLoader.getController();
            controller.setParent(this);
        });


        removeGreaterButton.setOnAction(actionEvent -> {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(RemoveGreaterWindowController.class.getResource("RemoveGreaterWindow.fxml"));
            fxmlLoader.setResources(ResourceBundle.getBundle("locales"));

            try {
                fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Parent root = fxmlLoader.getRoot();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("RemoveGreaterWindow.fxml".replace(".fxml", ""));
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

            removeGreaterWindowController = fxmlLoader.getController();
            removeGreaterWindowController.setParent(this);
        });




        nameFilterButton.setOnAction(actionEvent -> {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(FilterStartsWithNameWindowController.class.getResource("FilterStartsWithNameWindow.fxml"));
            fxmlLoader.setResources(ResourceBundle.getBundle("locales"));

            try {
                fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Parent root = fxmlLoader.getRoot();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("FilterStartsWithNameWindow.fxml".replace(".fxml", ""));
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

            FSWNController = fxmlLoader.getController();
            FSWNController.setParent(this);
        });

        exitButton.setOnAction(actionEvent -> System.exit(0));

        helpButton.setOnAction(actionEvent -> processUserCommand("help"));

        showButton.setOnAction(actionEvent -> processUserCommand("show"));
        historyButton.setOnAction(actionEvent -> processUserCommand("history"));
        infoButton.setOnAction(actionEvent -> processUserCommand("info"));
        clearButton.setOnAction(actionEvent -> processUserCommand("clear"));
        nameFilterButton.setOnAction(actionEvent -> {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(FilterStartsWithNameWindowController.class.getResource("FilterStartsWithNameWindow.fxml"));
            fxmlLoader.setResources(ResourceBundle.getBundle("locales"));

            try {
                Parent root = fxmlLoader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setResizable(false);
                stage.show();

                FilterStartsWithNameWindowController controller = fxmlLoader.getController();
                controller.setParent(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        // В методе initialize():
        replaceIfLowerButton.setOnAction(actionEvent -> openReplaceIfLowerWindow());

// В методе initialize():
        replaceIfLowerButton.setOnAction(actionEvent -> openReplaceIfLowerWindow());

// Метод открытия окна:
        replaceIfLowerButton.setOnAction(actionEvent -> openReplaceIfLowerWindow());


        filterLessDistanceButton.setOnAction(actionEvent -> {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(FilterLessThanDistanceWindowController.class.getResource("FilterLessThanDistanceWindow.fxml"));
            fxmlLoader.setResources(ResourceBundle.getBundle("locales"));

            try {
                Parent root = fxmlLoader.load();
                Stage stage = new Stage();
                stage.setTitle(resources.getString("filter_distance_window_title"));
                stage.setScene(new Scene(root));
                stage.setResizable(false);
                stage.show();

                FilterLessThanDistanceWindowController controller = fxmlLoader.getController();
                controller.setParent(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        clearAllButton.setOnAction(actionEvent -> processUserCommand("clear_all"));
        replaceIfLowerButton.setOnAction(actionEvent -> openReplaceIfLowerWindow());

        scriptButton.setOnAction(actionEvent -> {
            var fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("script files", "*.txt"));
            File scriptFile = fileChooser.showOpenDialog(mainWindow);

            Thread scriptThread = new Thread(() -> {
                try {
                    pingServerTimeline.stop();
                    client.executeScript(scriptFile.toString());
                    responseArea.setPromptText("Script successfully done!!!");
                    pingServerTimeline.play();


                }catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (FileNotFoundException e){
                    responseArea.setText("Файл " + scriptFile + " не найден");
                } catch (NoSuchElementException e){
                    responseArea.setText("Файл " + scriptFile + " пуст");
                } catch (IllegalStateException e){
                    responseArea.setText("Непредвиденная ошибка");
                } catch (SecurityException e){
                    responseArea.setText("Недостаточно прав для чтения файла " + scriptFile);
                } catch (InvalidPathException e){
                    responseArea.setText("Проверьте путь к файлу. В нём не должно быть лишних символов");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            scriptThread.start();
            responseArea.setPromptText("Script is running...");

        });

        ObjectTable.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2) {
                Route selectedRoute = ObjectTable.getSelectionModel().getSelectedItem();
                if (selectedRoute != null) {
                    loadUpdateWindow(selectedRoute.getId());
                }
            }
        });


        showButton.setOnAction(actionEvent -> processUserCommand("show"));

        historyButton.setOnAction(actionEvent -> processUserCommand("history"));

        infoButton.setOnAction(actionEvent -> processUserCommand("info"));

        clearButton.setOnAction(actionEvent -> processUserCommand("clear"));

        idColumn.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            ObjectTable.setItems(collection.stream()
                    .sorted(Comparator.comparing(Route::getId))
                    .collect(Collectors.toCollection(() -> FXCollections.observableList(collection)))
            );
            ObjectTable.refresh();
        });

        nameColumn.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            ObjectTable.setItems(collection.stream()
                    .sorted(Comparator.comparing(Route::getName))
                    .collect(Collectors.toCollection(() -> FXCollections.observableList(collection)))
            );
            ObjectTable.refresh();
        });

        coordXColumn.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            ObjectTable.setItems(collection.stream()
                    .sorted(Comparator.comparing(vehicle -> vehicle.getCoordinates().getX()))
                    .collect(Collectors.toCollection(() -> FXCollections.observableList(collection)))
            );
            ObjectTable.refresh();
        });
        // Для randomButton
        randomButton.setOnAction(actionEvent -> {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(RandomWindowController.class.getResource("RandomWindow.fxml"));
            fxmlLoader.setResources(ResourceBundle.getBundle("locales"));

            try {
                Parent root = fxmlLoader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setResizable(false);
                stage.show();

                RandomWindowController controller = fxmlLoader.getController();
                controller.setParent(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        removeKeyButton.setOnAction(actionEvent -> {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(RemoveKeyWindowController.class.getResource("RemoveKeyWindow.fxml"));
            fxmlLoader.setResources(ResourceBundle.getBundle("locales"));

            try {
                Parent root = fxmlLoader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setResizable(false);
                stage.show();

                RemoveKeyWindowController controller = fxmlLoader.getController();
                controller.setParent(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        coordYColumn.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            ObjectTable.setItems(collection.stream()
                    .sorted(Comparator.comparing(vehicle -> vehicle.getCoordinates().getY()))
                    .collect(Collectors.toCollection(() -> FXCollections.observableList(collection)))
            );
            ObjectTable.refresh();
        });

        dateColumn.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            ObjectTable.setItems(collection.stream()
                    .sorted(Comparator.comparing(Route::getCreationDate))
                    .collect(Collectors.toCollection(() -> FXCollections.observableList(collection)))
            );
            ObjectTable.refresh();
        });
        idColumn.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> sortTable(Comparator.comparing(Route::getId)));
        nameColumn.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> sortTable(Comparator.comparing(Route::getName)));
        coordXColumn.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> sortTable(Comparator.comparing(v -> v.getCoordinates().getX())));
        coordYColumn.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> sortTable(Comparator.comparing(v -> v.getCoordinates().getY())));
        dateColumn.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> sortTable(Comparator.comparing(Route::getCreationDate)));
        creatorColumn.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> sortTable(Comparator.comparing(Route::getCreator)));
        distanceColumn.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> sortTable(Comparator.comparing(Route::getDistance)));



        creatorColumn.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            ObjectTable.setItems(collection.stream()
                    .sorted(Comparator.comparing(Route::getCreator))
                    .collect(Collectors.toCollection(() -> FXCollections.observableList(collection)))
            );
            ObjectTable.refresh();
        });

    }
    // Метод для сортировки таблицы
    private void sortTable(Comparator<Route> comparator) {
        ObjectTable.setItems(collection.stream()
                .sorted(comparator)
                .collect(Collectors.toCollection(FXCollections::observableArrayList))
        );
        ObjectTable.refresh();
    }

    @FXML
    private void handleSearchUser() {
        String username = userSearchField.getText().trim();
        if (username.isEmpty()) {
            printResponse("Введите имя пользователя!");
            return;
        }

        try {
            Request request = new Request(user, "show_user_elements", username);
            Response response = client.sendAndReceive(request);

            if (response.getCollection() != null && !response.getCollection().isEmpty()) {
                RefreshObjectsTable(response.getCollection());
                printResponse("Элементы пользователя '" + username + "' загружены.");
            } else {
                printResponse("У пользователя '" + username + "' нет элементов.");
            }
        } catch (Exception e) {
            printResponse("Ошибка: " + e.getMessage());
        }
    }

    @FXML

    // Метод для открытия окна replace_if_lower
    private void openReplaceIfLowerWindow() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(ReplaceIfLowerController.class.getResource("ReplaceIfLowerWindow.fxml"));
        fxmlLoader.setResources(ResourceBundle.getBundle("locales", locale));
        try {
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
            ReplaceIfLowerController controller = fxmlLoader.getController();
            controller.setParent(this); // Передача ссылки на MainPageController
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public ResourceBundle getResourcesBundle() {
        return resources;
    }

    private void setLocaleText() {
        resources = ResourceBundle.getBundle("locales", locale);
        enterUsernameLabel.setText(resources.getString("enter_username")); // Обновление надписи
        searchUserButton.setText(resources.getString("search"));
        MainTab.setText(resources.getString("main_page"));
        CanvasTab.setText(resources.getString("objects_tab"));
        coordXColumn.setText(resources.getString("coordinate_x"));
        coordYColumn.setText(resources.getString("coordinate_y"));
        nameColumn.setText(resources.getString("name"));
        dateColumn.setText(resources.getString("creation_date"));
        creatorColumn.setText(resources.getString("creator"));

        welcomeLabel.setText(resources.getString("greeting_message"));

        idColumn.setText(resources.getString("id"));
        fromColumn.setText(resources.getString("from"));
        toColumn.setText(resources.getString("to"));
        commandMenu.setText(resources.getString("commands"));
        executeScriptMenu.setText(resources.getString("executeScript"));
        exitButton.setText(resources.getString("exit"));

        InfoArea.setPromptText(resources.getString("item_info"));
        responseArea.setPromptText(resources.getString("response"));
    }

    private void loadRemoveByIdWindow() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(RemoveByIdWindowController.class.getResource("RemoveByIdWindow.fxml"));
        fxmlLoader.setResources(ResourceBundle.getBundle("locales"));

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Parent root = fxmlLoader.getRoot();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("RemoveByIdWindow.fxml".replace(".fxml", ""));
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        removeByIdWindowController = fxmlLoader.getController();
        removeByIdWindowController.setParent(this);
    }
    public void refreshData() {
        try {
            var response = client.sendAndReceive(new Request(user, "show", ""));
            // Обновление таблицы данными из response
        } catch (Exception e) {
            // Обработка ошибок
        }
    }
    private Timeline getTimeline() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(20), event -> {
            if (!isManualUpdate) { // Не обновляем если идет ручное изменение
                try {
                    Stack<Route> newVehicles = client.sendAndReceive(new Request(user, "ping", "")).getCollection();
                    if (!newVehicles.equals(vehicles)) {
                        Platform.runLater(() -> RefreshObjectsTable(newVehicles));
                    }
                } catch (Exception e) {
                    printResponse("Ошибка обновления: " + e.getMessage());
                }
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        return timeline;
    }

    public void RefreshObjectsTable(Stack<Route> newVehicles) {
        // Сравниваем содержимое коллекций
        if (!newVehicles.equals(vehicles)) {
            vehicles = newVehicles;
            vehiclesCopy.clear();
            vehiclesCopy.addAll(newVehicles);

            Platform.runLater(() -> {
                ObjectCanvas.getChildren().clear();
                drawVehicles(vehicles);

                collection = FXCollections.observableList(vehicles);
                ObjectTable.setItems(collection);
                ObjectTable.refresh();
            });
        }
    }

    public void processUserCommand(String command) {
        try {
            isManualUpdate = true; // Блокируем автообновление
            pingServerTimeline.pause(); // Приостанавливаем таймер

            Request request = new Request(user, command, "");
            var response = client.sendAndReceive(request);

            printResponse(response.getMessage());

            if (response.getCollection() != null) {
                RefreshObjectsTable(response.getCollection());
            }

        } catch (Exception e) {
            printResponse("Ошибка выполнения команды: " + e.getMessage());
        } finally {
            isManualUpdate = false;
            pingServerTimeline.play(); // Возобновляем таймер
        }
    }

    public void printResponse(String message) {
        responseArea.setText(message);
    }

    private void drawVehicles(Stack<Route> vehicles) {
        ObjectCanvas.getChildren().clear();
        if (vehicles.isEmpty()) return;

        // 1. Определение размеров холста
        final double canvasWidth = ObjectCanvas.getWidth() > 0 ? ObjectCanvas.getWidth() : 1200.0;
        final double canvasHeight = ObjectCanvas.getHeight() > 0 ? ObjectCanvas.getHeight() : 320.0;

        // 2. Поиск min/max координат
        double minX = Double.MAX_VALUE, maxX = Double.MIN_VALUE;
        double minY = Double.MAX_VALUE, maxY = Double.MIN_VALUE;

        for (Route element : vehicles) {
            if (!validateCoordinates(element)) continue;

            minX = findMin(minX, element.getFrom().getX(), element.getCoordinates().getX(), element.getTo().getX());
            maxX = findMax(maxX, element.getFrom().getX(), element.getCoordinates().getX(), element.getTo().getX());
            minY = findMin(minY, element.getFrom().getY(), element.getCoordinates().getY(), element.getTo().getY());
            maxY = findMax(maxY, element.getFrom().getY(), element.getCoordinates().getY(), element.getTo().getY());
        }

        // 3. Расчет масштаба
        final double finalMinX = minX, finalMinY = minY;
        final double scale = calculateScale(minX, maxX, minY, maxY, canvasWidth, canvasHeight);

        // 4. Отрисовка элементов
        for (Route element : vehicles) {
            if (!validateCoordinates(element)) continue;

            // Преобразование координат
            final double[] points = calculateNormalizedPoints(element, finalMinX, finalMinY, scale, canvasWidth, canvasHeight);

            // Создание графических элементов
            Color color = getElementColor(element);
            createAndDrawShapes(element, points, color);
        }
    }

    // Вспомогательные методы
    private boolean validateCoordinates(Route element) {
        return element.getCoordinates() != null
                && element.getFrom() != null
                && element.getTo() != null;
    }

    private double findMin(double current, double... values) {
        return Arrays.stream(values).reduce(current, Math::min);
    }

    private double findMax(double current, double... values) {
        return Arrays.stream(values).reduce(current, Math::max);
    }

    private double calculateScale(double minX, double maxX, double minY, double maxY, double canvasWidth, double canvasHeight) {
        double deltaX = maxX - minX;
        double deltaY = maxY - minY;
        double scaleX = (canvasWidth * 0.9) / (deltaX != 0 ? deltaX : 1);
        double scaleY = (canvasHeight * 0.9) / (deltaY != 0 ? deltaY : 1);
        return Math.min(scaleX, scaleY);
    }

    private double[] calculateNormalizedPoints(Route element, double minX, double minY, double scale, double canvasWidth, double canvasHeight) {
        Function<Double, Double> normalizeX = x -> (x - minX) * scale + (canvasWidth * 0.05);
        Function<Double, Double> normalizeY = y -> canvasHeight - ((y - minY) * scale + (canvasHeight * 0.05));

        return new double[]{
                normalizeX.apply(element.getFrom().getX()),
                normalizeY.apply(element.getFrom().getY()),
                normalizeX.apply(element.getCoordinates().getX()),
                normalizeY.apply(element.getCoordinates().getY()),
                normalizeX.apply(element.getTo().getX()),
                normalizeY.apply(element.getTo().getY())
        };
    }

    private Color getElementColor(Route element) {
        return element.getCreator().equals(user.getUsername())
                ? Color.DEEPSKYBLUE
                : Color.ORANGERED;
    }

    private void createAndDrawShapes(Route element, double[] points, Color color) {
        // Маркеры для точек
        Shape fromMarker = createSquare(points[0], points[1], 8, color);      // Квадрат для From
        Shape currentMarker = createCircle(points[2], points[3], 6, color);   // Круг для Current
        Shape toMarker = createTriangle(points[4], points[5], 8, color);      // Треугольник для To

        // Линии
        Line fromToCurrent = new Line(points[0], points[1], points[2], points[3]);
        fromToCurrent.setStroke(color.deriveColor(0, 1, 1, 0.7));
        fromToCurrent.getStrokeDashArray().addAll(5d, 5d);

        Line currentToTo = new Line(points[2], points[3], points[4], points[5]);
        currentToTo.setStroke(color.deriveColor(0, 1, 1, 0.7));

        // Текст
        Text idText = new Text(String.valueOf(element.getId()));
        idText.setFill(Color.WHITE);
        idText.setFont(Font.font(10));
        idText.setX(points[2] - 5);
        idText.setY(points[3] + 4);

        // Обработчик событий
        EventHandler<MouseEvent> handler = createEventHandler(element);

        // Добавление элементов
        addEventHandlers(handler, fromMarker, currentMarker, toMarker, fromToCurrent, currentToTo);
        ObjectCanvas.getChildren().addAll(
                fromToCurrent, currentToTo,
                fromMarker, currentMarker, toMarker,
                idText
        );
    }

    // Методы создания фигур
    private Rectangle createSquare(double x, double y, double size, Color color) {
        Rectangle square = new Rectangle(size, size);
        square.setX(x - size/2);
        square.setY(y - size/2);
        square.setFill(color);
        square.setStroke(Color.WHITE);
        return square;
    }

    private Circle createCircle(double x, double y, double radius, Color color) {
        Circle circle = new Circle(radius);
        circle.setCenterX(x);
        circle.setCenterY(y);
        circle.setFill(color);
        circle.setStroke(Color.WHITE);
        return circle;
    }

    private Polygon createTriangle(double x, double y, double size, Color color) {
        Polygon triangle = new Polygon();
        triangle.getPoints().addAll(
                x, y - size,
                x - size/2, y + size/2,
                x + size/2, y + size/2
        );
        triangle.setFill(color);
        triangle.setStroke(Color.WHITE);
        return triangle;
    }

    // Обработчики событий
    private EventHandler<MouseEvent> createEventHandler(Route element) {
        return event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                processUserCommand("remove_by_id " + element.getId());
            } else if (event.getClickCount() == 2) {
                // Передача ID элемента в метод обновления
                loadUpdateWindow(element.getId());
            } else {
                showElementInfo(element);
            }
        };
    }

    private void addEventHandlers(EventHandler<MouseEvent> handler, Node... nodes) {
        for (Node node : nodes) {
            node.setOnMouseClicked(handler);
        }
    }

    private void showElementInfo(Route element) {
        String info = String.format(
                "ID: %d\nОткуда: %s\nКуда: %s\nДистанция: %.2f\nСоздатель: %s",
                element.getId(),
                element.getFrom().getName(),
                element.getTo().getName(),
                element.getDistance(),
                element.getCreator()
        );
        InfoArea.setText(info);
    }

    private Text getCircleText(Route element, float x, double y) {
        var text = new Text(x - 13, y + 4, element.getId().toString());

        text.setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 2) {
                        loadUpdateWindow(element.getId());
                }
                // Обновляем InfoArea с данными из Route
                InfoArea.setText(
                        "ID: " + element.getId() + "\n" +
                                "Name: " + element.getName() + "\n" +
                                "Coordinates: " + element.getCoordinates() + "\n" +
                                "From: " + element.getFrom() + "\n" +
                                "To: " + element.getTo() + "\n" +
                                "Distance: " + element.getDistance() + "\n" +
                                "Creator: " + element.getCreator()
                );
            }
            if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
                loadRemoveByIdWindow();
            }
        });
        return text;
    }

    private void processTextNode(Text textNode) {
        try {
            Long id = Long.parseLong(textNode.getText());
            loadUpdateWindow(id); // Передаем ID напрямую
        } catch (NumberFormatException e) {
            printResponse("Ошибка формата ID");
        }
    }

    // Новый метод для открытия окна с передачей ID
    private void loadUpdateWindow(Long id) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(ResourceBundle.getBundle("locales")); // Явная установка ресурсов
            loader.setLocation(getClass().getResource("UpdateWindow.fxml"));
            Parent root = loader.load();
            UpdateWindowController controller = loader.getController();
            controller.init(id, this);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void initializeTable(ObservableList<Route> collection) {
        // Инициализация основных колонок
        idColumn.setCellValueFactory(cellData -> new SimpleLongProperty(cellData.getValue().getId()).asObject());
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        coordXColumn.setCellValueFactory(cellData ->
                new SimpleDoubleProperty(cellData.getValue().getCoordinates().getX()).asObject()
        );
        coordYColumn.setCellValueFactory(cellData ->
                new SimpleDoubleProperty(cellData.getValue().getCoordinates().getY()).asObject()
        );
        dateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCreationDate()));
        distanceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getDistance()).asObject());
        creatorColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCreator()));

        // Колонки для вложенных свойств с проверкой на null
        fromColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getFrom() != null
                                ? cellData.getValue().getFrom().getName()
                                : "N/A"
                )
        );

        toColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getTo() != null
                                ? cellData.getValue().getTo().getName()
                                : "N/A"
                )
        );

        // Добавление колонок только если они не были добавлены ранее
        if (!ObjectTable.getColumns().containsAll(List.of(fromColumn, toColumn))) {
            ObjectTable.getColumns().addAll(fromColumn, toColumn);
        }
    }

}