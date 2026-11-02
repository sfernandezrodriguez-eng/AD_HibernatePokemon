package org.example;

import org.example.Config.HibernateConfig;
import org.example.Models.Pokedex;
import org.example.Services.PokedexService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final String[] NAMES = {
            "Bulbasaur", "Ivysaur", "Venusaur", "Charmander", "Charmeleon",
            "Charizard", "Squirtle", "Wartortle", "Blastoise", "Pikachu"
    };

    private static final BigDecimal[] WEIGHTS = {
            new BigDecimal("6.90"), new BigDecimal("13.00"), new BigDecimal("100.00"),
            new BigDecimal("8.50"), new BigDecimal("19.00"), new BigDecimal("90.50"),
            new BigDecimal("9.00"), new BigDecimal("22.50"), new BigDecimal("85.50"),
            new BigDecimal("6.00")
    };

    public static void main(String[] args) {
        PokedexService service = new PokedexService();
        try {
            executeSequence(service, false);
            executeSequence(service, true);
        } finally {
            HibernateConfig.shutdown();
        }
    }

    private static void executeSequence(PokedexService service, boolean queries) {
        System.out.println();
        System.out.println(queries
                ? "Secuencia executada con consultas HQL e SQL:"
                : "Secuencia executada con obxectos e métodos Hibernate:");

        System.out.println("Inserir 10 entradas na táboa Pokedex.");
        List<Integer> ids = new ArrayList<Integer>();
        for (int i = 0; i < NAMES.length; i++) {
            ids.add(queries
                    ? service.crearPokedexConQuery(
                            NAMES[i], WEIGHTS[i], "Entrada de exemplo da Pokédex")
                    : service.crearPokedex(
                            NAMES[i], WEIGHTS[i], "Entrada de exemplo da Pokédex"));
        }

        System.out.println("Listar os pokemons na Pokédex.");
        printPokedex(queries ? service.listarPokedexConQuery() : service.listarPokedex());

        System.out.println("Modificar dúas entradas na Pokédex.");
        for (int i = 0; i < 2; i++) {
            String name = NAMES[i] + " actualizado";
            BigDecimal weight = WEIGHTS[i].add(new BigDecimal("0.20"));
            if (queries) {
                service.updatePokedexConQuery(ids.get(i), name, weight, "Entrada modificada");
            } else {
                service.updatePokedex(ids.get(i), name, weight, "Entrada modificada");
            }
        }

        System.out.println("Volver listar os pokemons na Pokédex.");
        printPokedex(queries ? service.listarPokedexConQuery() : service.listarPokedex());

        System.out.println("Eliminar todas as entradas da Pokédex.");
        if (queries) {
            service.borrarTodosLosPokedexConQuery();
        } else {
            for (Pokedex entry : service.listarPokedex()) {
                service.borrarPokedex(entry.getId());
            }
        }
    }

    private static void printPokedex(List<Pokedex> entries) {
        for (Pokedex entry : entries) {
            System.out.println(entry);
        }
    }
}
