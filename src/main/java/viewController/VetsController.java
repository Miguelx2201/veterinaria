package viewController;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Specialty;
import model.Veterinarian;
import model.Veterinary;

public class VetsController {

    @FXML private TextField txtBuscar;

    @FXML private TableView<Veterinarian> tabla;
    @FXML private TableColumn<Veterinarian,String> colNombre, colApellido, colId, colTelefono, colEmail, colDireccion, colTarjeta, colEspecialidad;
    @FXML private TableColumn<Veterinarian,String> colAnios; // lo muestro como texto

    @FXML private TextField fNombre, fApellido, fId, fTelefono, fEmail, fDireccion, fTarjeta, fAnios;
    @FXML private ComboBox<Specialty> fEspecialidad;
    @FXML private Button btnActualizar;

    private final ObservableList<Veterinarian> data = FXCollections.observableArrayList();
    private final FilteredList<Veterinarian> filtered = new FilteredList<>(data, v -> true);

    private Veterinary veterinaria;
    private Veterinarian seleccion;

    public void setVeterinaria(Veterinary v){
        this.veterinaria = v;
        data.setAll(v.getListVeterinarians());
    }

    @FXML
    private void initialize() {
        fEspecialidad.setItems(FXCollections.observableArrayList(Specialty.values()));

        colNombre.setCellValueFactory(c -> new ReadOnlyStringWrapper(safe(c.getValue().getName())));
        colApellido.setCellValueFactory(c -> new ReadOnlyStringWrapper(safe(c.getValue().getLastName())));
        colId.setCellValueFactory(c -> new ReadOnlyStringWrapper(safe(c.getValue().getId())));
        colTelefono.setCellValueFactory(c -> new ReadOnlyStringWrapper(safe(c.getValue().getPhone())));
        colEmail.setCellValueFactory(c -> new ReadOnlyStringWrapper(safe(c.getValue().getEmail())));
        colDireccion.setCellValueFactory(c -> new ReadOnlyStringWrapper(safe(c.getValue().getAddress())));
        colTarjeta.setCellValueFactory(c -> new ReadOnlyStringWrapper(safe(c.getValue().getBusinessCard())));
        colAnios.setCellValueFactory(c -> new ReadOnlyStringWrapper(String.valueOf(c.getValue().getYearsExperience())));
        colEspecialidad.setCellValueFactory(c -> new ReadOnlyStringWrapper(
                c.getValue().getSpeciality() == null ? "" : c.getValue().getSpeciality().name()
        ));

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
        String t = lower(txtBuscar.getText());
        filtered.setPredicate(v -> t.isEmpty()
                || lower(v.getName()).contains(t)
                || lower(v.getLastName()).contains(t)
                || lower(v.getId()).contains(t)
                || lower(v.getPhone()).contains(t)
                || lower(v.getEmail()).contains(t)
                || lower(v.getAddress()).contains(t)
                || lower(v.getBusinessCard()).contains(t)
                || String.valueOf(v.getYearsExperience()).contains(t)
                || (v.getSpeciality()!=null && lower(v.getSpeciality().name()).contains(t))
        );
    }
    @FXML private void limpiarFiltros(){ txtBuscar.clear(); filtered.setPredicate(v -> true); }
    @FXML private void nuevo(){ tabla.getSelectionModel().clearSelection(); limpiarFormulario(); }

    @FXML private void agregar(){
        if (!validarCrear()) { warn("Completa: Nombre, Apellido, ID, Teléfono y Años Exp. válido."); return; }
        int anios = parseIntSafe(fAnios.getText(), -1);
        Veterinarian v = new Veterinarian(
                fNombre.getText().trim(),
                fApellido.getText().trim(),
                fId.getText().trim(),
                fTelefono.getText().trim(),
                fEmail.getText().trim(),
                fDireccion.getText().trim(),
                fTarjeta.getText().trim(),
                anios,
                fEspecialidad.getValue()
        );
        String msg = veterinaria.addVeterinarian(v);
        info(msg);
        if (msg.contains("successfully")) data.add(v);
        limpiarFormulario();
    }

