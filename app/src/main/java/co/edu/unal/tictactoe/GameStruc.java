package co.edu.unal.tictactoe;

import java.io.Serializable;
import java.util.Random;

public class GameStruc implements Serializable{

    public String id;
    public String name;
    public String pass;
    public String board = "         ";
    public String player1;
    public String player2;
    public Boolean started;
    public int turn;

    public GameStruc(){
        this.id = null;
        this.pass = "";
        this.name = "";
        this.started = false;
        this.player1 = null;
        this.player2 = null;
        this.turn = turn();
    }
    public GameStruc(String name, String pass, boolean started, String player1){
        this.id = null;
        this.name = name;
        this.pass = pass;
        this.started = started;
        this.player1 =player1;
        this.player2 = null;
        this.turn = turn();
    }

    public GameStruc(String name, String pass, boolean started, String player1, String player2){
        this.id = null;
        this.name = name;
        this.pass = pass;
        this.started = started;
        this.player1 =player1;
        this.player2 = player2;
        this.turn = turn();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public Boolean getStarted() {
        return started;
    }

    public void setStarted(Boolean started) {
        this.started = started;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    @Override
    public String toString() {
        return pass + " " + board + " " + started;
    }
    public int turn(){
        Random randomGenerator = new Random();
        int randomInt = 0;
        for (int idx = 1; idx <= 10; ++idx){
            randomInt = randomGenerator.nextInt(2)+ 1;
        }
        return randomInt;
    }
}