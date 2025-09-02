package viewController;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import model.Appointment;
import model.AppointmentStatus;

import java.time.LocalDate;

public class AppointmentController {

    @FXML private TableView<Appointment> tableAppointments;
    @FXML private TableColumn<Appointment, String> colId;
    @FXML private TableColumn<Appointment, AppointmentStatus> colStatus;
    @FXML private TextField txtId;
    @FXML private DatePicker dpDate;
    @FXML private TextField txtTime;
    @FXML private ChoiceBox<AppointmentStatus> cbStatus;
    @FXML private Button btnAdd;

    private final ObservableList<Appointment> appointments = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getId()));
        colStatus.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getStatus()));
        tableAppointments.setItems(appointments);
        cbStatus.setItems(FXCollections.observableArrayList(AppointmentStatus.values()));
    }

    @FXML
    private void addAppointment() {
        Appointment appointment = new Appointment(
                txtId.getText(),
                dpDate.getValue() != null ? dpDate.getValue() : LocalDate.now(),
                0.0,
                txtTime.getText(),
                "",
                "",
                cbStatus.getValue(),
                0.0,
                null,
                null
        );
        appointments.add(appointment);
        txtId.clear();
        txtTime.clear();
        dpDate.setValue(null);
        cbStatus.getSelectionModel().clearSelection();
    }
}
