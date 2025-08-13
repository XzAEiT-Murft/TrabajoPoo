package views;

import controllers.ClienteController;
import controllers.PedidoController;
import controllers.PlatilloController;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.*;

public class PedidoView {

    private static final ClienteController clienteController = new ClienteController();
    private static final PlatilloController platilloController = new PlatilloController();
    private static final PedidoController pedidoController = new PedidoController();

    public static void mostrar(Stage stage) {
        ComboBox<Cliente> cmbCliente = new ComboBox<>();
        cmbCliente.getItems().addAll(clienteController.obtenerTodos());

        ComboBox<Platillo> cmbPlatillo = new ComboBox<>();
        cmbPlatillo.getItems().addAll(platilloController.obtenerTodos());

        TextField txtCantidad = new TextField();
        txtCantidad.setPromptText("Cantidad");

        Button btnAgregar = new Button("Agregar platillo");
        Button btnGuardar = new Button("Guardar pedido");

        ObservableList<PedidoDetalle> detalles = FXCollections.observableArrayList();
        TableView<PedidoDetalle> tabla = new TableView<>(detalles);

        TableColumn<PedidoDetalle, String> colPlatillo = new TableColumn<>("Platillo");
        colPlatillo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPlatillo().getNombre()));

        TableColumn<PedidoDetalle, Integer> colCantidad = new TableColumn<>("Cantidad");
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));

        TableColumn<PedidoDetalle, Double> colSubtotal = new TableColumn<>("Subtotal");
        colSubtotal.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getSubtotal()).asObject());

        tabla.getColumns().addAll(colPlatillo, colCantidad, colSubtotal);

        btnAgregar.setOnAction(e -> {
            Platillo plat = cmbPlatillo.getValue();
            if (plat == null) return;
            int cant;
            try {
                cant = Integer.parseInt(txtCantidad.getText());
            } catch (NumberFormatException ex) {
                mostrarAlerta("Cantidad inválida");
                return;
            }
            try {
                PedidoDetalle det = new PedidoDetalle(plat, cant);
                detalles.add(det);
                txtCantidad.clear();
            } catch (IllegalArgumentException ex) {
                mostrarAlerta(ex.getMessage());
            }
        });

        btnGuardar.setOnAction(e -> {
            Cliente cli = cmbCliente.getValue();
            if (cli == null) {
                mostrarAlerta("Selecciona un cliente");
                return;
            }
            Pedido pedido = new Pedido(cli);
            detalles.forEach(pedido::agregarDetalle);
            try {
                pedidoController.crearPedido(pedido);
                mostrarAlerta("Pedido guardado. Total: $" + pedido.getTotal());
                detalles.clear();
            } catch (IllegalArgumentException ex) {
                mostrarAlerta(ex.getMessage());
            }
        });

        VBox form = new VBox(10, cmbCliente, new HBox(5, cmbPlatillo, txtCantidad, btnAgregar), tabla, btnGuardar);
        form.setPadding(new Insets(10));

        stage.setScene(new Scene(form, 700, 500));
        stage.setTitle("Nuevo Pedido");
        stage.show();
    }

    private static void mostrarAlerta(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}