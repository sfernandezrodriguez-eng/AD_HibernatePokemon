package org.example.Services;

import org.example.Config.HibernateConfig;
import org.example.Models.Pokemon;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.util.List;

public class PokemonService {

    public Integer crearPokemon(String nome, LocalDate nacemento, Integer pokedexEntry, Integer adestrador) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Pokemon pokemon = new Pokemon();
            pokemon.setNome(nome);
            pokemon.setNacemento(nacemento);
            pokemon.setPokedexEntry(pokedexEntry);
            pokemon.setAdestrador(adestrador);
            session.save(pokemon);
            transaction.commit();
            return pokemon.getId();
        }
    }

    public Integer crearPokemonConQuery(String nome, LocalDate nacemento,
                                        Integer pokedexEntry, Integer adestrador) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Number id = (Number) session.createNativeQuery(
                            "insert into pokemon (nome, nacemento, pokedexentry, adestrador) " +
                                    "values (:nome, :nacemento, :pokedexentry, :adestrador) returning id")
                    .setParameter("nome", nome)
                    .setParameter("nacemento", nacemento)
                    .setParameter("pokedexentry", pokedexEntry)
                    .setParameter("adestrador", adestrador)
                    .getSingleResult();
            transaction.commit();
            return id.intValue();
        }
    }

    public Pokemon leerPokemon(int id) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.get(Pokemon.class, id);
        }
    }

    public Pokemon leerPokemonConQuery(int id) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.createQuery("from Pokemon where id = :id", Pokemon.class)
                    .setParameter("id", id)
                    .uniqueResult();
        }
    }

    public List<Pokemon> listarPokemon() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.createQuery("from Pokemon order by id", Pokemon.class).list();
        }
    }

    public List<Pokemon> listarPokemonConQuery() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.createQuery("from Pokemon order by id", Pokemon.class).list();
        }
    }

    public void updatePokemonConQuery(int id, String nome, LocalDate nacemento,
                                      Integer pokedexEntry, Integer adestrador) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            int actualizados = session.createQuery(
                            "update Pokemon set nome = :nome, nacemento = :nacemento, " +
                                    "pokedexEntry = :pokedexEntry, adestrador = :adestrador where id = :id")
                    .setParameter("nome", nome)
                    .setParameter("nacemento", nacemento)
                    .setParameter("pokedexEntry", pokedexEntry)
                    .setParameter("adestrador", adestrador)
                    .setParameter("id", id)
                    .executeUpdate();
            if (actualizados == 0) {
                throw new IllegalArgumentException("No existe un Pokemon con id " + id);
            }
            transaction.commit();
        }
    }

    public void updatePokemon(int id, String nome, LocalDate nacemento,
                              Integer pokedexEntry, Integer adestrador) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Pokemon pokemon = session.get(Pokemon.class, id);
            if (pokemon == null) {
                throw new IllegalArgumentException("No existe un Pokemon con id " + id);
            }
            pokemon.setNome(nome);
            pokemon.setNacemento(nacemento);
            pokemon.setPokedexEntry(pokedexEntry);
            pokemon.setAdestrador(adestrador);
            transaction.commit();
        }
    }

    public void borrarPokemon(int id) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Pokemon pokemon = session.get(Pokemon.class, id);
            if (pokemon != null) {
                session.delete(pokemon);
            }
            transaction.commit();
        }
    }

    public int borrarTodosLosPokemonConQuery() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            int eliminados = session.createQuery("delete from Pokemon").executeUpdate();
            transaction.commit();
            return eliminados;
        }
    }
}
