package nl.sogyo.mancala.domain;

import nl.sogyo.mancala.domain.exceptions.OngeldigBordException;

import java.util.List;


abstract class PocketTemplate {
    private final int pocketNr;
    private int stones;
    private PocketTemplate nextPocket;
    private final Player turn;

    PocketTemplate(int pocketNr, Player turn, int stones) {
        this.pocketNr = pocketNr;
        this.turn = turn;
        this.stones = stones;
    }

    void setNextPocket(PocketTemplate nextPocket) {
        this.nextPocket = nextPocket;
    }

    PocketTemplate getNextPocket() {
        return this.nextPocket;
    }

    abstract PocketTemplate createNextPocket(int nextNr, PocketTemplate firstPocket, Player turn, List<Integer> initialStones);

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
        if (this.isCurrentTurnSideEmpty(this)) {
            finishGame();
        }
    }

    boolean isCurrentTurnSideEmpty(PocketTemplate startPocket) {
        if (this.isTurnOfThisPlayer() && !this.isEmptyForGameEnd()) {
            return false;
        }

        PocketTemplate next = this.getNextPocket();
        if (next == startPocket) {
            return true;
        }

        return next.isCurrentTurnSideEmpty(startPocket);
    }

    abstract boolean isEmptyForGameEnd();

    private void finishGame() {
        clearAllSideStonesToMancalas();
        this.turn.setGameOver();
    }

    abstract void clearAllSideStonesToMancalas();

    int getWhoIsTheWinner(){
        if (isGameOver()) {
            PocketTemplate PlayerOne = getPocketFinder(1);
            PocketTemplate PlayerTwo = getPocketFinder(8);
            int scorePlayerOne = PlayerOne.calculateScore();
            int scorePlayerTwo = PlayerTwo.calculateScore();
            return (scorePlayerOne > scorePlayerTwo) ? 1 : (scorePlayerTwo > scorePlayerOne) ? 2 : 0;
            }
        return -1;
    }

    int getWhosePlayersTurn(){
        PocketTemplate PlayerOne = getPocketFinder(1);
        PocketTemplate PlayerTwo = getPocketFinder(8);
        boolean isTurnPlayerOne = PlayerOne.isTurnOfThisPlayer();
        boolean isTurnPlayerTwo = PlayerTwo.isTurnOfThisPlayer();
        return (isTurnPlayerOne) ? 1 : (isTurnPlayerTwo) ? 2 : 0;
    }

    private int calculateScore(){
        PocketTemplate myMancala = findMyMancala();
        return myMancala.getStonesAmount();
    }

    void setStones(int amount) {
        this.stones = amount;
    }

    int getStonesAmount(){
        return this.stones;
    }

    boolean isTurnOfThisPlayer(){
        return this.turn.isTurnOfThisPlayer();
    }

    public boolean isPocketOfCurrentPlayer(int targetPocketNr) {
        PocketTemplate targetPocket = getPocketFinder(targetPocketNr);
        return targetPocket.isTurnOfThisPlayer();
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

    boolean isPlayable() {
        return this.isTurnOfThisPlayer() && this.getStonesAmount() > 0;
    }

    boolean hasPlayableMoves() {
        return hasPlayableMoves(this);
    }

    private boolean hasPlayableMoves(PocketTemplate startPocket) {
        return this.isPlayable() || (this.getNextPocket() != startPocket && this.getNextPocket().hasPlayableMoves(startPocket));
    }

    boolean isGameOver() {
        return !hasPlayableMoves();
    }

    PocketTemplate stepForward(int steps) {
        if (steps == 0) {
            return this;
        }
        return this.nextPocket.stepForward(steps - 1);
    }

    abstract PocketTemplate countStepsToMancala(int steps);

    abstract PocketTemplate findMyMancala();

}