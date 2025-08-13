package views;

import controllers.HospitalController;
import controllers.HospitalValidator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import models.Hospital;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class HospitalView {

    private static final ObservableList<Hospital> hospitales = FXCollections.observableArrayList();
    private static final HospitalController controller = new HospitalController();

    public static ObservableList<Hospital> getListaHospitales() {
        return hospitales;
    }

    public static void mostrar(Stage stage) {
        // Campos de entrada
        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Nombre");

        TextField txtDireccion = new TextField();
        txtDireccion.setPromptText("Dirección");

        TextField txtTelefono = new TextField();
        txtTelefono.setPromptText("Teléfono");

        // Barra y filtros de búsqueda
        TextField txtBusqueda = new TextField();
        txtBusqueda.setPromptText("Buscar...");
        ComboBox<String> cmbFiltro = new ComboBox<>();
        cmbFiltro.getItems().addAll("Nombre", "Dirección", "Teléfono");
        cmbFiltro.setValue("Nombre");

        // Checkbox para mostrar solo activos
        CheckBox chkSoloActivos = new CheckBox("Mostrar solo activos");
        chkSoloActivos.setSelected(false); // Mostrar todos por defecto

        // Botones
        Button btnAgregar = new Button("Agregar");
        Button btnEliminar = new Button("Eliminar");
        Button btnEditar = new Button("Editar");
        Button btnBaja = new Button("Dar de Baja");
        Button btnReactivar = new Button("Reactivar");
        Button btnRegresar = new Button("Regresar");

        // Cargar hospitales desde la base de datos al iniciar
        recargarHospitalesDesdeBD();

        // Tabla
        TableView<Hospital> tabla = new TableView<>();
        tabla.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tabla.setItems(hospitales);

        // Llenar campos al seleccionar un registro
        tabla.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                txtNombre.setText(newSel.getNombre());
                txtDireccion.setText(newSel.getDireccion());
                txtTelefono.setText(newSel.getTelefono());
            }
        });

        
        TableColumn<Hospital, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        TableColumn<Hospital, String> colDireccion = new TableColumn<>("Dirección");
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));

        TableColumn<Hospital, String> colTelefono = new TableColumn<>("Teléfono");
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));

        TableColumn<Hospital, LocalDate> colFechaAlta = new TableColumn<>("Fecha de Alta");
        colFechaAlta.setCellValueFactory(new PropertyValueFactory<>("fechaAlta"));

        TableColumn<Hospital, Boolean> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(new PropertyValueFactory<>("activo"));
        colEstado.setCellFactory(col -> new TableCell<Hospital, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item ? "Activo" : "Baja");
                }
            }
        });

        tabla.getColumns().addAll(colNombre, colDireccion, colTelefono, colFechaAlta, colEstado);

        // Funciones

        btnAgregar.setOnAction(e -> {
            Hospital h = new Hospital(
                    txtNombre.getText().trim(),
                    txtDireccion.getText(),
                    txtTelefono.getText()
            );
            String error = HospitalValidator.validar(h);
            if (error != null && !error.isEmpty()) {
                mostrarAlerta("Error de validación", error);
                return;
            }
            if (controller.existePorNombre(txtNombre.getText().trim())) {
                mostrarAlerta("Duplicado", "Ya existe un hospital con ese nombre.");
                return;
            }
            controller.crearHospital(h); // Guarda en BD
            limpiarCampos(txtNombre, txtDireccion, txtTelefono);
            recargarHospitalesDesdeBD();
            actualizarTabla(tabla, chkSoloActivos.isSelected(), txtBusqueda.getText(), cmbFiltro.getValue());
        });

        btnEditar.setOnAction(e -> {
            Hospital seleccionado = tabla.getSelectionModel().getSelectedItem();
            if (seleccionado == null) {
                mostrarAlerta("Sin selección", "Selecciona un hospital para editar.");
                return;
            }
            seleccionado.setNombre(txtNombre.getText());
            seleccionado.setDireccion(txtDireccion.getText());
            seleccionado.setTelefono(txtTelefono.getText());
            String error = HospitalValidator.validar(seleccionado);
            if (error != null && !error.isEmpty()) {
                mostrarAlerta("Error de validación", error);
                return;
            }
            controller.actualizarHospital(seleccionado);
            limpiarCampos(txtNombre, txtDireccion, txtTelefono);
            recargarHospitalesDesdeBD();
            actualizarTabla(tabla, chkSoloActivos.isSelected(), txtBusqueda.getText(), cmbFiltro.getValue());
        });

        btnEliminar.setOnAction(e -> {
            ObservableList<Hospital> seleccionados = tabla.getSelectionModel().getSelectedItems();
            if (seleccionados.isEmpty()) {
                mostrarAlerta("Sin selección", "Selecciona al menos un hospital para eliminar.");
                return;
            }
            boolean exito = controller.eliminarHospitales(seleccionados); // Borra en la BD
            if (!exito) {
                mostrarAlerta("Error", "No se puede eliminar el hospital porque tiene registros asociados.");
            }
            recargarHospitalesDesdeBD();
            actualizarTabla(tabla, chkSoloActivos.isSelected(), txtBusqueda.getText(), cmbFiltro.getValue());
        });

        btnBaja.setOnAction(e -> {
            ObservableList<Hospital> seleccionados = tabla.getSelectionModel().getSelectedItems();
            if (seleccionados.isEmpty()) {
                mostrarAlerta("Sin selección", "Selecciona al menos un hospital para dar de baja.");
                return;
            }
            for (Hospital h : seleccionados) {
                h.setActivo(false);
                controller.actualizarHospital(h);
            }
            recargarHospitalesDesdeBD();
            actualizarTabla(tabla, chkSoloActivos.isSelected(), txtBusqueda.getText(), cmbFiltro.getValue());
        });

        btnReactivar.setOnAction(e -> {
            ObservableList<Hospital> seleccionados = tabla.getSelectionModel().getSelectedItems();
            if (seleccionados.isEmpty()) {
                mostrarAlerta("Sin selección", "Selecciona al menos un hospital para reactivar.");
                return;
            }
            for (Hospital h : seleccionados) {
                h.setActivo(true);
                controller.actualizarHospital(h);
            }
            recargarHospitalesDesdeBD();
            actualizarTabla(tabla, chkSoloActivos.isSelected(), txtBusqueda.getText(), cmbFiltro.getValue());
        });


        // Búsqueda en tiempo real
        txtBusqueda.textProperty().addListener((obs, oldVal, newVal) -> {
            actualizarTabla(tabla, chkSoloActivos.isSelected(), newVal, cmbFiltro.getValue());
        });

        cmbFiltro.setOnAction(e -> {
            actualizarTabla(tabla, chkSoloActivos.isSelected(), txtBusqueda.getText(), cmbFiltro.getValue());
        });

        chkSoloActivos.setOnAction(e -> {
            actualizarTabla(tabla, chkSoloActivos.isSelected(), txtBusqueda.getText(), cmbFiltro.getValue());
        });

        btnRegresar.setOnAction(e -> MainView.mostrar(stage));

        // Layout
        HBox menuBusqueda = new HBox(10, txtBusqueda, cmbFiltro);
        menuBusqueda.setAlignment(Pos.CENTER_LEFT);

        VBox campos = new VBox(10, txtNombre, txtDireccion, txtTelefono, chkSoloActivos, menuBusqueda);
        HBox botones = new HBox(10, btnAgregar, btnEditar, btnBaja, btnReactivar, btnEliminar, btnRegresar);
        VBox contenedor = new VBox(15, campos, botones, tabla);
        contenedor.setPadding(new Insets(20));
        contenedor.setAlignment(Pos.CENTER);

        Scene scene = new Scene(contenedor, 1000, 700);
        scene.getStylesheets().add(HospitalView.class.getResource("/styles/hospital.css").toExternalForm());

        stage.setTitle("Gestión de Hospitales");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    private static void actualizarTabla(TableView<Hospital> tabla,
                                         boolean soloActivos,
                                         String textoBusqueda,
                                         String campo) {
        String filtro = textoBusqueda == null ? "" : textoBusqueda.toLowerCase();

        List<Hospital> filtrados = hospitales.stream()
                .filter(h -> (!soloActivos || h.isActivo()))
                .filter(h -> {
                    if (filtro.isEmpty()) return true;
                    switch (campo) {
                        case "Dirección":
                            return h.getDireccion().toLowerCase().contains(filtro);
                        case "Teléfono":
                            return h.getTelefono().toLowerCase().contains(filtro);
                        default:
                            return h.getNombre().toLowerCase().contains(filtro);
                    }
                })
                .sorted((a, b) -> a.getNombre().compareToIgnoreCase(b.getNombre()))
                .collect(Collectors.toList());

        tabla.setItems(FXCollections.observableArrayList(filtrados));
    }

    private static void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private static void limpiarCampos(TextField... campos) {
        for (TextField campo : campos) {
            campo.clear();
        }
    }

    private static void recargarHospitalesDesdeBD() {
        hospitales.clear();
        hospitales.addAll(controller.obtenerTodos());
    }
}