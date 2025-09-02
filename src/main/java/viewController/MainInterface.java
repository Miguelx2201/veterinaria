package viewController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;
import model.Veterinary;            // <-- importa tu clase real
import viewController.PetController;

public class MainInterface {

    @FXML private StackPane contentRoot;
    @FXML private Label statusLabel;
    @FXML private ProgressBar statusProgress;

    // Servicio de dominio: compártelo con las sub-vistas
    private final Veterinary veterinaria = new Veterinary(); // o recíbelo por setter si ya lo tienes creado

    @FXML
    private void initialize() {
        // Abre por defecto Mascotas
        goMascotas();
    }

    // -------- Navegación --------

    @FXML
    private void goMascotas() {
        // Ruta al FXML de mascotas. Ajusta si tu archivo se llama diferente:
        loadIntoCenter("/pets.fxml", controller -> {
            // Inyecta la instancia de Veterinary en el PetController
            if (controller instanceof PetController pc) {
                pc.setVeterinaria(veterinaria);
            }
        });
    }

    @FXML
    private void goPropietarios() {
        loadIntoCenter("/owners.fxml", controller -> {
            if (controller instanceof viewController.OwnersController c) c.setVeterinaria(veterinaria);
        });
    }

    @FXML
    private void goVeterinarios() {
        loadIntoCenter("/vets.fxml", controller -> {
            if (controller instanceof viewController.VetsController c) c.setVeterinaria(veterinaria);
        });
    }

    @FXML
    private void goCitas() {
        loadIntoCenter("/appointment.fxml", controller -> {
            if (controller instanceof viewController.AppointmentController c) c.setVeterinaria(veterinaria);
        });
    }


    // -------- Acciones globales --------

    @FXML private void guardar()  { /* si manejas persistencia global, hazlo aquí */ }
    @FXML private void salir()    { System.exit(0); }
    @FXML private void acercaDe() {
        new Alert(Alert.AlertType.INFORMATION,
                "Gestión Veterinaria\nHecho con JavaFX.\n© Miguel").showAndWait();
    }

    // -------- Utilidades internas --------

    /**
     * Carga un FXML en el centro y, si se proporciona, aplica una acción al controller cargado.
     */
    private void loadIntoCenter(String fxmlPath, java.util.function.Consumer<Object> controllerConsumer) {
        try {
            statusProgress.setVisible(true);
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();
            Object controller = loader.getController();

            if (controllerConsumer != null && controller != null) {
                controllerConsumer.accept(controller);
            }

            // Reemplaza el contenido central
            contentRoot.getChildren().setAll(view);
            setStatus("Cargado: " + fxmlPath);
        } catch (Exception ex) {
            setStatus("Error: " + ex.getMessage());
            new Alert(Alert.AlertType.ERROR, "No se pudo cargar " + fxmlPath + "\n" + ex).showAndWait();
        } finally {
            statusProgress.setVisible(false);
        }
    }

    private void setStatus(String msg) {
        if (statusLabel != null) statusLabel.setText(msg);
    }

    private void infoPendiente(String nombreVista) {
        new Alert(Alert.AlertType.INFORMATION, nombreVista + " aún no implementada.").showAndWait();
    }
}
