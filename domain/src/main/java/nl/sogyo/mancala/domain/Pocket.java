package nl.sogyo.mancala.domain;

public class Pocket {
    final int pocketNr;
    int stones;
    Pocket nextPocket;

    public Pocket() {
        this.pocketNr = 1;
        this.stones = 4;
        this.nextPocket = new Pocket(this.pocketNr+1);
    }

    public Pocket(int pocketNr) {
        this.pocketNr = pocketNr;
        this.stones = 4;
//        this.nextPocket = new Pocket(this.pocketNr+1);
    }

    public static void main(String[] args) {

        System.out.println("Hello World!");

    }
}
