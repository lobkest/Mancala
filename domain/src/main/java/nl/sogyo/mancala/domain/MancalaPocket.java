package nl.sogyo.mancala.domain;

import java.util.List;

class MancalaPocket extends PocketTemplate  {

    MancalaPocket(int pocketNr, PocketTemplate firstPocket, Player turn, List<Integer> initialStones) {
        super(pocketNr, turn, initialStones.get(pocketNr - 1));

        PocketTemplate next = createNextPocket(pocketNr + 1, firstPocket, turn, initialStones);
        setNextPocket(next);
    }

    @Override
    PocketTemplate createNextPocket(int nextNr, PocketTemplate firstPocket, Player turnOne, List<Integer> initialStones) {
        if (nextNr == 15) {
            return firstPocket;
        }
        Player turnTwo = new Player(turnOne);
        turnOne.giveTurnTwo(turnTwo);
        return new Pocket(nextNr, firstPocket, turnTwo, initialStones);
    }

    @Override
    void passRemainingStones(int remainingStones) {
        if (remainingStones > 0) {
            this.getNextPocket().receiveStones(remainingStones);
        }else {
            determineIfGameIsOver();
        }
    }

    @Override
    boolean isPlayable() {
        return false;
    }

    @Override
    void receiveStones(int stonesPassedOn) {
        if (this.isTurnOfThisPlayer()) {
            depositStoneAndPass(stonesPassedOn);
        } else {
            this.getNextPocket().receiveStones(stonesPassedOn);
        }
    }

    @Override
    boolean isEmptyForGameEnd() {
        return true;
    }

    @Override
    void clearAllSideStonesToMancalas() {
        if (this.getNextPocket().getPocketNr() == 1) {
            return;
        }
        this.getNextPocket().clearAllSideStonesToMancalas();
    }

    @Override
    protected PocketTemplate countStepsToMancala(int steps) {
        return this.stepForward(steps);
    }

    @Override
    PocketTemplate findMyMancala() {
        return this;
    }


}