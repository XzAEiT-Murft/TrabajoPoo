package views;

import jakarta.persistence.EntityManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Hospital;
import utils.JPAUtil;

import java.util.List;

public class MainView {

    public static void mostrar(Stage stage) {
        // 🔘 Botones principales del menú
        Button btnHospitales = new Button("Gestionar Hospitales");
         Button btnPacientes = new Button("Gestionar Pacientes");
        Button btnSalir = new Button("Salir");

        // 🎯 Acciones al hacer clic
        btnHospitales.setOnAction(e -> HospitalView.mostrar(stage));

        btnPacientes.setOnAction(e -> {
            // 📥 Obtener hospitales activos desde la base de datos
            EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
            List<Hospital> lista = em.createQuery("SELECT h FROM Hospital h WHERE h.activo = true", Hospital.class) 
                    .getResultList();
            ObservableList<Hospital> hospitales = FXCollections.observableArrayList(lista);
            em.close();

            // 👇 Ir a la vista de pacientes con la lista de hospitales
            PacienteView.mostrar(stage, hospitales);
        });

        btnSalir.setOnAction(e -> stage.close());

        // 🧱 Contenedor principal
        VBox layout = new VBox(20, btnHospitales, btnPacientes, btnSalir);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout, 800, 600);

        // 🎨 Aplicación del estilo CSS
        scene.getStylesheets().add(MainView.class.getResource("/styles/main.css").toExternalForm());

        // ⚙️ Configuración de la ventana
        stage.setScene(scene);
        stage.setTitle("Sistema de Hospitales");
        stage.setMaximized(true);
        stage.show();
    }
}