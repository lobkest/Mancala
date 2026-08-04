
package nl.sogyo.mancala.domain;

import nl.sogyo.mancala.domain.exceptions.OngeldigBordException;
import nl.sogyo.mancala.domain.exceptions.CanNotPlayThisPocket;

import java.util.List;

class MancalaPocket extends PocketTemplate  {

    MancalaPocket(int pocketNr, PocketTemplate firstPocket, Player turn) {
        super(pocketNr, turn, 0);

        PocketTemplate next = createNextPocket(pocketNr + 1, firstPocket, turn);
        setNextPocket(next);
    }

    @Override
    PocketTemplate createNextPocket(int nextNr, PocketTemplate firstPocket, Player turnOne) {
        if (nextNr == 15) {
            return firstPocket;
        }
        Player turnTwo = new Player(turnOne);
        turnOne.giveTurnTwo(turnTwo);
        return new Pocket(nextNr, firstPocket, turnTwo);
    }

    // constructors voor maken van custom board
    MancalaPocket(int pocketNr, PocketTemplate firstPocket, Player turn, List<Integer> initialStones) {
        super(pocketNr, turn, initialStones.get(pocketNr - 1));

        PocketTemplate next = createNextPocket(pocketNr + 1, firstPocket, turn, initialStones);
        setNextPocket(next);
    }
    private PocketTemplate createNextPocket(int nextNr, PocketTemplate firstPocket, Player turnOne, List<Integer> initialStones) {
        if (nextNr == 15) {
            return firstPocket;
        }
        Player turnTwo = new Player(turnOne);
        turnOne.giveTurnTwo(turnTwo);
        return new Pocket(nextNr, firstPocket, turnTwo, initialStones);
    }
    //

    @Override
    void passRemainingStones(int remainingStones) {
        if (remainingStones > 0) {
            this.getNextPocket().receiveStones(remainingStones);
        }else {
            determineIfGameIsOver();
        }
    }

    @Override
    boolean isSideEmpty() {
        return true;
    }

    @Override
    void receiveStones(int stonesPassedOn) {
        if (this.getTurn().isTurnOfThisPlayer()) {
            depositStoneAndPass(stonesPassedOn);
        } else {
            this.getNextPocket().receiveStones(stonesPassedOn);
        }
    }

    @Override
    boolean isCurrentTurnSideEmpty() {
        return true;
    }

    @Override
    void clearAllSideStonesToMancalas() {
        if (this.getNextPocket().getPocketNr() == 1) {
            return;
        }
        this.getNextPocket().clearAllSideStonesToMancalas();
    }

}