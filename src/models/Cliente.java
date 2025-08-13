package models;

import java.time.LocalDate;
import jakarta.persistence.*;

@Entity
@Table(name = "Clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nombre;
    private String correo;
    @Column(name = "FechaRegistro")
    private LocalDate fechaAlta;
    private boolean activo;

    public Cliente() {
    }

    public Cliente(String nombre, String correo) {
        this.nombre = nombre;
        this.correo = correo;
        this.fechaAlta = LocalDate.now();
        this.activo = true;
    }

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

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
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
