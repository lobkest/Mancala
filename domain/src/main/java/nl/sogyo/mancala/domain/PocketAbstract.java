package nl.sogyo.mancala.domain;

import nl.sogyo.mancala.domain.exceptions.OngeldigBordException;

public abstract class PocketAbstract {
    protected final int pocketNr;
    protected int stones;
    protected PocketAbstract nextPocket;
    protected final Beurt beurt;
    protected final int owner;

    protected PocketAbstract() {
        this.pocketNr = 1;
        this.stones = 4;
        this.beurt = new Beurt();
        this.owner = 1;
    }

    protected PocketAbstract(int pocketNr, Beurt beurt, int owner) {
        this.pocketNr = pocketNr;
        this.beurt = beurt;
        this.owner = owner;
    }

    protected abstract PocketAbstract createNextPocket(int nextNr, PocketAbstract firstPocket, Beurt beurt, int owner);

    public PocketAbstract getPocketFinder(int i) {
        return getPocketFinder(i, this);
    }

//    protected void receiveStones(int stonesPassedOn) {
//        boolean isMyTurn = this.beurt.isTurnOf(this.owner);
//        if (isMyTurn) {
//            depositStoneAndPass(stonesPassedOn);
//        } else {
//            this.nextPocket.receiveStones(stonesPassedOn);
//        }
//    }

    protected void receiveStones(int stonesPassedOn) {
        depositStoneAndPass(stonesPassedOn);
    }

    protected abstract void passRemainingStones(int remainingStones);

    protected void depositStoneAndPass(int stonesPassedOn) {
        this.stones++;
        stonesPassedOn--;

        passRemainingStones(stonesPassedOn);
    }

    private PocketAbstract getPocketFinder(int i, PocketAbstract startPocket) {
        if (this.pocketNr == i) {
            return this;
        }
        if (this.nextPocket == startPocket) {
            throw new OngeldigBordException();
        }
        return this.nextPocket.getPocketFinder(i, startPocket);
    }

    public void determineIfGameIsOver() {
        PocketAbstract playerOneStart = getPocketFinder(1);
        PocketAbstract playerTwoStart = getPocketFinder(8);

        boolean playerOneEmpty = playerOneStart.getSideStonesCount() == 0;
        boolean playerTwoEmpty = playerTwoStart.getSideStonesCount() == 0;

        if (playerOneEmpty || playerTwoEmpty) {
            gameFinished(playerOneStart, playerTwoStart);
        }
    }

    private void gameFinished(PocketAbstract playerOneStart, PocketAbstract playerTwoStart) {
        PocketAbstract mancalaOne = getPocketFinder(7);
        PocketAbstract mancalaTwo = getPocketFinder(14);

        int playerOneRemaining = playerOneStart.clearSideStones();
        int playerTwoRemaining = playerTwoStart.clearSideStones();

        mancalaOne.setAddStones(playerOneRemaining);
        mancalaTwo.setAddStones(playerTwoRemaining);

        mancalaOne.beurt.setGameOver();
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

    public abstract void setMoveStones();
//    protected abstract void receiveStones(int stonesPassedOn);

    protected abstract int getSideStonesCount();
    protected abstract int clearSideStones();
}