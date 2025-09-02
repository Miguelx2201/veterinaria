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

/**
 * Controller basado en los fx:id que enviaste.
 * - Llena combos, configura tabla, aplica filtros.
 * - Agrega/Elimina via Veterinary (inyectable).
 */
public class PetController {

    // ---- Filtros (arriba)
    @FXML private TextField buscarNombre;
    @FXML private ComboBox<Species> filtroEspecie;
    @FXML private ComboBox<Owner>   filtroPropietario;
    @FXML private Button botonFiltrar;
    @FXML private Button botonLimpiarFiltros;

    // ---- Tabla (centro)
    @FXML private TableView<Pet> tabla;
    @FXML private TableColumn<Pet, String> columnaNombre;
    @FXML private TableColumn<Pet, String> columnaEspecie;
    @FXML private TableColumn<Pet, String> columnaRaza;
    @FXML private TableColumn<Pet, String> columnaEdad;
    @FXML private TableColumn<Pet, String> columnaPropietario;

    // ---- Formulario (abajo)
    @FXML private TextField ingresarNombre;
    @FXML private ComboBox<Species> ingresarEspecie;      // <--- ajustado
    @FXML private TextField ingresarRaza;
    @FXML private TextField ingresarEdad;
    @FXML private TextField ingresarPeso;
    @FXML private ComboBox<Owner> ingresarPropietario;    // <--- ajustado
    @FXML private Button botonAgregar;
    @FXML private Button botonEliminar;
    @FXML private Button botonActualizar;
    private Pet seleccion;

    // ---- Datos en memoria (UI)
    private final ObservableList<Pet> data = FXCollections.observableArrayList();
    private final FilteredList<Pet> filtered = new FilteredList<>(data, p -> true);
    private final ObservableList<Owner> owners = FXCollections.observableArrayList();

    // ---- Servicio del dominio (tu clase veterinaria)
    private Veterinary veterinaria; // se inyecta desde el MainController

    /** Llamado desde fuera para pasar la dependencia real */
    public void setVeterinaria(Veterinary veterinaria) {
        this.veterinaria = veterinaria;

        // Carga inicial de datos reales (si procede)
        // data.setAll(veterinary.findAllPets());  // <-- Ajusta al nombre real si lo tienes
        // owners.setAll(veterinary.findAllOwners());
        // Para que la UI arranque incluso si no hay backend aún, relleno algo si la lista está vacía:
        //if (owners.isEmpty() && veterinaria != null) {
        //    owners.setAll(veterinaria.findAllOwners()); // si existe
        //}
        //if (data.isEmpty() && veterinaria != null) {
        //    data.setAll(veterinaria.findAllPets()); // si existe
        //}
    }

