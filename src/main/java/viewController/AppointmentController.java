package viewController;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import model.*;

import java.time.LocalDate;
import java.util.UUID;

public class AppointmentController {

    // Filtros
    @FXML private DatePicker filtroFecha;
    @FXML private ComboBox<Status> filtroEstado;

    // Tabla
    @FXML private TableView<Appointment> tabla;
    @FXML private TableColumn<Appointment,String> colFecha, colHora, colMascota, colVet, colMotivo, colEstado, colCosto, colDuracion;

    // Formulario
    @FXML private DatePicker fFecha;
    @FXML private TextField fHora, fMotivo, fObs, fCosto, fDuracion;
    @FXML private ComboBox<Pet> fMascota;
    @FXML private ComboBox<Veterinarian> fVeterinario;
    @FXML private ComboBox<Status> fEstado;
    @FXML private Button btnActualizar;

    private final ObservableList<Appointment> data = FXCollections.observableArrayList();
    private final FilteredList<Appointment> filtered = new FilteredList<>(data, a -> true);

    private final ObservableList<Pet> pets = FXCollections.observableArrayList();
    private final ObservableList<Veterinarian> vets = FXCollections.observableArrayList();

    private Veterinary veterinaria;
    private Appointment seleccion;

    public void setVeterinaria(Veterinary v){
        this.veterinaria = v;
        data.setAll(v.getListAppointments());
        pets.setAll(v.getListPets());
        vets.setAll(v.getListVeterinarians());
    }

