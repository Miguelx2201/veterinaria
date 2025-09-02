package viewController;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MainInterface {
    @FXML private Button botonGestionarCitas;
    @FXML private Button botonGestionarMascotas;

    @FXML private void gestionCitas() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AppointmentView.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Gestión de Citas");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML private void gestionMascotas() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/PetView.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Gestión de Mascotas");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
