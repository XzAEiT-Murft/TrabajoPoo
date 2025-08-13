package views;

import controllers.PacienteValidator;
import controllers.PacienteController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import models.Hospital;
import models.Paciente;
import java.util.List;
import java.util.stream.Collectors;


public class PacienteView {

    private static final ObservableList<Paciente> pacientes = FXCollections.observableArrayList();
    private static final PacienteController controller = new PacienteController();

    public static void mostrar(Stage stage, ObservableList<Hospital> hospitales) {
        // Campos de entrada
        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Nombre");

        TextField txtApellido1 = new TextField();
        txtApellido1.setPromptText("Primer Apellido");

        TextField txtApellido2 = new TextField();
        txtApellido2.setPromptText("Segundo Apellido");

        TextField txtEdad = new TextField();
        txtEdad.setPromptText("Edad");

        ComboBox<String> cmbGenero = new ComboBox<>();
        cmbGenero.getItems().addAll("Masculino", "Femenino", "Otro");
        cmbGenero.setPromptText("Género");

        ComboBox<Hospital> cmbHospital = new ComboBox<>(hospitales);
        cmbHospital.setPromptText("Hospital");

        // Barra de búsqueda y filtros
        TextField txtBusqueda = new TextField();
        txtBusqueda.setPromptText("Buscar...");
        ComboBox<String> cmbFiltro = new ComboBox<>();
        cmbFiltro.getItems().addAll("Nombre", "Apellido", "Hospital");
        cmbFiltro.setValue("Nombre");

        CheckBox chkSoloActivos = new CheckBox("Mostrar solo activos");

        Label lblMensaje = new Label();

        Button btnAgregar = new Button("Agregar");
        Button btnEditar = new Button("Editar");
        Button btnBaja = new Button("Dar de Baja");
        Button btnReactivar = new Button("Reactivar");
        Button btnEliminar = new Button("Eliminar");
        Button btnRegresar = new Button("Regresar");

        // Datos
        recargarPacientesDesdeBD();

        TableView<Paciente> tabla = new TableView<>();
        tabla.setItems(pacientes);
        tabla.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        tabla.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                txtNombre.setText(newSel.getNombrePila());
                txtApellido1.setText(newSel.getApellido1());
                txtApellido2.setText(newSel.getApellido2());
                txtEdad.setText(String.valueOf(newSel.getEdad()));
                cmbGenero.setValue(newSel.getGenero());
                cmbHospital.setValue(newSel.getHospital());
            }
        });

        TableColumn<Paciente, String> colNombre = new TableColumn<>("Nombre Completo");
        colNombre.setCellValueFactory(p -> new SimpleStringProperty(
                p.getValue().getNombrePila() + " " + p.getValue().getApellido1() +
                        (p.getValue().getApellido2() != null && !p.getValue().getApellido2().isEmpty() ? " " + p.getValue().getApellido2() : "")));

        TableColumn<Paciente, String> colHospital = new TableColumn<>("Hospital");
        colHospital.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getHospital().getNombre()));

        TableColumn<Paciente, Integer> colEdad = new TableColumn<>("Edad");
        colEdad.setCellValueFactory(new PropertyValueFactory<>("edad"));

        TableColumn<Paciente, String> colGenero = new TableColumn<>("Género");
        colGenero.setCellValueFactory(new PropertyValueFactory<>("genero"));

        TableColumn<Paciente, Boolean> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(new PropertyValueFactory<>("activo"));
        colEstado.setCellFactory(col -> new TableCell<Paciente, Boolean>() {
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

        tabla.getColumns().addAll(colNombre, colHospital, colEdad, colGenero, colEstado);

        // Acciones
        btnAgregar.setOnAction(e -> {
            if (!PacienteValidator.validar(txtNombre.getText(), txtApellido1.getText(), txtApellido2.getText(),
                    txtEdad.getText(), cmbGenero.getValue(), cmbHospital.getValue())) {
                return;
            }
            int edad = Integer.parseInt(txtEdad.getText());
            Paciente p = new Paciente(txtNombre.getText().trim(), txtApellido1.getText().trim(),
                    txtApellido2.getText().trim(), edad, cmbGenero.getValue(), cmbHospital.getValue());
            controller.crearPaciente(p);
            limpiarCampos(txtNombre, txtApellido1, txtApellido2, txtEdad);
            cmbGenero.setValue(null);
            cmbHospital.setValue(null);
            recargarPacientesDesdeBD();
            actualizarTabla(tabla, chkSoloActivos.isSelected(), txtBusqueda.getText(), cmbFiltro.getValue());
            lblMensaje.setText("Paciente registrado correctamente.");
        });

        btnEditar.setOnAction(e -> {
            Paciente sel = tabla.getSelectionModel().getSelectedItem();
            if (sel == null) {
                mostrarAlerta("Sin selección", "Selecciona un paciente para editar.");
                return;
            }
            if (!PacienteValidator.validar(txtNombre.getText(), txtApellido1.getText(), txtApellido2.getText(),
                    txtEdad.getText(), cmbGenero.getValue(), cmbHospital.getValue())) {
                return;
            }
            sel.setNombrePila(txtNombre.getText());
            sel.setApellido1(txtApellido1.getText());
            sel.setApellido2(txtApellido2.getText());
            sel.setEdad(Integer.parseInt(txtEdad.getText()));
            sel.setGenero(cmbGenero.getValue());
            sel.setHospital(cmbHospital.getValue());
            controller.actualizarPaciente(sel);
            limpiarCampos(txtNombre, txtApellido1, txtApellido2, txtEdad);
            cmbGenero.setValue(null);
            cmbHospital.setValue(null);
            recargarPacientesDesdeBD();
            actualizarTabla(tabla, chkSoloActivos.isSelected(), txtBusqueda.getText(), cmbFiltro.getValue());
        });

        btnEliminar.setOnAction(e -> {
            List<Paciente> seleccionados = tabla.getSelectionModel().getSelectedItems();
            if (seleccionados.isEmpty()) {
                mostrarAlerta("Sin selección", "Selecciona al menos un paciente para eliminar.");
                return;
            }
            controller.eliminarPacientes(seleccionados);
            recargarPacientesDesdeBD();
            actualizarTabla(tabla, chkSoloActivos.isSelected(), txtBusqueda.getText(), cmbFiltro.getValue());
        });

        btnBaja.setOnAction(e -> {
            List<Paciente> seleccionados = tabla.getSelectionModel().getSelectedItems();
            if (seleccionados.isEmpty()) {
                mostrarAlerta("Sin selección", "Selecciona al menos un paciente para dar de baja.");
                return;
            }
            for (Paciente p : seleccionados) {
                p.setActivo(false);
                controller.actualizarPaciente(p);
            }
            recargarPacientesDesdeBD();
            actualizarTabla(tabla, chkSoloActivos.isSelected(), txtBusqueda.getText(), cmbFiltro.getValue());
        });

        btnReactivar.setOnAction(e -> {
            List<Paciente> seleccionados = tabla.getSelectionModel().getSelectedItems();
            if (seleccionados.isEmpty()) {
                mostrarAlerta("Sin selección", "Selecciona al menos un paciente para reactivar.");
                return;
            }
            for (Paciente p : seleccionados) {
                p.setActivo(true);
                controller.actualizarPaciente(p);
            }
            recargarPacientesDesdeBD();
            actualizarTabla(tabla, chkSoloActivos.isSelected(), txtBusqueda.getText(), cmbFiltro.getValue());
        });

        // Búsqueda
        txtBusqueda.textProperty().addListener((obs, oldVal, newVal) ->
                actualizarTabla(tabla, chkSoloActivos.isSelected(), newVal, cmbFiltro.getValue()));

        cmbFiltro.setOnAction(e -> actualizarTabla(tabla, chkSoloActivos.isSelected(), txtBusqueda.getText(), cmbFiltro.getValue()));

        chkSoloActivos.setOnAction(e -> actualizarTabla(tabla, chkSoloActivos.isSelected(), txtBusqueda.getText(), cmbFiltro.getValue()));

        btnRegresar.setOnAction(e -> MainView.mostrar(stage));

        HBox menuBusqueda = new HBox(10, txtBusqueda, cmbFiltro);
        menuBusqueda.setAlignment(Pos.CENTER_LEFT);

        VBox campos = new VBox(10, txtNombre, txtApellido1, txtApellido2, txtEdad, cmbGenero, cmbHospital, chkSoloActivos, menuBusqueda);
        HBox botones = new HBox(10, btnAgregar, btnEditar, btnBaja, btnReactivar, btnEliminar, btnRegresar);
        VBox root = new VBox(15, campos, botones, lblMensaje, tabla);
        root.setPadding(new Insets(20));

        Scene scene = new Scene(root, 1000, 700);
        scene.getStylesheets().add(PacienteView.class.getResource("/styles/paciente.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("Gestión de Pacientes");
        stage.setMaximized(true);
        stage.show();
    }

    private static void actualizarTabla(TableView<Paciente> tabla,
                                         boolean soloActivos,
                                         String textoBusqueda,
                                         String campo) {
        String filtro = textoBusqueda == null ? "" : textoBusqueda.toLowerCase();

        List<Paciente> filtrados = pacientes.stream()
                .filter(p -> (!soloActivos || p.isActivo()))
                .filter(p -> {
                    if (filtro.isEmpty()) return true;
                    switch (campo) {
                        case "Apellido":
                            return p.getApellido1().toLowerCase().contains(filtro) ||
                                   (p.getApellido2() != null && p.getApellido2().toLowerCase().contains(filtro));
                        case "Hospital":
                            return p.getHospital().getNombre().toLowerCase().contains(filtro);
                        default:
                            return p.getNombrePila().toLowerCase().contains(filtro);
                    }
                })
                .sorted((a, b) -> a.getNombrePila().compareToIgnoreCase(b.getNombrePila()))
                .collect(Collectors.toList());

        tabla.setItems(FXCollections.observableArrayList(filtrados));
    }

    private static void limpiarCampos(TextField... campos) {
        for (TextField c : campos) {
            c.clear();
        }
    }

    private static void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private static void recargarPacientesDesdeBD() {
        pacientes.clear();
        pacientes.addAll(controller.obtenerTodos());
    }
}