package app;

import javafx.application.Application;
import javafx.stage.Stage;
import util.ScreenManager;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        ScreenManager.setStage(stage);
        ScreenManager.addScreen("main", "/MainInterface.fxml");
        ScreenManager.setScreen("main");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
