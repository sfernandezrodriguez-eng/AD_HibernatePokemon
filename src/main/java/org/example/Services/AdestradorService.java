package org.example.Services;

import org.example.Config.HibernateConfig;
import org.example.Models.Adestrador;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.util.List;

public class AdestradorService {

    public Integer crearAdestrador(String nome, LocalDate nacemento) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Adestrador Adestrador = new Adestrador();
            Adestrador.setNome(nome);
            Adestrador.setNacemento(nacemento);
            session.save(Adestrador);
            transaction.commit();
            return Adestrador.getId();
        }
    }

    public Adestrador leerAdestrador(int id) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.get(Adestrador.class, id);
        }
    }

    public List<Adestrador> listarAdestrador() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.createQuery("from Adestrador order by id", Adestrador.class).list();
        }
    }

    public void updateAdestrador(int id, String nome, LocalDate nacemento) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Adestrador Adestrador = session.get(Adestrador.class, id);
            if (Adestrador == null) {
                throw new IllegalArgumentException("No existe un Adestrador con id " + id);
            }
            Adestrador.setNome(nome);
            Adestrador.setNacemento(nacemento);
            transaction.commit();
        }
    }

    public void borrarAdestrador(int id) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Adestrador Adestrador = session.get(Adestrador.class, id);
            if (Adestrador != null) {
                session.delete(Adestrador);
            }
            transaction.commit();
        }
    }
}
