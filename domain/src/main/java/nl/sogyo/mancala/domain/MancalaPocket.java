
package nl.sogyo.mancala.domain;

import nl.sogyo.mancala.domain.exceptions.OngeldigBordException;
import nl.sogyo.mancala.domain.exceptions.CanNotPlayThisPocket;

public class MancalaPocket extends PocketAbstract  {

    MancalaPocket(int pocketNr, PocketAbstract firstPocket, int owner, Player turn) {
        super(pocketNr, owner, turn, 0);

        PocketAbstract next = createNextPocket(pocketNr + 1, firstPocket, owner, turn);
        setNextPocket(next);
    }

    @Override
    PocketAbstract createNextPocket(int nextNr, PocketAbstract firstPocket, int owner, Player turnOne) {
        if (nextNr == 15) {
            return firstPocket;
        }

        Player nextTurn = turnOne;
        if (owner == 1) {
            Player turnTwo = new Player(turnOne);
            turnOne.giveTurnTwo(turnTwo);
            nextTurn = turnTwo;
        }

        int nextOwner = owner + 1;
        return new Pocket(nextNr, firstPocket, nextOwner, nextTurn);
    }

    @Override
    public void setMoveStones() {
        throw new CanNotPlayThisPocket();
    }

    @Override
    void passRemainingStones(int remainingStones) {
        if (remainingStones > 0) {
            this.getNextPocket().receiveStones(remainingStones);
        }
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
        if (this.getTurn().isTurnOfThisPlayer()) {
            return true;
        }
        return this.getNextPocket().isCurrentTurnSideEmpty();
    }

    @Override
    void clearAllSideStonesToMancalas() {
        if (this.getPocketNr() == 14) {
            return;
        }
        this.getNextPocket().clearAllSideStonesToMancalas();
    }

    @Override
    int getSideStonesCount() {
        return 0;
    }

    @Override
    int clearSideStones() {
        return 0;
    }
}