package nl.sogyo.mancala.domain;

import nl.sogyo.mancala.domain.exceptions.GameOver;
import nl.sogyo.mancala.domain.exceptions.OngeldigBordException;
import nl.sogyo.mancala.domain.exceptions.CanNotPlayThisPocket;


public abstract class PocketTemplate {
    private final int pocketNr;
    private int stones;
    private PocketTemplate nextPocket;
    private final Player turn;

    PocketTemplate() {
        this.pocketNr = 1;
        this.turn = new Player();
        this.stones = 4;
    }

    PocketTemplate(int pocketNr, Player turn, int stones) {
        this.pocketNr = pocketNr;
        this.turn = turn;
        this.stones = stones;
    }

    void setNextPocket(PocketTemplate nextPocket) {
        this.nextPocket = nextPocket;
    }

    public PocketTemplate getNextPocket() {
        return this.nextPocket;
    }

    abstract PocketTemplate createNextPocket(int nextNr, PocketTemplate firstPocket, Player turn);

    PocketTemplate getPocketFinder(int i) {
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

    private PocketTemplate getPocketFinder(int i, PocketTemplate startPocket) {
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
        PocketTemplate PlayerOne = getPocketFinder(1);
        PocketTemplate PlayerTwo = getPocketFinder(8);
        int scorePlayerOne = PlayerOne.getWhatIsTheScore();
        int scorePlayerTwo = PlayerTwo.getWhatIsTheScore();
        return (scorePlayerOne > scorePlayerTwo) ? 1 : (scorePlayerTwo > scorePlayerOne) ? 2 : 0;
    }

    private int getWhatIsTheScore(){
        PocketTemplate myMancala = findMyMancala();
        return myMancala.getStonesAmount();
    }

    void setStones(int amount) {
        this.stones = amount;
    }

    PocketTemplate findMyMancala() {
        if (this.nextPocket instanceof MancalaPocket) {
            return this.nextPocket;
        }
        return this.nextPocket.findMyMancala();
    }

    public int getStonesAmount(){
        return this.stones;
    }

    Player getTurn(){
        return this.turn;
    }

    boolean isTurnOfThisPlayer(){
        return this.turn.isTurnOfThisPlayer();
    }

    // just for client for now
    public int getWhoseTurnIsIt(){
        return this.isTurnOfThisPlayer() ? 1 : 2;
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

}