    @FXML private void guardarCambios(){
        if (seleccion == null){ info("Selecciona un veterinario."); return; }
        if (!validarEditar()){ warn("Completa: Nombre, Apellido, Teléfono y Años Exp. válido."); return; }

        int anios = parseIntSafe(fAnios.getText(), -1);
        Veterinarian actualizado = new Veterinarian(
                fNombre.getText().trim(),
                fApellido.getText().trim(),
                fId.getText().trim(),
                fTelefono.getText().trim(),
                fEmail.getText().trim(),
                fDireccion.getText().trim(),
                fTarjeta.getText().trim(),
                anios,
                fEspecialidad.getValue()
        );

        String originalId = seleccion.getId();
        String msg = veterinaria.updateVeterinarian(originalId, actualizado);
        info(msg);
        if (msg.contains("successfully")) {
            int idx = data.indexOf(seleccion);
            if (idx >= 0) data.set(idx, actualizado);
            tabla.getSelectionModel().clearSelection();
            limpiarFormulario();
        }
    }

    @FXML private void eliminar(){
        Veterinarian v = tabla.getSelectionModel().getSelectedItem();
        if (v == null) return;
        if (!confirm("¿Eliminar a " + v.getName() + " " + v.getLastName() + "?")) return;
        String msg = veterinaria.removeVeterinarian(v.getId());
        info(msg);
        if (msg.contains("successfully")) data.remove(v);
        limpiarFormulario();
    }

    // util
    private void cargarFormulario(Veterinarian v){
        fNombre.setText(safe(v.getName()));
        fApellido.setText(safe(v.getLastName()));
        fId.setText(safe(v.getId()));
        fTelefono.setText(safe(v.getPhone()));
        fEmail.setText(safe(v.getEmail()));
        fDireccion.setText(safe(v.getAddress()));
        fTarjeta.setText(safe(v.getBusinessCard()));
        fAnios.setText(String.valueOf(v.getYearsExperience()));
        fEspecialidad.setValue(v.getSpeciality());
    }
    private void limpiarFormulario(){
        fNombre.clear(); fApellido.clear(); fId.clear(); fTelefono.clear(); fEmail.clear(); fDireccion.clear();
        fTarjeta.clear(); fAnios.clear(); fEspecialidad.setValue(null);
    }
    private boolean validarCrear(){
        return !(isEmpty(fNombre.getText()) || isEmpty(fApellido.getText()) || isEmpty(fId.getText())
                || isEmpty(fTelefono.getText()) || parseIntSafe(fAnios.getText(), -1) < 0);
    }
    private boolean validarEditar(){
        return !(isEmpty(fNombre.getText()) || isEmpty(fApellido.getText())
                || isEmpty(fTelefono.getText()) || parseIntSafe(fAnios.getText(), -1) < 0);
    }

    // helpers
    private static int parseIntSafe(String s, int def){ try { return Integer.parseInt(s.trim()); } catch(Exception e){ return def; } }
    private static String safe(String s){ return s==null? "": s; }
    private static String lower(String s){ return s==null? "": s.toLowerCase().trim(); }
    private static boolean isEmpty(String s){ return s==null || s.trim().isEmpty(); }
    private static void warn(String m){ new Alert(Alert.AlertType.WARNING, m).showAndWait(); }
    private static void info(String m){ new Alert(Alert.AlertType.INFORMATION, m).showAndWait(); }
    private static boolean confirm(String m){
        Alert a=new Alert(Alert.AlertType.CONFIRMATION,m,ButtonType.OK,ButtonType.CANCEL);
        return a.showAndWait().orElse(ButtonType.CANCEL)==ButtonType.OK;
    }
}
