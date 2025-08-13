package views;

import controllers.PedidoController;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import models.Pedido;
import models.PedidoDetalle;

import java.time.format.DateTimeFormatter;

public class PedidoView {

    private final TableView<Pedido> tablaPedidos = new TableView<>();
    private final TableView<PedidoDetalle> tablaDetalles = new TableView<>();
    private final PedidoController controller = new PedidoController();
    private final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static void mostrar(Stage owner) {
        new PedidoView().show(owner);
    }

    private void show(Stage owner) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // ==========================
        // Tabla de Pedidos (encabezado)
        // ==========================
        TableColumn<Pedido, String> colId = new TableColumn<>("Pedido");
        colId.setCellValueFactory(cd -> new SimpleStringProperty(
                cd.getValue() != null && cd.getValue().getId() != null
                        ? cd.getValue().getId().toString() : ""
        ));
        colId.setPrefWidth(80);

        TableColumn<Pedido, String> colCliente = new TableColumn<>("Cliente");
        colCliente.setCellValueFactory(cd -> new SimpleStringProperty(
                cd.getValue() != null && cd.getValue().getCliente() != null
                        ? cd.getValue().getCliente().getNombre() : ""
        ));
        colCliente.setPrefWidth(220);

        TableColumn<Pedido, String> colFecha = new TableColumn<>("Fecha");
        colFecha.setCellValueFactory(cd -> new SimpleStringProperty(
                cd.getValue() != null && cd.getValue().getFecha() != null
                        ? cd.getValue().getFecha().format(FMT) : ""
        ));
        colFecha.setPrefWidth(160);

        // Usa Double exacto (nada de Number) y ReadOnlyObjectWrapper
        TableColumn<Pedido, Double> colTotal = new TableColumn<>("Total");
        colTotal.setCellValueFactory(cd -> new ReadOnlyObjectWrapper<>(
                cd.getValue() != null ? cd.getValue().getTotal() : 0.0
        ));
        colTotal.setPrefWidth(100);

        tablaPedidos.getColumns().setAll(colId, colCliente, colFecha, colTotal);

        // ==========================
        // Tabla de Detalles (líneas)
        // ==========================
        TableColumn<PedidoDetalle, String> colPlatillo = new TableColumn<>("Platillo");
        colPlatillo.setCellValueFactory(cd -> new SimpleStringProperty(
                cd.getValue() != null && cd.getValue().getPlatillo() != null
                        ? cd.getValue().getPlatillo().getNombre() : ""
        ));
        colPlatillo.setPrefWidth(250);

        // tipa exacto: Integer
        TableColumn<PedidoDetalle, Integer> colCant = new TableColumn<>("Cantidad");
        colCant.setCellValueFactory(cd -> new ReadOnlyObjectWrapper<>(
                cd.getValue() != null ? cd.getValue().getCantidad() : 0
        ));
        colCant.setPrefWidth(90);

        // tipa exacto: Double
        TableColumn<PedidoDetalle, Double> colPrecio = new TableColumn<>("Precio");
        colPrecio.setCellValueFactory(cd -> new ReadOnlyObjectWrapper<>(
                cd.getValue() != null && cd.getValue().getPlatillo() != null
                        ? cd.getValue().getPlatillo().getPrecio() : 0.0
        ));
        colPrecio.setPrefWidth(100);

        TableColumn<PedidoDetalle, Double> colSub = new TableColumn<>("Subtotal");
        colSub.setCellValueFactory(cd -> new ReadOnlyObjectWrapper<>(
                cd.getValue() != null ? cd.getValue().getSubtotal() : 0.0
        ));
        colSub.setPrefWidth(110);

        tablaDetalles.getColumns().setAll(colPlatillo, colCant, colPrecio, colSub);

        // ==========================
        // Datos
        // ==========================
        // Este método del controller DEBE usar JOIN FETCH (ya lo hicimos)
        tablaPedidos.getItems().setAll(controller.obtenerTodos());

        // al seleccionar encabezado → mostrar líneas
        tablaPedidos.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            tablaDetalles.getItems().clear();
            if (sel != null && sel.getDetalles() != null) {
                tablaDetalles.getItems().setAll(sel.getDetalles());
            }
        });

        if (!tablaPedidos.getItems().isEmpty()) {
            tablaPedidos.getSelectionModel().selectFirst();
        }

        SplitPane split = new SplitPane(tablaPedidos, tablaDetalles);
        split.setDividerPositions(0.55);

        Button cerrar = new Button("Cerrar");
        cerrar.setOnAction(e -> ((Stage) root.getScene().getWindow()).close());
        HBox actions = new HBox(8, cerrar);
        actions.setPadding(new Insets(10, 0, 0, 0));

        root.setCenter(split);
        root.setBottom(actions);

        Stage stage = new Stage();
        stage.setTitle("Pedidos");
        stage.initOwner(owner);
        stage.setScene(new Scene(root, 820, 520));
        stage.show();
    }
}
