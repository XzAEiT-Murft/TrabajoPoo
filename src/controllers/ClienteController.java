package controllers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import models.Cliente;
import utils.JPAUtil;

import java.util.List;

public class ClienteController {

    public void crearCliente(Cliente cliente) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(cliente);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally { em.close(); }
    }

    public void actualizarCliente(Cliente cliente) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(cliente);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally { em.close(); }
    }

    public boolean eliminarClientes(List<Cliente> lista) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            for (Cliente c : lista) {
                Cliente ref = em.find(Cliente.class, c.getId());
                if (ref != null) em.remove(ref);
            }
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        } finally { em.close(); }
    }

    public List<Cliente> obtenerTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Cliente> q = em.createQuery(
                "SELECT c FROM Cliente c ORDER BY c.id", Cliente.class);
            return q.getResultList();
        } finally { em.close(); }
    }

    public boolean existePorCorreo(String correo) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Long count = em.createQuery(
                "SELECT COUNT(c) FROM Cliente c WHERE LOWER(c.correo) = :correo", Long.class)
                .setParameter("correo", correo.toLowerCase())
                .getSingleResult();
            return count > 0;
        } finally { em.close(); }
    }
}
