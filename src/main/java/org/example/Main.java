package org.example;

import org.example.Config.HibernateConfig;
import org.example.Models.Adestrador;
import org.example.Models.Pokedex;
import org.example.Models.Pokemon;
import org.example.Services.AdestradorService;
import org.example.Services.PokedexService;
import org.example.Services.PokemonService;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import java.io.BufferedWriter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final String[] POKEDEX_NAMES = {
            "Bulbasaur", "Ivysaur", "Venusaur", "Charmander", "Charmeleon",
            "Charizard", "Squirtle", "Wartortle", "Blastoise", "Pikachu"
    };

    private static final BigDecimal[] POKEDEX_WEIGHTS = {
            new BigDecimal("6.90"), new BigDecimal("13.00"), new BigDecimal("100.00"),
            new BigDecimal("8.50"), new BigDecimal("19.00"), new BigDecimal("90.50"),
            new BigDecimal("9.00"), new BigDecimal("22.50"), new BigDecimal("85.50"),
            new BigDecimal("6.00")
    };

    private static final String[] POKEMON_NAMES = {
            "Bulbasaur de Ash", "Ivysaur de Ash", "Pikachu", "Charmander de Ash",
            "Squirtle de Ash", "Charizard de Ash", "Staryu", "Psyduck",
            "Vulpix", "Poliwag", "Horsea", "Raichu"
    };

    public static void main(String[] args) throws Exception {
        PokedexService pokedexService = new PokedexService();
        AdestradorService adestradorService = new AdestradorService();
        PokemonService pokemonService = new PokemonService();

        try {
            ejecutarSecuencia(false, pokedexService, adestradorService, pokemonService);
            ejecutarSecuencia(true, pokedexService, adestradorService, pokemonService);
        } finally {
            HibernateConfig.shutdown();
        }
    }

    private static void ejecutarSecuencia(boolean consultas, PokedexService pokedexService,
                                          AdestradorService adestradorService,
                                          PokemonService pokemonService) throws Exception {
        System.out.println();
        System.out.println(consultas
                ? "Secuencia ejecutada con consultas HQL y SQL:"
                : "Secuencia ejecutada con objetos y métodos Hibernate:");

        System.out.println("Insertar 10 entradas na táboa Pokedex.");
        List<Integer> pokedexIds = new ArrayList<Integer>();
        for (int i = 0; i < POKEDEX_NAMES.length; i++) {
            pokedexIds.add(consultas
                    ? pokedexService.crearPokedexConQuery(POKEDEX_NAMES[i], POKEDEX_WEIGHTS[i],
                    "Entrada de exemplo da Pokédex")
                    : pokedexService.crearPokedex(POKEDEX_NAMES[i], POKEDEX_WEIGHTS[i],
                    "Entrada de exemplo da Pokédex"));
        }

        System.out.println("Insertar 2 adestradores na táboa Adestrador.");
        List<Integer> adestradorIds = new ArrayList<Integer>();
        adestradorIds.add(consultas
                ? adestradorService.crearAdestradorConQuery(
                "Ash Ketchum", LocalDate.of(2007, 5, 22))
                : adestradorService.crearAdestrador(
                "Ash Ketchum", LocalDate.of(2007, 5, 22)));
        adestradorIds.add(consultas
                ? adestradorService.crearAdestradorConQuery(
                "Misty", LocalDate.of(2007, 12, 12))
                : adestradorService.crearAdestrador(
                "Misty", LocalDate.of(2007, 12, 12)));

        System.out.println("Listar os pokemons da táboa Pokedex.");
        imprimirPokedex(consultas
                ? pokedexService.listarPokedexConQuery() : pokedexService.listarPokedex());

        System.out.println("Listar os adestradores.");
        imprimirAdestradores(consultas
                ? adestradorService.listarAdestradorConQuery()
                : adestradorService.listarAdestrador());

        System.out.println("Insertar 12 pokemons na táboa Pokemon: 6 para cada adestrador.");
        List<Integer> pokemonIds = new ArrayList<Integer>();
        for (int i = 0; i < POKEMON_NAMES.length; i++) {
            Integer adestradorId = adestradorIds.get(i < 6 ? 0 : 1);
            Integer pokedexId = pokedexIds.get(i % pokedexIds.size());
            LocalDate nacemento = LocalDate.of(2020, 1, 1).plusDays(i);
            pokemonIds.add(consultas
                    ? pokemonService.crearPokemonConQuery(
                    POKEMON_NAMES[i], nacemento, pokedexId, adestradorId)
                    : pokemonService.crearPokemon(
                    POKEMON_NAMES[i], nacemento, pokedexId, adestradorId));
        }

        System.out.println("Listar os pokemons da táboa Pokemon.");
        imprimirPokemon(consultas
                ? pokemonService.listarPokemonConQuery() : pokemonService.listarPokemon());

        System.out.println("Modificar dúas entradas da táboa Pokedex.");
        for (int i = 0; i < 2; i++) {
            String nome = POKEDEX_NAMES[i] + " actualizado";
            BigDecimal peso = POKEDEX_WEIGHTS[i].add(new BigDecimal("0.20"));
            if (consultas) {
                pokedexService.updatePokedexConQuery(
                        pokedexIds.get(i), nome, peso, "Entrada modificada");
            } else {
                pokedexService.updatePokedex(
                        pokedexIds.get(i), nome, peso, "Entrada modificada");
            }
        }

        System.out.println("Modificar os dous adestradores.");
        for (int i = 0; i < adestradorIds.size(); i++) {
            String nome = i == 0 ? "Ash Ketchum actualizado" : "Misty actualizada";
            LocalDate data = i == 0
                    ? LocalDate.of(2007, 5, 23) : LocalDate.of(2007, 12, 13);
            if (consultas) {
                adestradorService.updateAdestradorConQuery(adestradorIds.get(i), nome, data);
            } else {
                adestradorService.updateAdestrador(adestradorIds.get(i), nome, data);
            }
        }

        System.out.println("Modificar 4 pokemons.");
        for (int i = 0; i < 4; i++) {
            Integer adestradorId = adestradorIds.get(i < 2 ? 0 : 1);
            LocalDate data = LocalDate.of(2021, 1, 1).plusDays(i);
            if (consultas) {
                pokemonService.updatePokemonConQuery(pokemonIds.get(i),
                        POKEMON_NAMES[i] + " actualizado", data,
                        pokedexIds.get(i), adestradorId);
            } else {
                pokemonService.updatePokemon(pokemonIds.get(i),
                        POKEMON_NAMES[i] + " actualizado", data,
                        pokedexIds.get(i), adestradorId);
            }
        }

        System.out.println("Volver listar os pokemons da táboa Pokedex.");
        imprimirPokedex(consultas
                ? pokedexService.listarPokedexConQuery() : pokedexService.listarPokedex());

        System.out.println("Volver listar os adestradores.");
        imprimirAdestradores(consultas
                ? adestradorService.listarAdestradorConQuery()
                : adestradorService.listarAdestrador());

        System.out.println("Volver listar os pokemons da táboa Pokemon.");
        imprimirPokemon(consultas
                ? pokemonService.listarPokemonConQuery() : pokemonService.listarPokemon());

        System.out.println("Exportar os pokemons a pokemon.xml.");
        toXMLPokemons(consultas
                ? pokemonService.listarPokemonConQuery() : pokemonService.listarPokemon(),
                "pokemon.xml");

        System.out.println("Exportar os adestradores a adestradores.xml.");
        toXMLAdestradores(consultas
                ? adestradorService.listarAdestradorConQuery()
                : adestradorService.listarAdestrador(), "adestradores.xml");

        System.out.println("Exportar a táboa Pokedex a pokedex.xml.");
        toXMLPokedex(consultas
                ? pokedexService.listarPokedexConQuery() : pokedexService.listarPokedex(),
                "pokedex.xml");

        System.out.println("Eliminar todos os pokemons.");
        if (consultas) {
            pokemonService.borrarTodosLosPokemonConQuery();
        } else {
            for (Pokemon pokemon : pokemonService.listarPokemon()) {
                pokemonService.borrarPokemon(pokemon.getId());
            }
        }

        System.out.println("Eliminar todos os datos da táboa Pokedex.");
        if (consultas) {
            pokedexService.borrarTodosLosPokedexConQuery();
        } else {
            for (Pokedex pokedex : pokedexService.listarPokedex()) {
                pokedexService.borrarPokedex(pokedex.getId());
            }
        }

        System.out.println("Eliminar todos os datos da táboa Adestrador.");
        if (consultas) {
            adestradorService.borrarTodosLosAdestradoresConQuery();
        } else {
            for (Adestrador adestrador : adestradorService.listarAdestrador()) {
                adestradorService.borrarAdestrador(adestrador.getId());
            }
        }
    }

    private static void imprimirPokedex(List<Pokedex> entradas) {
        for (Pokedex entrada : entradas) {
            System.out.println(entrada);
        }
    }

    private static void imprimirAdestradores(List<Adestrador> adestradores) {
        for (Adestrador adestrador : adestradores) {
            System.out.println(adestrador);
        }
    }

    private static void imprimirPokemon(List<Pokemon> pokemons) {
        for (Pokemon pokemon : pokemons) {
            System.out.println(pokemon);
        }
    }

    private static void toXMLPokedex(List<Pokedex> entradas, String nomeFicheiro)
            throws Exception {
        try (BufferedWriter output = Files.newBufferedWriter(
                Paths.get(nomeFicheiro), StandardCharsets.UTF_8)) {
            XMLStreamWriter xml = XMLOutputFactory.newInstance().createXMLStreamWriter(output);
            try {
                xml.writeStartDocument("UTF-8", "1.0");
                xml.writeStartElement("pokedex");
                for (Pokedex entrada : entradas) {
                    xml.writeStartElement("entrada");
                    escreverElemento(xml, "id", String.valueOf(entrada.getId()));
                    escreverElemento(xml, "nome", entrada.getNome());
                    escreverElemento(xml, "peso",
                            entrada.getPeso() == null ? "" : entrada.getPeso().toPlainString());
                    escreverElemento(xml, "misc", entrada.getMisc());
                    xml.writeEndElement();
                }
                xml.writeEndElement();
                xml.writeEndDocument();
            } finally {
                xml.close();
            }
        }
    }

    private static void toXMLPokemons(List<Pokemon> pokemons, String nomeFicheiro)
            throws Exception {
        try (BufferedWriter output = Files.newBufferedWriter(
                Paths.get(nomeFicheiro), StandardCharsets.UTF_8)) {
            XMLStreamWriter xml = XMLOutputFactory.newInstance().createXMLStreamWriter(output);
            try {
                xml.writeStartDocument("UTF-8", "1.0");
                xml.writeStartElement("pokemons");
                for (Pokemon pokemon : pokemons) {
                    xml.writeStartElement("pokemon");
                    escreverElemento(xml, "id", String.valueOf(pokemon.getId()));
                    escreverElemento(xml, "nome", pokemon.getNome());
                    escreverElemento(xml, "nacemento",
                            pokemon.getNacemento() == null
                                    ? "" : pokemon.getNacemento().toString());
                    escreverElemento(xml, "pokedexEntry",
                            pokemon.getPokedexEntry() == null
                                    ? "" : pokemon.getPokedexEntry().toString());
                    escreverElemento(xml, "adestrador",
                            pokemon.getAdestrador() == null
                                    ? "" : pokemon.getAdestrador().toString());
                    xml.writeEndElement();
                }
                xml.writeEndElement();
                xml.writeEndDocument();
            } finally {
                xml.close();
            }
        }
    }

    private static void toXMLAdestradores(List<Adestrador> adestradores, String nomeFicheiro)
            throws Exception {
        try (BufferedWriter output = Files.newBufferedWriter(
                Paths.get(nomeFicheiro), StandardCharsets.UTF_8)) {
            XMLStreamWriter xml = XMLOutputFactory.newInstance().createXMLStreamWriter(output);
            try {
                xml.writeStartDocument("UTF-8", "1.0");
                xml.writeStartElement("adestradores");
                for (Adestrador adestrador : adestradores) {
                    xml.writeStartElement("adestrador");
                    escreverElemento(xml, "id", String.valueOf(adestrador.getId()));
                    escreverElemento(xml, "nome", adestrador.getNome());
                    escreverElemento(xml, "nacemento",
                            adestrador.getNacemento() == null
                                    ? "" : adestrador.getNacemento().toString());
                    xml.writeEndElement();
                }
                xml.writeEndElement();
                xml.writeEndDocument();
            } finally {
                xml.close();
            }
        }
    }

    private static void escreverElemento(XMLStreamWriter xml, String nome, String valor)
            throws Exception {
        xml.writeStartElement(nome);
        if (valor != null) {
            xml.writeCharacters(valor);
        }
        xml.writeEndElement();
    }
}
