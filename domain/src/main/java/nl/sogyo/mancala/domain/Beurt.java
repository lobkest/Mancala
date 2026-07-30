package nl.sogyo.mancala.domain;

public class Beurt {
    private int beurtPlayer;

    public Beurt(){
        this.beurtPlayer = 1;
    }

    public int getWhichPlayerIsNow(){
        return this.beurtPlayer;
    }

    public void setChangeBeurt(){
        if (this.beurtPlayer == 1) {
            this.beurtPlayer = 2;
        } else if (this.beurtPlayer == 2){
            this.beurtPlayer = 1;
        }
    }
}