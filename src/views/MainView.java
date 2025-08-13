package views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainView {

    public static void mostrar(Stage stage) {
        Button btnClientes = new Button("Gestionar Clientes");
        Button btnPlatillos = new Button("Gestionar Platillos");
        Button btnPedidos = new Button("Gestionar Pedidos");
        Button btnSalir = new Button("Salir");

        btnClientes.setOnAction(e -> ClienteView.mostrar(stage));
        btnPlatillos.setOnAction(e -> PlatilloView.mostrar(stage));
        btnPedidos.setOnAction(e -> PedidoView.mostrar(stage));
                btnSalir.setOnAction(e -> stage.close());

        VBox layout = new VBox(20, btnClientes, btnPlatillos, btnPedidos, btnSalir);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout, 800, 600);
        scene.getStylesheets().add(MainView.class.getResource("/styles/main.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("Delicias Gourmet");
        stage.setMaximized(true);
        stage.show();
    }
}