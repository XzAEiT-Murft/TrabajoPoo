package controllers;

import javafx.scene.control.Alert;
import models.Hospital;

public class PacienteValidator {

    public static boolean validar(String nombre, String apellido1, String apellido2, String edadStr, String genero, Hospital hospital) {
        // Verificación de campos obligatorios
        if (nombre.isEmpty() || apellido1.isEmpty()) {
            mostrarAlerta("Nombre y apellido paterno son obligatorios.");
            return false;
        }

        // Validar edad como número entero y mayor o igual a 0
        int edad;
        try {
            edad = Integer.parseInt(edadStr);
            if (edad < 0) {
                mostrarAlerta("La edad debe ser un número positivo.");
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Edad inválida. Introduce un número entero.");
            return false;
        }

        // Validar género
        if (genero == null || genero.isEmpty()) {
            mostrarAlerta("Debes seleccionar un género.");
            return false;
        }

        // Validar hospital asignado
        if (hospital == null) {
            mostrarAlerta("Selecciona un hospital para registrar al paciente.");
            return false;
        }

        return true;
    }

    private static void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validación de datos");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
