package models;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Platillos")
public class Platillo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nombre;
    private double precio;
    @Column(name = "FechaRegistro")
    private LocalDate fechaAlta;
    private boolean activo;

    public Platillo() {}

    public Platillo(String nombre, double precio) {
        this.nombre = nombre;
        this.precio = precio;
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

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
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