package viewController;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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

    private Veterinary veterinary;
    private Pet seleccion;

    public void setVeterinary(Veterinary veterinary) {
        this.veterinary = veterinary;
        observableList.setAll(veterinary.getListPets());
    }

    @FXML private void initialize(){
        columnaNombre.setCellValueFactory(c -> new ReadOnlyStringWrapper(safe(c.getValue().getName())));
        columnaEspecie.setCellValueFactory(c -> new ReadOnlyStringWrapper(safe(c.getValue().getSpecies())));
        columnaRaza.setCellValueFactory(c -> new ReadOnlyStringWrapper(safe(c.getValue().getBreed())));
        columnaEdad.setCellValueFactory(c -> new ReadOnlyStringWrapper(safe(c.getValue().getAge())));
        columnaPropietario.setCellValueFactory(c -> new ReadOnlyStringWrapper(safe(c.getValue().getOwner())));
        columnaCitas.setCellValueFactory(c -> new ReadOnlyStringWrapper(safe(c.getValue().getListAppointments())));

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
        filteredList.setPredicate(veterinary -> t.isEmpty()
            || lower(veterinary.getName()).contains(t))
            || lower(veterinary.get)
    }


}