    @FXML
    private void initialize() {
        // Combos base
        fEstado.setItems(FXCollections.observableArrayList(Status.values()));
        filtroEstado.setItems(FXCollections.observableArrayList(Status.values()));

        fMascota.setItems(pets);
        fVeterinario.setItems(vets);

        // converters para mostrar nombres
        fMascota.setConverter(new StringConverter<>() {
            @Override public String toString(Pet p){ return p==null? "" : p.getName(); }
            @Override public Pet fromString(String s){ return null; }
        });
        fVeterinario.setConverter(new StringConverter<>() {
            @Override public String toString(Veterinarian v){ return v==null? "" : (v.getName()+" "+v.getLastName()); }
            @Override public Veterinarian fromString(String s){ return null; }
        });

        // columnas
        colFecha.setCellValueFactory(c -> new ReadOnlyStringWrapper(
                c.getValue().getDate()==null ? "" : c.getValue().getDate().toString()
        ));
        colHora.setCellValueFactory(c -> new ReadOnlyStringWrapper(safe(c.getValue().getTime())));
        colMascota.setCellValueFactory(c -> new ReadOnlyStringWrapper(
                c.getValue().getPet()==null? "" : safe(c.getValue().getPet().getName())
        ));
        colVet.setCellValueFactory(c -> new ReadOnlyStringWrapper(
                c.getValue().getVeterinarian()==null? "" :
                        (safe(c.getValue().getVeterinarian().getName())+" "+safe(c.getValue().getVeterinarian().getLastName()))
        ));
        colMotivo.setCellValueFactory(c -> new ReadOnlyStringWrapper(safe(c.getValue().getReason())));
        colEstado.setCellValueFactory(c -> new ReadOnlyStringWrapper(
                c.getValue().getStatus()==null? "" : c.getValue().getStatus().name()
        ));
        colCosto.setCellValueFactory(c -> new ReadOnlyStringWrapper(String.valueOf(c.getValue().getCost())));
        colDuracion.setCellValueFactory(c -> new ReadOnlyStringWrapper(String.valueOf(c.getValue().getDuration())));

        tabla.setItems(filtered);
        tabla.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            seleccion = sel;
            if (sel != null) cargarFormulario(sel); else limpiarFormulario();
            if (btnActualizar != null) btnActualizar.setDisable(sel == null);
        });
        if (btnActualizar != null) btnActualizar.setDisable(true);
    }

    // Acciones
    @FXML private void filtrar(){
        LocalDate f = filtroFecha.getValue();
        Status s = filtroEstado.getValue();
        filtered.setPredicate(a ->
                (f==null || f.equals(a.getDate())) &&
                        (s==null || s.equals(a.getStatus()))
        );
    }
    @FXML private void limpiarFiltros(){
        filtroFecha.setValue(null);
        filtroEstado.setValue(null);
        filtered.setPredicate(a -> true);
    }
    @FXML private void nuevo(){
        tabla.getSelectionModel().clearSelection();
        limpiarFormulario();
    }

    @FXML private void agregar(){
        if (!validarCrear()) { warn("Completa Fecha, Hora (HH:mm), Mascota, Veterinario y Estado. Costo/Duración válidos opcionales."); return; }
        Appointment a = leerFormularioConIdNuevo();
        String msg = veterinaria.addAppointmente(a); // (sic) firma exacta
        info(msg);
        if (msg.contains("successfully")) data.add(a);
        limpiarFormulario();
    }

    @FXML private void guardarCambios(){
        if (seleccion == null){ info("Selecciona una cita."); return; }
        if (!validarCrear()){ warn("Completa Fecha, Hora, Mascota, Veterinario y Estado."); return; }

        Appointment actualizado = leerFormularioConId(seleccion.getId());
        String msg = veterinaria.updateAppointment(seleccion.getId(), actualizado);
        info(msg);
        if (msg.contains("successfully")) {
            int idx = data.indexOf(seleccion);
            if (idx >= 0) data.set(idx, actualizado);
            tabla.getSelectionModel().clearSelection();
            limpiarFormulario();
        }
    }

    @FXML private void eliminar(){
        Appointment a = tabla.getSelectionModel().getSelectedItem();
        if (a == null) return;
        if (!confirm("¿Eliminar la cita de "+ (a.getPet()!=null?a.getPet().getName():"(sin mascota)") +"?")) return;
        String msg = veterinaria.removeAppointmente(a.getId());
        info(msg);
        if (msg.contains("successfully")) data.remove(a);
        limpiarFormulario();
    }

    // util
    private void cargarFormulario(Appointment a){
        fFecha.setValue(a.getDate());
        fHora.setText(safe(a.getTime()));
        fMascota.setValue(a.getPet());
        fVeterinario.setValue(a.getVeterinarian());
        fMotivo.setText(safe(a.getReason()));
        fObs.setText(safe(a.getObservation()));
        fEstado.setValue(a.getStatus());
        fCosto.setText(String.valueOf(a.getCost()));
        fDuracion.setText(String.valueOf(a.getDuration()));
    }
    private void limpiarFormulario(){
        fFecha.setValue(null); fHora.clear(); fMascota.setValue(null); fVeterinario.setValue(null);
        fMotivo.clear(); fObs.clear(); fEstado.setValue(null); fCosto.clear(); fDuracion.clear();
    }
    private Appointment leerFormularioConIdNuevo(){
        String id = "apt-" + UUID.randomUUID();
        return leerFormularioConId(id);
    }
    private Appointment leerFormularioConId(String id){
        LocalDate date = fFecha.getValue();
        String time = safe(fHora.getText()).trim();
        double cost = parseDoubleSafe(fCosto.getText(), 0.0);
        String reason = safe(fMotivo.getText()).trim();
        String observation = safe(fObs.getText()).trim();
        Status status = fEstado.getValue();
        double duration = parseDoubleSafe(fDuracion.getText(), 0.0);
        Pet pet = fMascota.getValue();
        Veterinarian vet = fVeterinario.getValue();
        return new Appointment(id, date, cost, time, reason, observation, status, duration, pet, vet);
    }
    private boolean validarCrear(){
        if (fFecha.getValue()==null || isEmpty(fHora.getText()) || fMascota.getValue()==null || fVeterinario.getValue()==null || fEstado.getValue()==null) return false;
        if (!fHora.getText().trim().matches("^\\d{2}:\\d{2}$")) return false; // HH:mm
        if (!isEmpty(fCosto.getText()) && parseDoubleSafe(fCosto.getText(), -1) < 0) return false;
        if (!isEmpty(fDuracion.getText()) && parseDoubleSafe(fDuracion.getText(), -1) < 0) return false;
        return true;
    }

    // helpers
    private static String safe(String s){ return s==null? "" : s; }
    private static boolean isEmpty(String s){ return s==null || s.trim().isEmpty(); }
    private static double parseDoubleSafe(String s, double def){ try { return Double.parseDouble(s.trim()); } catch(Exception e){ return def; } }
    private static void warn(String m){ new Alert(Alert.AlertType.WARNING, m).showAndWait(); }
    private static void info(String m){ new Alert(Alert.AlertType.INFORMATION, m).showAndWait(); }
    private static boolean confirm(String m){
        Alert a=new Alert(Alert.AlertType.CONFIRMATION,m,ButtonType.OK,ButtonType.CANCEL);
        return a.showAndWait().orElse(ButtonType.CANCEL)==ButtonType.OK;
    }
}
