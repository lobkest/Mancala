package nl.sogyo.mancala.domain;

public class Beurt {
    private int beurtPlayer;

    public Beurt(){
        this.beurtPlayer = 1;
//        this.beurtPlayer = (int)(Math.random()*1)+1; // als ik random wil selecteren wie begint
    }

    protected int getWhichPlayerIsNow(){
        return this.beurtPlayer;
    }

    protected void setChangeBeurt(){
        if (this.beurtPlayer == 1) {
            this.beurtPlayer = 2;
        } else {
            this.beurtPlayer = 1;
        }
    }

    public boolean isTurnOf(int player) {
        return this.beurtPlayer == player;
    }

    // only for the tests, never to be used for interface
    protected void setPlayerToNumber(int playerNr){
        this.beurtPlayer = playerNr;
    }

    protected void setGameOver(){
        this.beurtPlayer = 0;
    }
}