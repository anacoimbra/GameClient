package puc.redes.dto;

import java.util.*;

/**
 * Created by aninh on 15/03/2016.
 */
public class Jogador {
    public int id;
    public String nome;
    public boolean turn;
    public int pontos = 0;

    public Jogador(int id, String nome){
        this.id = id;
        this.nome = nome;
        this.turn = false;
    }

    public Jogador(int id, String nome, boolean t){
        this.id = id;
        this.nome = nome;
        this.turn = t;
    }
}
