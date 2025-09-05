package viewController;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import model.Owner;
import model.Pet;
import model.Species;
import model.Veterinary;


public class PetController {
    @FXML private TextField textoBuscar;
    @FXML private TableView<Pet> tabla;
    @FXML private TableColumn<Pet, String> columnaEspecie, columnaNombre, columnaRaza, columnaEdad,
    columnaPropietario, columnaCitas;
    @FXML private TextField ingresarNombre, ingresarRaza, ingresarEdad;
    @FXML private ComboBox<Species> elegirEspecie;
    @FXML private ComboBox<Owner> elegirPropietario;

    private final ObservableList<Pet> observableList = FXCollections.observableArrayList();
    private final FilteredList<Pet> filteredList = new FilteredList<>(observableList, p -> true);
    private final ObservableList<Owner> owners = FXCollections.observableArrayList();

    private Veterinary veterinary;
    private Pet seleccion;

    public void setVeterinary(Veterinary veterinary) {
        this.veterinary = veterinary;
        observableList.setAll(veterinary.getListPets());
        owners.setAll(veterinary.getListOwners());
    }

    @FXML private void initialize(){
        elegirEspecie.setItems(FXCollections.observableArrayList(Species.values()));
        elegirPropietario.setItems(owners);

        elegirPropietario.setConverter(new StringConverter<Owner>() {
            @Override
            public String toString(Owner owner) {
                return owner == null ? "" : owner.getName();
            }
            @Override
            public Owner fromString(String string) {
                return null;
            }
        });

        columnaNombre.setCellValueFactory(c -> new ReadOnlyStringWrapper(safe(c.getValue().getName())));
        columnaEspecie.setCellValueFactory(c -> new ReadOnlyStringWrapper(String.valueOf(c.getValue().getSpecies())));
        columnaRaza.setCellValueFactory(c -> new ReadOnlyStringWrapper(safe(c.getValue().getBreed())));
        columnaEdad.setCellValueFactory(c -> new ReadOnlyStringWrapper(String.valueOf(c.getValue().getAge())));
        columnaPropietario.setCellValueFactory(c -> new ReadOnlyStringWrapper(String.valueOf(c.getValue().getOwner())));
        columnaCitas.setCellValueFactory(c -> new ReadOnlyStringWrapper(String.valueOf(c.getValue().getListAppointments())));

        tabla.setItems(filteredList);
        tabla.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, pet) -> {
            seleccion = pet;
            if(seleccion != null)   cargarFormulario(pet);
            else limpiarFormulario();
        });
    }
    //onAction
    @FXML private void filtrar() {
        String t = lower(textoBuscar.getText());
        filteredList.setPredicate(pet -> t.isEmpty()
            || lower(pet.getName()).contains(t)
            || lower(String.valueOf(pet.getSpecies())).contains(t)
            || lower(pet.getBreed()).contains(t)
            || lower(pet.getOwner().getName()).contains(t)
            || lower(String.valueOf(pet.getAge())).contains(t)
        );
    }

    @FXML private void limpiarFiltros() {
        textoBuscar.clear();
        filteredList.setPredicate(pet -> true);
    }
    @FXML private void limpiar() {
        tabla.getSelectionModel().clearSelection();
        limpiarFormulario();
    }

    @FXML private void agregar() {
        if(!validarCrear()) {
            warn("Completa: Nombre, ID, Especie, Raza, Dueño, color, edad y peso validos.");
            return;
        }
        byte age = Byte.parseByte(ingresarEdad.getText());
        Pet pet = new Pet(
                ingresarNombre.getText(),
                elegirEspecie.getValue(),
                ingresarRaza.getText(),
                age,
                elegirPropietario.getValue()
        );
        String mensaje = veterinary.addPet(pet);
        info(mensaje);
        if(mensaje.contains("success")) observableList.add(pet);
        limpiarFormulario();
    }
    @FXML private void guardarCambios() {
        if(seleccion == null) {
            warn("Seleccione una mascota para editar.");
            return;
        }
        if(!validarEditar()) {
            warn("Completa: Nombre, ID, Especie, Raza, Dueño, color, edad y peso validos.");
            return;
        }
        byte age = Byte.parseByte(ingresarEdad.getText());
        Pet petUpdate = new Pet(
                ingresarNombre.getText(),
                elegirEspecie.getValue(),
                ingresarRaza.getText(),
                age,
                elegirPropietario.getValue()
        );
        String idEditar = seleccion.getId();
        String mensaje = veterinary.updatePet(idEditar, petUpdate);
        info(mensaje);
        if(mensaje.contains("success")) {
            observableList.add(petUpdate);
            int indice = observableList.indexOf(seleccion);
            if( indice >= 0) observableList.set(indice, petUpdate);
            tabla.getSelectionModel().clearSelection();
            limpiarFormulario();
        }
    }
    @FXML private void eliminar() {
        Pet pet = tabla.getSelectionModel().getSelectedItem();
        if(pet == null) {
            warn("Seleccione una mascota para eliminar.");
            return;
        }
        String mensaje =  veterinary.removePet(pet);
        info(mensaje);
        if(mensaje.contains("success")) {
            observableList.remove(pet);
            tabla.getSelectionModel().clearSelection();
            limpiarFormulario();
        }
    }
    // Metodo utiles
    private void cargarFormulario(Pet pet) {
        ingresarNombre.setText(safe(pet.getName()));
        elegirEspecie.setValue(pet.getSpecies());
        ingresarRaza.setText(safe(pet.getBreed()));
        ingresarEdad.setText(String.valueOf(pet.getAge()));
        elegirPropietario.setValue(pet.getOwner());
    }
    private void limpiarFormulario() {
        ingresarNombre.clear();
        elegirEspecie.setValue(null);
        ingresarRaza.clear();
        ingresarEdad.clear();
        elegirPropietario.setValue(null);
    }
    private boolean validarCrear() {
        return !(isEmpty(ingresarNombre.getText())||isEmpty(ingresarRaza.getText())||Integer.parseInt(ingresarEdad.getText())<0);
    }
    private boolean validarEditar() {
        return !(isEmpty(ingresarNombre.getText())||isEmpty(ingresarRaza.getText())||Integer.parseInt(ingresarEdad.getText())<0);
    }

    //Metodos helpers
    private String safe(String text) {
        return text == null ? "" : text;
    }
    private String lower(String text) {
        return text == null ? "" : text.toLowerCase().trim();
    }
    private void warn(String mensaje) {
        new Alert(Alert.AlertType.WARNING, mensaje).showAndWait();
    }
    private void info(String mensaje) {
        new Alert(Alert.AlertType.INFORMATION, mensaje).showAndWait();
    }
    private boolean isEmpty(String text) {
        return text == null || text.trim().isEmpty();
    }


}
