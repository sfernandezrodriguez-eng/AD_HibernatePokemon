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
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        PokedexService pokedexService = new PokedexService();
        AdestradorService adestradorService = new AdestradorService();
        PokemonService pokemonService = new PokemonService();

        try {
            System.out.println("Secuencia ejecutada con los métodos Hibernate disponibles:");

            System.out.println("Insertar 10 entradas en la tabla Pokedex.");
            String[] nomesPokedex = {
                    "Bulbasaur", "Ivysaur", "Venusaur", "Charmander", "Charmeleon",
                    "Charizard", "Squirtle", "Wartortle", "Blastoise", "Pikachu"
            };
            BigDecimal[] pesos = {
                    new BigDecimal("6.90"), new BigDecimal("13.00"), new BigDecimal("100.00"),
                    new BigDecimal("8.50"), new BigDecimal("19.00"), new BigDecimal("90.50"),
                    new BigDecimal("9.00"), new BigDecimal("22.50"), new BigDecimal("85.50"),
                    new BigDecimal("6.00")
            };
            List<Integer> pokedexIds = new ArrayList<Integer>();
            for (int i = 0; i < nomesPokedex.length; i++) {
                pokedexIds.add(pokedexService.crearPokedex(
                        nomesPokedex[i], pesos[i], "Entrada de exemplo da Pokédex"));
            }

            System.out.println("Insertar 2 adestradores na táboa Adestrador.");
            List<Integer> adestradorIds = new ArrayList<Integer>();
            adestradorIds.add(adestradorService.crearAdestrador(
                    "Ash Ketchum", LocalDate.of(2007, 5, 22)));
            adestradorIds.add(adestradorService.crearAdestrador(
                    "Misty", LocalDate.of(2007, 12, 12)));

            System.out.println("Listar os pokemons da táboa Pokedex.");
            imprimirPokedex(pokedexService.listarPokedex());

            System.out.println("Listar os adestradores.");
            imprimirAdestradores(adestradorService.listarAdestrador());

            System.out.println("Insertar 12 pokemons na táboa Pokemon: 6 para cada adestrador.");
            String[] nomesPokemon = {
                    "Bulbasaur de Ash", "Ivysaur de Ash", "Pikachu", "Charmander de Ash",
                    "Squirtle de Ash", "Charizard de Ash", "Staryu", "Psyduck",
                    "Vulpix", "Poliwag", "Horsea", "Raichu"
            };
            List<Integer> pokemonIds = new ArrayList<Integer>();
            for (int i = 0; i < nomesPokemon.length; i++) {
                Integer adestradorId = adestradorIds.get(i < 6 ? 0 : 1);
                Integer pokedexId = pokedexIds.get(i % pokedexIds.size());
                pokemonIds.add(pokemonService.crearPokemon(
                        nomesPokemon[i], LocalDate.of(2020, 1, 1).plusDays(i),
                        pokedexId, adestradorId));
            }

            System.out.println("Listar os pokemons da táboa Pokemon.");
            imprimirPokemon(pokemonService.listarPokemon());

            System.out.println("Modificar dúas entradas da táboa Pokedex.");
            pokedexService.updatePokedex(pokedexIds.get(0), "Bulbasaur actualizado",
                    new BigDecimal("7.10"), "Entrada modificada");
            pokedexService.updatePokedex(pokedexIds.get(1), "Ivysaur actualizado",
                    new BigDecimal("13.20"), "Entrada modificada");

            System.out.println("Modificar os dous adestradores.");
            adestradorService.updateAdestrador(adestradorIds.get(0),
                    "Ash Ketchum actualizado", LocalDate.of(2007, 5, 23));
            adestradorService.updateAdestrador(adestradorIds.get(1),
                    "Misty actualizada", LocalDate.of(2007, 12, 13));

            System.out.println("Modificar 4 pokemons.");
            for (int i = 0; i < 4; i++) {
                pokemonService.updatePokemon(pokemonIds.get(i),
                        nomesPokemon[i] + " actualizado",
                        LocalDate.of(2021, 1, 1).plusDays(i),
                        pokedexIds.get(i), adestradorIds.get(i < 2 ? 0 : 1));
            }

            System.out.println("Volver listar os pokemons da táboa Pokedex.");
            imprimirPokedex(pokedexService.listarPokedex());

            System.out.println("Volver listar os adestradores.");
            imprimirAdestradores(adestradorService.listarAdestrador());

            System.out.println("Volver listar os pokemons da táboa Pokemon.");
            imprimirPokemon(pokemonService.listarPokemon());

            System.out.println("Exportar a táboa Pokedex a pokedex.xml.");
            exportarPokedexXml(pokedexService.listarPokedex(), "pokedex.xml");

            System.out.println("Exportar a táboa Adestrador a adestradores.xml.");
            exportarAdestradoresXml(adestradorService.listarAdestrador(), "adestradores.xml");

            System.out.println("Eliminar todos os pokemons.");
            for (Pokemon pokemon : pokemonService.listarPokemon()) {
                pokemonService.borrarPokemon(pokemon.getId());
            }

            System.out.println("Eliminar todas as entradas da Pokedex.");
            pokedexService.borrarTodosLosPokedex();

            System.out.println("Eliminar todos os adestradores.");
            for (Adestrador adestrador : adestradorService.listarAdestrador()) {
                adestradorService.borrarAdestrador(adestrador.getId());
            }
        } finally {
            HibernateConfig.shutdown();
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

    private static void exportarPokedexXml(List<Pokedex> entradas, String nomeFicheiro)
            throws Exception {
        try (BufferedWriter output = Files.newBufferedWriter(
                Paths.get(nomeFicheiro), StandardCharsets.UTF_8)) {
            XMLStreamWriter xml = XMLOutputFactory.newInstance().createXMLStreamWriter(output);
            try {
                xml.writeStartDocument("UTF-8", "1.0");
                xml.writeStartElement("pokedex");
                for (Pokedex entrada : entradas) {
                    xml.writeStartElement("pokemon");
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
        } catch (IOException e) {
            throw new IOException("Non se puido escribir o ficheiro " + nomeFicheiro, e);
        }
    }

    private static void exportarAdestradoresXml(List<Adestrador> adestradores, String nomeFicheiro)
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
        } catch (IOException e) {
            throw new IOException("Non se puido escribir o ficheiro " + nomeFicheiro, e);
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
