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

    protected PocketAbstract(int pocketNr, PocketAbstract firstPocket, Beurt beurt) {
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
        if (this.pocketNr == i) {
            return this;
        }
        if (this.nextPocket.pocketNr == 1) {
            throw new OngeldigBordException();
        }
        return this.nextPocket.getPocketFinder(i);
    }

    public abstract boolean canIDoMove();
    public abstract void setMoveStones();
    protected abstract void receiveStones(int stonesPassedOn);

//
//    public void SetMoveStones() {
//        int stonesPassedOn = this.stones;
//        this.stones = 0;
//        this.nextPocket.SetMoveStones(stonesPassedOn);
//    }
//
//    private void SetMoveStones(int stonesPassedOn) {
//        stonesPassedOn -= 1;
//        this.stones += 1;
//        if (stonesPassedOn >= 1){
//            this.nextPocket.SetMoveStones(stonesPassedOn);
//        }
//    }
//
//    public boolean CanIDoMove() {
//        int whoseTurnNow = this.beurt.getWhichPlayerIsNow();
//        return (this.pocketNr > 7 && whoseTurnNow == 2) ||
//                (this.pocketNr < 8 && whoseTurnNow == 1);
//    }
}