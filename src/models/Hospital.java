package models;

import java.time.LocalDate;
import jakarta.persistence.*;

@Entity
@Table(name = "Hospitales")
public class Hospital {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nombre;
    private String direccion;
    private String telefono;
    @Column(name = "FechaRegistro") // <- Enlaza con el nombre real de la columna
    private LocalDate fechaAlta;
    private boolean activo;

    // 🔧 Constructor vacío requerido por JPA
    public Hospital() {
    }

    // Constructor sin ID (para cuando se crea un nuevo hospital)
    public Hospital(String nombre, String direccion, String telefono) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.fechaAlta = LocalDate.now(); // Asignamos la fecha actual como fecha de alta
        this.activo = true; // Por defecto está activo al ser registrado
    }

    // Constructor completo (opcional, por si lo necesitas con ID)
    public Hospital(int id, String nombre, String direccion, String telefono, LocalDate fechaAlta, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.fechaAlta = fechaAlta;
        this.activo = activo;
    }

    // Getters y Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public LocalDate getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(LocalDate fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
