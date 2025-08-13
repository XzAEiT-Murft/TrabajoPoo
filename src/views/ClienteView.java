package views;

import controllers.ClienteController;
import controllers.ClienteValidator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import models.Cliente;

import java.util.List;

public class ClienteView {

    private static final ObservableList<Cliente> clientes = FXCollections.observableArrayList();
    private static final ClienteController controller = new ClienteController();

    public static ObservableList<Cliente> getListaClientes() {
        return clientes;
    }

    public static void mostrar(Stage stage) {
        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Nombre");

        TextField txtCorreo = new TextField();
        txtCorreo.setPromptText("Correo");

        Button btnAgregar = new Button("Agregar");
        Button btnEliminar = new Button("Eliminar");

        TableView<Cliente> tabla = new TableView<>();
        tabla.setItems(clientes);

        TableColumn<Cliente, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        TableColumn<Cliente, String> colCorreo = new TableColumn<>("Correo");
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));

        tabla.getColumns().addAll(colNombre, colCorreo);

        btnAgregar.setOnAction(e -> {
            Cliente c = new Cliente(txtNombre.getText().trim(), txtCorreo.getText().trim());
            String error = ClienteValidator.validar(c);
            if (!error.isEmpty()) {
                mostrarAlerta("Validación", error);
                return;
            }
            if (controller.existePorCorreo(c.getCorreo())) {
                mostrarAlerta("Duplicado", "Ya existe un cliente con ese correo.");
                return;
            }
            controller.crearCliente(c);
            recargarClientesDesdeBD();
            txtNombre.clear();
            txtCorreo.clear();
        });

        btnEliminar.setOnAction(e -> {
            Cliente seleccionado = tabla.getSelectionModel().getSelectedItem();
            if (seleccionado == null) {
                mostrarAlerta("Sin selección", "Selecciona un cliente.");
                return;
            }
            controller.eliminarClientes(List.of(seleccionado));
            recargarClientesDesdeBD();
        });

        VBox form = new VBox(10, txtNombre, txtCorreo, new HBox(10, btnAgregar, btnEliminar));
        form.setPadding(new Insets(10));
        form.setAlignment(Pos.CENTER_LEFT);

        Button btnClientes = new Button("Clientes");
        btnClientes.setDisable(true);
        btnClientes.setOnAction(e -> ClienteView.mostrar(stage));
        Button btnPlatillos = new Button("Platillos");
        btnPlatillos.setOnAction(e -> PlatilloView.mostrar(stage));
        Button btnPedidos = new Button("Pedidos");
        btnPedidos.setOnAction(e -> PedidoView.mostrar(stage));
        Button btnSalir = new Button("Salir");
        btnSalir.setOnAction(e -> stage.close());

        HBox navBar = new HBox(10, btnClientes, btnPlatillos, btnPedidos, btnSalir);
        navBar.setAlignment(Pos.CENTER);
        navBar.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setTop(form);
        root.setCenter(tabla);
        root.setBottom(navBar);

        Scene scene = new Scene(root, 600, 400);
        stage.setTitle("Clientes");
        stage.setScene(scene);
        stage.show();

        recargarClientesDesdeBD();
    }

    private static void recargarClientesDesdeBD() {
        clientes.setAll(controller.obtenerTodos());
    }

    private static void mostrarAlerta(String titulo, String contenido) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}