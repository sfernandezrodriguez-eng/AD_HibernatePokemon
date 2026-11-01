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

    public Integer crearAdestradorConQuery(String nome, LocalDate nacemento) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Number id = (Number) session.createNativeQuery(
                            "insert into adestrador (nome, nacemento) values (:nome, :nacemento) returning id")
                    .setParameter("nome", nome)
                    .setParameter("nacemento", nacemento)
                    .getSingleResult();
            transaction.commit();
            return id.intValue();
        }
    }

    public Adestrador leerAdestrador(int id) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.get(Adestrador.class, id);
        }
    }

    public Adestrador leerAdestradorConQuery(int id) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.createQuery("from Adestrador where id = :id", Adestrador.class)
                    .setParameter("id", id)
                    .uniqueResult();
        }
    }

    public List<Adestrador> listarAdestrador() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.createQuery("from Adestrador order by id", Adestrador.class).list();
        }
    }

    public List<Adestrador> listarAdestradorConQuery() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.createQuery("from Adestrador order by id", Adestrador.class).list();
        }
    }

    public void updateAdestradorConQuery(int id, String nome, LocalDate nacemento) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            int actualizados = session.createQuery(
                            "update Adestrador set nome = :nome, nacemento = :nacemento where id = :id")
                    .setParameter("nome", nome)
                    .setParameter("nacemento", nacemento)
                    .setParameter("id", id)
                    .executeUpdate();
            if (actualizados == 0) {
                throw new IllegalArgumentException("No existe un Adestrador con id " + id);
            }
            transaction.commit();
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

    public int borrarTodosLosAdestradoresConQuery() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            int eliminados = session.createQuery("delete from Adestrador").executeUpdate();
            transaction.commit();
            return eliminados;
        }
    }
}
