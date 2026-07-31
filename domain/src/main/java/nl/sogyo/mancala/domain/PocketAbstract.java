package nl.sogyo.mancala.domain;

import nl.sogyo.mancala.domain.exceptions.OngeldigBordException;
import nl.sogyo.mancala.domain.exceptions.CanNotPlayThisPocket;

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

    protected PocketAbstract getPocketFinder(int i) {
        return getPocketFinder(i, this);
    }

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

    protected void determineIfGameIsOver() {
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

    public int getWhoIsTheWinner(){
        if (this.beurt.getWhichPlayerIsNow() == 0) {
            int scorePlayerOne = getWhatIsTheScore(1);
            int scorePlayerTwo = getWhatIsTheScore(2);
            return (scorePlayerOne > scorePlayerTwo) ? 1 : (scorePlayerTwo > scorePlayerOne) ? 2 : 0;
        }
        return -1;
    }

    private int getWhatIsTheScore(int playerNr){
        int findpocket = 0;
        if (playerNr == 1){
            findpocket = 7;
        }
        else{
            findpocket = 14;
        }
        return getPocketFinder(findpocket).getStonesAmount();
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

    protected abstract int getSideStonesCount();
    protected abstract int clearSideStones();
    protected abstract PocketAbstract findNeighborPocket(int pocketNr);
}