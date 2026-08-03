package nl.sogyo.mancala.domain;

import nl.sogyo.mancala.domain.exceptions.GameOver;
import nl.sogyo.mancala.domain.exceptions.OngeldigBordException;
import nl.sogyo.mancala.domain.exceptions.CanNotPlayThisPocket;


public abstract class PocketAbstract {
    protected final int pocketNr;
    protected int stones;
    protected PocketAbstract nextPocket;
    protected final int owner;
    protected final Player turn;

    protected PocketAbstract() {
        this.pocketNr = 1;
        this.stones = 4;
        this.turn = new Player();
        this.owner = 1;
    }

    protected PocketAbstract(int pocketNr, int owner, Player turn) {
        this.pocketNr = pocketNr;
        this.owner = owner;
        this.turn = turn;
    }

    protected abstract PocketAbstract createNextPocket(int nextNr, PocketAbstract firstPocket, int owner, Player turn);

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
        if (isCurrentTurnSideEmpty()) {
            gameFinished();
        }
    }

    protected abstract boolean isCurrentTurnSideEmpty();

    private void gameFinished() {
        clearAllSideStonesToMancalas();
        this.turn.setGameOver();
        throw new GameOver();
    }

    protected abstract void clearAllSideStonesToMancalas();

    public int getWhoIsTheWinner(){
        PocketAbstract PlayerOne = getPocketFinder(1);
        PocketAbstract PlayerTwo = getPocketFinder(8);
        int scorePlayerOne = PlayerOne.getWhatIsTheScore();
        int scorePlayerTwo = PlayerTwo.getWhatIsTheScore();
        return (scorePlayerOne > scorePlayerTwo) ? 1 : (scorePlayerTwo > scorePlayerOne) ? 2 : 0;
    }

    private int getWhatIsTheScore(){
        PocketAbstract myMancala = findMyMancala();
        return myMancala.getStonesAmount();
    }

    protected void setStones(int amount) {
        this.stones = amount;
    }

    protected PocketAbstract findMyMancala() {
        if (this.nextPocket instanceof MancalaPocket && this.nextPocket.owner == this.owner) {
            return this.nextPocket;
        }
        return this.nextPocket.findMyMancala();
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
}