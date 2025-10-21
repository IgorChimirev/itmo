package client.UI;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ResourceBundle;

public class ApplicationStarter extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(
                ApplicationStarter.class.getResource("/client/UI/LoginRegisterPage.fxml")
        );
        fxmlLoader.setResources(ResourceBundle.getBundle("locales"));


        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("ydei menia pogaltista");
        stage.setScene(scene);
        stage.setResizable(false);


        Platform.runLater(() -> stage.show());
    }

    public static void main(String[] args) {

        System.setProperty("glass.accessible.force", "false");
        launch(args);
    }
}