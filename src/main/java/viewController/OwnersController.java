package viewController;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Owner;
import model.Veterinary;

public class OwnersController {

    // Filtros
    @FXML private TextField txtBuscar;

    // Tabla
    @FXML private TableView<Owner> tabla;
    @FXML private TableColumn<Owner,String> colNombre, colApellido, colId, colTelefono, colEmail, colDireccion;

    // Formulario
    @FXML private TextField fNombre, fApellido, fId, fTelefono, fEmail, fDireccion;
    @FXML private Button btnActualizar;

    private final ObservableList<Owner> data = FXCollections.observableArrayList();
    private final FilteredList<Owner> filtered = new FilteredList<>(data, o -> true);

    private Veterinary veterinaria;
    private Owner seleccion;

    public void setVeterinaria(Veterinary v){
        this.veterinaria = v;
        data.setAll(v.getListOwners());
    }

    @FXML
    private void initialize() {
        colNombre.setCellValueFactory(c -> new ReadOnlyStringWrapper(safe(c.getValue().getName())));
        colApellido.setCellValueFactory(c -> new ReadOnlyStringWrapper(safe(c.getValue().getLastName())));
        colId.setCellValueFactory(c -> new ReadOnlyStringWrapper(safe(c.getValue().getId())));
        colTelefono.setCellValueFactory(c -> new ReadOnlyStringWrapper(safe(c.getValue().getPhone())));
        colEmail.setCellValueFactory(c -> new ReadOnlyStringWrapper(safe(c.getValue().getEmail())));
        colDireccion.setCellValueFactory(c -> new ReadOnlyStringWrapper(safe(c.getValue().getAddress())));

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
        filtered.setPredicate(o -> t.isEmpty()
                || lower(o.getName()).contains(t)
                || lower(o.getLastName()).contains(t)
                || lower(o.getId()).contains(t)
                || lower(o.getPhone()).contains(t)
                || lower(o.getEmail()).contains(t)
                || lower(o.getAddress()).contains(t));
    }

    @FXML private void limpiarFiltros(){
        txtBuscar.clear();
        filtered.setPredicate(o -> true);
    }

    @FXML private void nuevo(){
        tabla.getSelectionModel().clearSelection();
        limpiarFormulario();
    }

    @FXML private void agregar(){
        if (!validarCrear()) { warn("Completa: Nombre, Apellido, ID y Teléfono."); return; }
        Owner o = leerFormulario();
        String msg = veterinaria.addOwner(o);
        info(msg);
        if (msg.contains("successfully")) data.add(o);
        limpiarFormulario();
    }

    @FXML private void guardarCambios(){
        if (seleccion == null) { info("Selecciona un propietario."); return; }
        if (!validarEditar()) { warn("Completa: Nombre, Apellido y Teléfono."); return; }

        // id original para updateOwner(id, owner)
        String originalId = seleccion.getId();
        Owner actualizado = leerFormulario(); // contiene el ID que esté en el campo fId ahora

        String msg = veterinaria.updateOwner(originalId, actualizado);
        info(msg);
        if (msg.contains("successfully")) {
            // Reemplazar en la lista visual
            int idx = data.indexOf(seleccion);
            if (idx >= 0) data.set(idx, actualizado);
            tabla.getSelectionModel().clearSelection();
            limpiarFormulario();
        }
    }

    @FXML private void eliminar(){
        Owner o = tabla.getSelectionModel().getSelectedItem();
        if (o == null) return;
        if (!confirm("¿Eliminar a " + o.getName() + " " + o.getLastName() + "?")) return;

        String msg = veterinaria.removeOwner(o.getId());
        info(msg);
        if (msg.contains("successfully")) data.remove(o);
        limpiarFormulario();
    }

    // Util
    private void cargarFormulario(Owner o){
        fNombre.setText(safe(o.getName()));
        fApellido.setText(safe(o.getLastName()));
        fId.setText(safe(o.getId()));
        fTelefono.setText(safe(o.getPhone()));
        fEmail.setText(safe(o.getEmail()));
        fDireccion.setText(safe(o.getAddress()));
    }
    private Owner leerFormulario(){
        return new Owner(
                safe(fNombre.getText()).trim(),
                safe(fApellido.getText()).trim(),
                safe(fId.getText()).trim(),
                safe(fTelefono.getText()).trim(),
                safe(fEmail.getText()).trim(),
                safe(fDireccion.getText()).trim()
        );
    }
    private void limpiarFormulario(){
        fNombre.clear(); fApellido.clear(); fId.clear(); fTelefono.clear(); fEmail.clear(); fDireccion.clear();
    }
    private boolean validarCrear(){
        return !(isEmpty(fNombre.getText()) || isEmpty(fApellido.getText()) || isEmpty(fId.getText()) || isEmpty(fTelefono.getText()));
    }
    private boolean validarEditar(){
        return !(isEmpty(fNombre.getText()) || isEmpty(fApellido.getText()) || isEmpty(fTelefono.getText()));
    }

    // helpers UI
    private static String safe(String s){ return s==null ? "" : s; }
    private static String lower(String s){ return s==null ? "" : s.toLowerCase().trim(); }
    private static boolean isEmpty(String s){ return s==null || s.trim().isEmpty(); }
    private static void warn(String m){ new Alert(Alert.AlertType.WARNING, m).showAndWait(); }
    private static void info(String m){ new Alert(Alert.AlertType.INFORMATION, m).showAndWait(); }
    private static boolean confirm(String m){
        Alert a = new Alert(Alert.AlertType.CONFIRMATION, m, ButtonType.OK, ButtonType.CANCEL);
        return a.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }
}
