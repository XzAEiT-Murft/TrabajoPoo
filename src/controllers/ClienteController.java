package controllers;

import jakarta.persistence.*;
import models.Cliente;
import utils.JPAUtil;

import java.util.List;

public class ClienteController {

    public void crearCliente(Cliente cliente) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(cliente);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void actualizarCliente(Cliente cliente) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(cliente);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public boolean eliminarClientes(List<Cliente> lista) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            for (Cliente c : lista) {
                c = em.find(Cliente.class, c.getId());
                if (c != null) em.remove(c);
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

    public List<Cliente> obtenerClientesActivos() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        List<Cliente> lista = em.createQuery("SELECT c FROM Cliente c WHERE c.activo = true", Cliente.class).getResultList();
        em.close();
        return lista;
    }

    public List<Cliente> obtenerTodos() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        List<Cliente> lista = em.createQuery("SELECT c FROM Cliente c", Cliente.class).getResultList();
        em.close();
        return lista;
    }

    public boolean existePorCorreo(String correo) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            Long count = em.createQuery(
                    "SELECT COUNT(c) FROM Cliente c WHERE LOWER(c.correo) = :correo",
                    Long.class)
                .setParameter("correo", correo.toLowerCase())
                .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }
}