package controllers;

import jakarta.persistence.*;
import models.Platillo;
import utils.JPAUtil;

import java.util.List;

public class PlatilloController {

    public void crearPlatillo(Platillo platillo) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(platillo);
            tx.commit();
            System.out.println("Platillo guardado correctamente.");
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void actualizarPlatillo(Platillo platillo) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(platillo);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public boolean eliminarPlatillos(List<Platillo> lista) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            for (Platillo p : lista) {
                p = em.find(Platillo.class, p.getId());
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

    public List<Platillo> obtenerTodos() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            TypedQuery<Platillo> query = em.createQuery("SELECT p FROM Platillo p", Platillo.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public boolean existePorNombre(String nombre) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            Long count = em.createQuery(
                    "SELECT COUNT(p) FROM Platillo p WHERE LOWER(p.nombre) = :nom",
                    Long.class)
                .setParameter("nom", nombre.toLowerCase())
                .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }
}