package org.example.Models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "pokemon")
public class Pokemon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nome", nullable = false, length = 50)
    private String nome;

    @Column(name = "nacemento")
    private LocalDate nacemento;

    @Column(name = "pokedexentry")
    private Integer pokedexEntry;

    @Column(name = "adestrador")
    private Integer adestrador;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


    public LocalDate getNacemento() {
        return nacemento;
    }

    public void setNacemento(LocalDate nacemento) {
        this.nacemento = nacemento;
    }

    public Integer getPokedexEntry() {
        return pokedexEntry;
    }

    public void setPokedexEntry(Integer pokedexEntry) {
        this.pokedexEntry = pokedexEntry;
    }

    public Integer getAdestrador() {
        return adestrador;
    }

    public void setAdestrador(Integer adestrador) {
        this.adestrador = adestrador;
    }

    @Override
    public String toString() {
        return "Pokemon{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", nacemento=" + nacemento +
                ", pokedexEntry=" + pokedexEntry +
                ", adestrador=" + adestrador +
                '}';
    }
}
