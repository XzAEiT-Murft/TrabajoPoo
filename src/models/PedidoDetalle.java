package models;

import jakarta.persistence.*;

@Entity
@Table(name = "PedidoDetalle")
public class PedidoDetalle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "PedidoId")
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "PlatilloId")
    private Platillo platillo;

    private int cantidad;

    public PedidoDetalle() {}

    public PedidoDetalle(Platillo platillo, int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }
        this.platillo = platillo;
        this.cantidad = cantidad;
    }

    public double getSubtotal() {
        return platillo.getPrecio() * cantidad;
    }

    // Getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }

    public Platillo getPlatillo() { return platillo; }
    public void setPlatillo(Platillo platillo) { this.platillo = platillo; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }
        this.cantidad = cantidad;
    }
}