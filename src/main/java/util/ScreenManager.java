package util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

public class ScreenManager {
    private static Stage mainStage;
    private static HashMap<String, Scene> screens = new HashMap<>();

    public static void setStage(Stage stage) {
        mainStage = stage;
    }
    public static void addScreen(String name, String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(ScreenManager.class.getResource(fxmlPath));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        screens.put(name, scene);
    }
    public static void setScreen(String name) {
        mainStage.setScene(screens.get(name));
        mainStage.show();
    }
}
