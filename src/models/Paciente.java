package models;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Pacientes") 
public class Paciente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nombrePila;
    private String apellido1;
    private String apellido2;
    private int edad;
    private String genero;
    @Column(name = "FechaRegistro")
    private LocalDate fechaAlta;
    private boolean activo;
    @ManyToOne
    @JoinColumn(name = "HospitalId")
    private Hospital hospital;

    // Constructor sin ID (para crear nuevos pacientes)
    public Paciente(String nombrePila, String apellido1, String apellido2, int edad, String genero, Hospital hospital) {
        this.nombrePila = nombrePila;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.edad = edad;
        this.genero = genero;
        this.hospital = hospital;
        this.fechaAlta = LocalDate.now(); // Fecha de registro
        this.activo = true; // Al registrar, está activo
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombrePila() {
        return nombrePila;
    }

    public void setNombrePila(String nombrePila) {
        this.nombrePila = nombrePila;
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
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

    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    // Constructor vacío para JPA
    public Paciente() {}

    @Override
    public String toString() {
        return nombrePila + " " + apellido1 + (apellido2 != null && !apellido2.isEmpty() ? " " + apellido2 : "");
    }
}