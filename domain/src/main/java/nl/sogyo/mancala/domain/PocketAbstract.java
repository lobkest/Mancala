package nl.sogyo.mancala.domain;

import nl.sogyo.mancala.domain.exceptions.OngeldigBordException;

public abstract class PocketAbstract {
    protected final int pocketNr;
    protected int stones;
    protected PocketAbstract nextPocket;
    protected final Beurt beurt;

    public PocketAbstract() {
        this.pocketNr = 1;
        this.stones = 4;
        this.beurt = new Beurt();
        this.nextPocket = createNextPocket(2, this, this.beurt);
    }

    protected PocketAbstract(int pocketNr, Beurt beurt) {
        this.pocketNr = pocketNr;
        this.beurt = beurt;
    }

    protected PocketAbstract createNextPocket(int nextNr, PocketAbstract firstPocket, Beurt beurt) {
        if (nextNr == 7 || nextNr == 14) {
            return new MancalaPocket(nextNr, firstPocket, beurt);
        } else if (nextNr < 14) {
            return new Pocket(nextNr, firstPocket, beurt);
        } else if (nextNr == 15){
            return firstPocket;
        }
        else{
            throw new OngeldigBordException();
        }
    }

    public PocketAbstract getPocketFinder(int i) {
        return getPocketFinder(i, this);
    }

    private PocketAbstract getPocketFinder(int i, PocketAbstract startPocket) {
        if (this.pocketNr == i) {
            return this;
        }
        // If we've looped all the way back to where we started searching, the pocket doesn't exist
        if (this.nextPocket == startPocket) {
            throw new OngeldigBordException();
        }
        return this.nextPocket.getPocketFinder(i, startPocket);
    }

    protected void setStones(int amount) {
        this.stones = amount;
    }

    protected int getStonesAmount(){
        return this.stones;
    }

    protected void setAddStones(int amount){
        this.stones += amount;
    }

    public abstract boolean canIDoMove();
    public abstract void setMoveStones();
    protected abstract void receiveStones(int stonesPassedOn);


}