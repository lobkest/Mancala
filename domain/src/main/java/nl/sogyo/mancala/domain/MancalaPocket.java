
package nl.sogyo.mancala.domain;

import nl.sogyo.mancala.domain.exceptions.OngeldigBordException;
import nl.sogyo.mancala.domain.exceptions.CanNotPlayThisPocket;

public class MancalaPocket extends PocketAbstract  {

    protected MancalaPocket(int pocketNr, PocketAbstract firstPocket, int owner, Player turn) {
        super(pocketNr, owner, turn);
        this.stones = 0;
        this.nextPocket = createNextPocket(pocketNr + 1, firstPocket, owner, turn);
    }

    @Override
    protected PocketAbstract createNextPocket(int nextNr, PocketAbstract firstPocket, int owner, Player turnOne) {
        int nextOwner = owner + 1;
        if (nextNr == 15) {
            return firstPocket;
        }
        Player nextTurn = turnOne;
        if (owner == 1) {
            Player turnTwo = new Player(turnOne);
            turnOne.giveTurnTwo(turnTwo);
            nextTurn = turnTwo;
        }
        return new Pocket(nextNr, firstPocket, nextOwner, nextTurn);
    }

    @Override
    public void setMoveStones() {
        throw new CanNotPlayThisPocket();
    }

    @Override
    protected void passRemainingStones(int remainingStones) {
        if (remainingStones > 0) {
            this.nextPocket.receiveStones(remainingStones);
        }
    }

    @Override
    protected void receiveStones(int stonesPassedOn) {
        if (this.turn.isTurnOfThisPlayer()) {
            depositStoneAndPass(stonesPassedOn);
        } else {
            this.nextPocket.receiveStones(stonesPassedOn);
        }
    }

    @Override
    protected boolean isCurrentTurnSideEmpty() {
        if (this.turn.isTurnOfThisPlayer()) {
            return true;
        }
        return this.nextPocket.isCurrentTurnSideEmpty();
    }

    @Override
    protected void clearAllSideStonesToMancalas() {
        if (this.pocketNr == 14) {
            return;
        }
        this.nextPocket.clearAllSideStonesToMancalas();
    }

    @Override
    protected int getSideStonesCount() {
        return 0;
    }

    @Override
    protected int clearSideStones() {
        return 0;
    }
}