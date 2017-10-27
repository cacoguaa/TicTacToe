package co.edu.unal.tictactoe;


public class GameStruc{

    public String pass;
    public String board = "000000000";
    public Boolean started;

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }
    public String getPass() {
        return pass;
    }

    public String getBoard() {
        return board;
    }

    public boolean isStarted() {
        return started;
    }

    public GameStruc(){
        this.pass = "";
        this.started = false;
    }
    public GameStruc(String pass, boolean started){
        this.pass = pass;
        this.started = started;
    }

    @Override
    public String toString() {
        return pass + " " + board + " " + started;
    }
}