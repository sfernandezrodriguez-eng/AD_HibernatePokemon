package org.example.Services;

import org.example.Config.HibernateConfig;
import org.example.Models.Pokedex;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.util.List;

public class PokedexService {

    public Integer crearPokedex(String nome, BigDecimal peso, String misc) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Pokedex pokedex = new Pokedex();
            pokedex.setNome(nome);
            pokedex.setPeso(peso);
            pokedex.setMisc(misc);
            session.save(pokedex);
            transaction.commit();
            return pokedex.getId();
        }
    }

    public Integer crearPokedexConQuery(String nome, BigDecimal peso, String misc) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Number id = (Number) session.createNativeQuery(
                            "insert into pokedex (nome, peso, misc) values (:nome, :peso, :misc) returning id")
                    .setParameter("nome", nome)
                    .setParameter("peso", peso)
                    .setParameter("misc", misc)
                    .getSingleResult();
            transaction.commit();
            return id.intValue();
        }
    }

    public Pokedex leerPokedex(int id) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.get(Pokedex.class, id);
        }
    }

    public Pokedex leerPokedexConQuery(int id) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.createQuery("from Pokedex where id = :id", Pokedex.class)
                    .setParameter("id", id)
                    .uniqueResult();
        }
    }

    public List<Pokedex> listarPokedex() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.createQuery("from Pokedex order by id", Pokedex.class).list();
        }
    }

    public List<Pokedex> listarPokedexConQuery() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.createQuery("from Pokedex order by id", Pokedex.class).list();
        }
    }

    public void updatePokedexConQuery(int id, String nome, BigDecimal peso, String misc) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            int actualizados = session.createQuery(
                            "update Pokedex set nome = :nome, peso = :peso, misc = :misc where id = :id")
                    .setParameter("nome", nome)
                    .setParameter("peso", peso)
                    .setParameter("misc", misc)
                    .setParameter("id", id)
                    .executeUpdate();
            if (actualizados == 0) {
                throw new IllegalArgumentException("No existe una entrada Pokedex con id " + id);
            }
            transaction.commit();
        }
    }

    public void updatePokedex(int id, String nome, BigDecimal peso, String misc) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Pokedex pokedex = session.get(Pokedex.class, id);
            if (pokedex == null) {
                throw new IllegalArgumentException("No existe una entrada Pokedex con id " + id);
            }
            pokedex.setNome(nome);
            pokedex.setPeso(peso);
            pokedex.setMisc(misc);
            transaction.commit();
        }
    }

    public void borrarPokedex(int id) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Pokedex pokedex = session.get(Pokedex.class, id);
            if (pokedex != null) {
                session.delete(pokedex);
            }
            transaction.commit();
        }
    }

    public int borrarTodosLosPokedex() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            int eliminados = session.createQuery("delete from Pokedex").executeUpdate();
            transaction.commit();
            return eliminados;
        }
    }

    public int borrarTodosLosPokedexConQuery() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            int eliminados = session.createQuery("delete from Pokedex").executeUpdate();
            transaction.commit();
            return eliminados;
        }
    }
}
