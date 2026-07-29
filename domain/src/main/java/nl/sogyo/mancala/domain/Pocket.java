package nl.sogyo.mancala.domain;

import nl.sogyo.mancala.domain.exceptions.OngeldigBordException;

public class Pocket {
    final int pocketNr;
    int stones;
    Pocket nextPocket;

    public Pocket() {
        this.pocketNr = 1;
        this.stones = 4;
        this.nextPocket = new Pocket(this.pocketNr + 1);
    }

    public Pocket(int pocketNr) {
        this.pocketNr = pocketNr;
        this.stones = 4;
        if (pocketNr < 14) {
            this.nextPocket = new Pocket(this.pocketNr + 1);
        }
    }

    public Pocket getPocketFinder(int i) {
        if (this.pocketNr == i) {
            return this;
        }

        if (this.nextPocket == null) {
            throw new OngeldigBordException();
        }

        return this.nextPocket.getPocketFinder(i);
    }
}