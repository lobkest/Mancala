package nl.sogyo.mancala.domain;

import nl.sogyo.mancala.domain.exceptions.GameOver;
import nl.sogyo.mancala.domain.exceptions.OngeldigBordException;
import nl.sogyo.mancala.domain.exceptions.CanNotPlayThisPocket;


public abstract class PocketAbstract {
    private final int pocketNr;
    private int stones;
    private PocketAbstract nextPocket;
    private final int owner;
    private final Player turn;

    PocketAbstract() {
        this.pocketNr = 1;
        this.owner = 1;
        this.turn = new Player();
        this.stones = 4;
    }

    PocketAbstract(int pocketNr, int owner, Player turn, int stones) {
        this.pocketNr = pocketNr;
        this.owner = owner;
        this.turn = turn;
        this.stones = stones;
    }

    void setNextPocket(PocketAbstract nextPocket) {
        this.nextPocket = nextPocket;
    }

    PocketAbstract getNextPocket() {
        return this.nextPocket;
    }

    abstract PocketAbstract createNextPocket(int nextNr, PocketAbstract firstPocket, int owner, Player turn);

    PocketAbstract getPocketFinder(int i) {
        return getPocketFinder(i, this);
    }

    void receiveStones(int stonesPassedOn) {
        depositStoneAndPass(stonesPassedOn);
    }

    abstract void passRemainingStones(int remainingStones);

    void depositStoneAndPass(int stonesPassedOn) {
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

    void determineIfGameIsOver() {
        if (isCurrentTurnSideEmpty()) {
            gameFinished();
        }
    }

    abstract boolean isCurrentTurnSideEmpty();

    private void gameFinished() {
        clearAllSideStonesToMancalas();
        this.turn.setGameOver();
        throw new GameOver();
    }

    abstract void clearAllSideStonesToMancalas();

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

    void setStones(int amount) {
        this.stones = amount;
    }

    PocketAbstract findMyMancala() {
        if (this.nextPocket instanceof MancalaPocket && this.nextPocket.owner == this.owner) {
            return this.nextPocket;
        }
        return this.nextPocket.findMyMancala();
    }

    int getStonesAmount(){
        return this.stones;
    }

    Player getTurn(){
        return this.turn;
    }

    boolean isTurnOfThisPlayer(){
        return this.turn.isTurnOfThisPlayer();
    }

    int getPocketNr(){
        return this.pocketNr;
    }

    void setChangeTurn(){
        this.turn.setChangeTurn();
    }

    void setAddStones(int amount){
        this.stones += amount;
    }

    public abstract void setMoveStones();

    abstract int getSideStonesCount();
    abstract int clearSideStones();
}