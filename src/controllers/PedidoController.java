package controllers;

import jakarta.persistence.*;
import models.*;
import utils.JPAUtil;

import java.util.List;

public class PedidoController {

    public void crearPedido(Pedido pedido) {
        if (pedido.getDetalles() == null || pedido.getDetalles().isEmpty()) {
            throw new IllegalArgumentException("No se puede crear un pedido sin platillos.");
        }
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(pedido);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public List<Pedido> obtenerPedidosPorCliente(Cliente cliente) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            TypedQuery<Pedido> q = em.createQuery("SELECT p FROM Pedido p WHERE p.cliente = :cli", Pedido.class);
            q.setParameter("cli", cliente);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Pedido obtenerPedidoConPlatillos(int idPedido) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.find(Pedido.class, idPedido);
        } finally {
            em.close();
        }
    }

    public boolean eliminarPedidos(List<Pedido> pedidos) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            for (Pedido p : pedidos) {
                p = em.find(Pedido.class, p.getId());
                if (p != null) em.remove(p);
            }
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }
}
