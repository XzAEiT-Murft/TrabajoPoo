package controllers;

import models.Hospital;

public class HospitalValidator {

    public static String validar(Hospital h) {
        if (h.getNombre() == null || h.getNombre().trim().isEmpty()) {
            return "El nombre no puede estar vacío.";
        }
        if (h.getTelefono() == null || h.getTelefono().trim().isEmpty()) {
            return "El teléfono no puede estar vacío.";
        }
        if (h.getDireccion() == null || h.getDireccion().trim().isEmpty()) {
            return "La dirección no puede estar vacía.";
        }

        return ""; // ← Nunca debe retornar null
    }
}
