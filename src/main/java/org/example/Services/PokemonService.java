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

    public Pokemon leerPokemon(int id) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.get(Pokemon.class, id);
        }
    }

    public List<Pokemon> listarPokemon() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.createQuery("from Pokemon order by id", Pokemon.class).list();
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
}
