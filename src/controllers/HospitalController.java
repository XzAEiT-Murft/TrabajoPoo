package controllers;

import jakarta.persistence.*;
import models.Hospital;
import utils.JPAUtil;

import java.util.List;

public class HospitalController {

    public void crearHospital(Hospital hospital) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(hospital);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void actualizarHospital(Hospital hospital) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(hospital);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public boolean eliminarHospitales(List<Hospital> lista) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            for (Hospital h : lista) {
                h = em.find(Hospital.class, h.getId());
                if (h != null) em.remove(h);
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

    public List<Hospital> obtenerHospitalesActivos() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        List<Hospital> lista = em.createQuery("SELECT h FROM Hospital h WHERE h.activo = true", Hospital.class).getResultList();
        em.close();
        return lista;
    }

    // HospitalController.java
    public List<Hospital> obtenerTodos() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        List<Hospital> lista = em.createQuery("SELECT h FROM Hospital h", Hospital.class).getResultList();
        em.close();
        return lista;
    }

    public boolean existePorNombre(String nombre) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            Long count = em.createQuery(
                    "SELECT COUNT(h) FROM Hospital h WHERE LOWER(h.nombre) = :nom",
                    Long.class)
                .setParameter("nom", nombre.toLowerCase())
                .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }
}