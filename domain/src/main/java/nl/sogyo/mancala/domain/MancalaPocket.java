
package nl.sogyo.mancala.domain;

import nl.sogyo.mancala.domain.exceptions.OngeldigBordException;
import nl.sogyo.mancala.domain.exceptions.CanNotPlayThisPocket;

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

//    @Override
//    public void setMoveStones() {
//        throw new CanNotPlayThisPocket();
//    }

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
        if (this.getNextPocket().getPocketNr() == 1) {
            return;
        }
        this.getNextPocket().clearAllSideStonesToMancalas();
    }

}