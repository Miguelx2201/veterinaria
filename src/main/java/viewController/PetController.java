package viewController;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import model.Pet;
import model.Species;

public class PetController {

    @FXML private TableView<Pet> tablePets;
    @FXML private TableColumn<Pet, String> colId;
    @FXML private TableColumn<Pet, String> colName;
    @FXML private TableColumn<Pet, Species> colSpecies;
    @FXML private TextField txtId;
    @FXML private TextField txtName;
    @FXML private ChoiceBox<Species> cbSpecies;
    @FXML private Button btnAdd;

    private final ObservableList<Pet> pets = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getId()));
        colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        colSpecies.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getSpecies()));
        tablePets.setItems(pets);
        cbSpecies.setItems(FXCollections.observableArrayList(Species.values()));
    }

    @FXML
    private void addPet() {
        Pet pet = new Pet(
                txtId.getText(),
                txtName.getText(),
                cbSpecies.getValue(),
                "",
                (byte) 0,
                "",
                0.0
        );
        pets.add(pet);
        txtId.clear();
        txtName.clear();
        cbSpecies.getSelectionModel().clearSelection();
    }
}
