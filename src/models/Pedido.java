package models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Pedidos")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "ClienteId")
    private Cliente cliente;

    private LocalDate fecha;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PedidoDetalle> detalles = new ArrayList<>();

    public Pedido() {}

    public Pedido(Cliente cliente) {
        this.cliente = cliente;
        this.fecha = LocalDate.now();
    }

    public void agregarDetalle(PedidoDetalle det) {
        detalles.add(det);
        det.setPedido(this);
    }

    public double getTotal() {
        return detalles.stream().mapToDouble(PedidoDetalle::getSubtotal).sum();
    }

    // Getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public List<PedidoDetalle> getDetalles() { return detalles; }
    public void setDetalles(List<PedidoDetalle> detalles) { this.detalles = detalles; }
}