    @FXML
    private void initialize() {
        // --- Combos (Species)
        filtroEspecie.setItems(FXCollections.observableArrayList(Species.values()));
        ingresarEspecie.setItems(FXCollections.observableArrayList(Species.values()));

        // --- Combos (Owners)
        ingresarPropietario.setItems(owners);
        filtroPropietario.setItems(owners);

        // Mostrar Owner por nombre
        StringConverter<Owner> ownerConverter = new StringConverter<>() {
            @Override public String toString(Owner o) { return o == null ? "" : o.getName(); }
            @Override public Owner fromString(String s) { return null; }
        };
        ingresarPropietario.setConverter(ownerConverter);
        filtroPropietario.setConverter(ownerConverter);

        // --- Configurar columnas (usando getters del modelo)
        columnaNombre.setCellValueFactory(c -> new ReadOnlyStringWrapper(
                safe(c.getValue().getName())
        ));
        columnaEspecie.setCellValueFactory(c -> new ReadOnlyStringWrapper(
                c.getValue().getSpecies() == null ? "" : c.getValue().getSpecies().name()
        ));
        columnaRaza.setCellValueFactory(c -> new ReadOnlyStringWrapper(
                safe(c.getValue().getBreed())
        ));
        columnaEdad.setCellValueFactory(c -> new ReadOnlyStringWrapper(
                String.valueOf(c.getValue().getAge())
        ));
        columnaPropietario.setCellValueFactory(c -> new ReadOnlyStringWrapper(
                c.getValue().getOwner() == null ? "" : safe(c.getValue().getOwner().getName())
        ));

        // --- Tabla
        tabla.setItems(filtered);
        tabla.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            if (sel != null) cargarFormulario(sel);
        });

        // --- Estados iniciales
        botonEliminar.disableProperty().bind(
                tabla.getSelectionModel().selectedItemProperty().isNull()
        );

        // --- Placeholders/Prompts útiles
        if (buscarNombre.getText() == null || buscarNombre.getText().isEmpty()) {
            buscarNombre.setPromptText("Buscar por nombre o raza");
        }

        tabla.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            seleccion = newSel;
            if (newSel != null) {
                cargarFormulario(newSel);   // rellena campos con la mascota elegida
            } else {
                limpiarFormulario();
            }
            // Habilitar/deshabilitar el botón de actualizar según haya selección
            if (botonActualizar != null) {
                botonActualizar.setDisable(newSel == null);
            }
        });

        // Si quieres que arrancar sin poder actualizar:
        if (botonActualizar != null) {
            botonActualizar.setDisable(true);
        }
    }

    // ========= Acciones =========
    @FXML
    private void guardarCambios() {
        Pet sel = tabla.getSelectionModel().getSelectedItem();
        if (sel == null) {
            new Alert(Alert.AlertType.INFORMATION, "Selecciona una mascota primero.").showAndWait();
            return;
        }

        // Validación rápida
        if (ingresarNombre.getText() == null || ingresarNombre.getText().trim().isEmpty()
                || ingresarEspecie.getValue() == null
                || ingresarPropietario.getValue() == null) {
            new Alert(Alert.AlertType.WARNING, "Completa Nombre, Especie y Propietario.").showAndWait();
            return;
        }

        // Parsear edad y peso de forma segura
        byte age;
        try {
            age = Byte.parseByte(ingresarEdad.getText().trim());
        } catch (Exception e) {
            new Alert(Alert.AlertType.WARNING, "Edad no válida.").showAndWait();
            return;
        }

        double weight = 0.0;
        if (ingresarPeso.getText() != null && !ingresarPeso.getText().trim().isEmpty()) {
            try {
                weight = Double.parseDouble(ingresarPeso.getText().trim());
            } catch (Exception e) {
                new Alert(Alert.AlertType.WARNING, "Peso no válido.").showAndWait();
                return;
            }
        }

        // Actualizar campos de la mascota seleccionada
        sel.setName(ingresarNombre.getText().trim());
        sel.setSpecies(ingresarEspecie.getValue());
        sel.setBreed(ingresarRaza.getText().trim());
        sel.setAge(age);
        sel.setWeight(weight);
        sel.setOwner(ingresarPropietario.getValue());

        // Llamar a tu método updatePet en Veterinary
        if (veterinaria != null) {
            String result = veterinaria.updatePet(sel);
            new Alert(Alert.AlertType.INFORMATION, result).showAndWait();
        }

        // Refrescar tabla
        tabla.refresh();
        limpiarFormulario();
    }



    @FXML
    private void filtrar() {
        final String texto = lowerOrEmpty(buscarNombre.getText());
        final Species especie = filtroEspecie.getValue();
        final Owner owner = filtroPropietario.getValue();

        filtered.setPredicate(pet -> {
            boolean mTexto = texto.isEmpty()
                    || lowerOrEmpty(pet.getName()).contains(texto)
                    || lowerOrEmpty(pet.getBreed()).contains(texto);

            boolean mEspecie = (especie == null) || especie.equals(pet.getSpecies());

            boolean mOwner = (owner == null) || (pet.getOwner() != null && owner.equals(pet.getOwner()));

            return mTexto && mEspecie && mOwner;
        });
    }

    @FXML
    private void limpiarFiltros() {
        buscarNombre.clear();
        filtroEspecie.setValue(null);
        filtroPropietario.setValue(null);
        filtered.setPredicate(p -> true);
    }

    @FXML
    private void agregar() {
        // Validación básica
        if (!validarFormulario(true)) {
            alertWarn("Completa Nombre, Especie, Propietario y una Edad válida (0-30).");
            return;
        }

        Pet nueva = leerFormulario();

        // Llamada a tu dominio (si la tienes)
        if (veterinaria != null) {
            try {
                veterinaria.addPet(nueva); // <-- ajusta al nombre real de tu método
            } catch (Exception ex) {
                alertError("No se pudo agregar la mascota: " + ex.getMessage());
                return;
            }
        }

        data.add(nueva);
        limpiarFormulario();
        tabla.getSelectionModel().clearSelection();
    }

    @FXML
    private void eliminar() {
        Pet sel = tabla.getSelectionModel().getSelectedItem();
        if (sel == null) return;

        if (!confirm("¿Eliminar la mascota \"" + sel.getName() + "\"?")) return;

        if (veterinaria != null) {
            try {
                // puedes eliminar por id si lo prefieres:
                // veterinary.removePetById(sel.getId());
                veterinaria.removePet(sel); // <-- ajusta a tu firma real
            } catch (Exception ex) {
                alertError("No se pudo eliminar: " + ex.getMessage());
                return;
            }
        }

        data.remove(sel);
        limpiarFormulario();
    }

    // ========= Utilidades de formulario/tabla =========

    private void cargarFormulario(Pet p) {
        ingresarNombre.setText(safe(p.getName()));
        ingresarEspecie.setValue(p.getSpecies());
        ingresarRaza.setText(safe(p.getBreed()));
        ingresarEdad.setText(String.valueOf(p.getAge()));
        ingresarPeso.setText(String.valueOf(p.getWeight()));
        ingresarPropietario.setValue(p.getOwner());
    }

    private Pet leerFormulario() {
        String name = trimOrNull(ingresarNombre.getText());
        Species species = ingresarEspecie.getValue();
        String breed = trimOrEmpty(ingresarRaza.getText());
        byte age = parseByteSafe(ingresarEdad.getText(), (byte)0);
        double weight = parseDoubleSafe(ingresarPeso.getText(), 0.0);
        Owner owner = ingresarPropietario.getValue();

        // El id puede venir de Veterinary (auto), o generar uno simple si hace falta
        String id = (name == null ? "" : name.toLowerCase().replaceAll("\\s+","-")) + "-" + System.currentTimeMillis();

        Pet p = new Pet(id, name, species, breed, age, /*color*/"", weight);
        p.setOwner(owner); // tu Pet tiene setOwner? (en el snippet no estaba; agrégalo si falta)
        return p;
    }

    private void limpiarFormulario() {
        ingresarNombre.clear();
        ingresarEspecie.setValue(null);
        ingresarRaza.clear();
        ingresarEdad.clear();
        ingresarPeso.clear();
        ingresarPropietario.setValue(null);
    }

    private boolean validarFormulario(boolean crear) {
        if (isEmpty(ingresarNombre.getText())) return false;
        if (ingresarEspecie.getValue() == null) return false;
        if (ingresarPropietario.getValue() == null) return false;

        byte age = parseByteSafe(ingresarEdad.getText(), (byte)-1);
        if (age < 0 || age > 30) return false;

        // peso opcional, pero si viene, que sea >= 0
        double w = parseDoubleSafe(ingresarPeso.getText(), -1);
        if (!isEmpty(ingresarPeso.getText()) && w < 0) return false;

        return true;
    }

    // ========= Helpers varios =========
    private static String safe(String s) { return s == null ? "" : s; }
    private static String trimOrEmpty(String s) { return s == null ? "" : s.trim(); }
    private static String trimOrNull(String s) { return (s == null || s.trim().isEmpty()) ? null : s.trim(); }
    private static boolean isEmpty(String s) { return s == null || s.trim().isEmpty(); }
    private static String lowerOrEmpty(String s) { return s == null ? "" : s.toLowerCase().trim(); }

    private static byte parseByteSafe(String s, byte def) {
        try { return Byte.parseByte(trimOrEmpty(s)); } catch (Exception e) { return def; }
    }
    private static double parseDoubleSafe(String s, double def) {
        try { return Double.parseDouble(trimOrEmpty(s)); } catch (Exception e) { return def; }
    }

    private static void alertWarn(String msg) {
        new Alert(Alert.AlertType.WARNING, msg).showAndWait();
    }
    private static void alertError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }
    private static boolean confirm(String msg) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION, msg, ButtonType.OK, ButtonType.CANCEL);
        return a.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }

}
