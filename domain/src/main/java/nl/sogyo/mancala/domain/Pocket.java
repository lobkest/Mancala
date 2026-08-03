package nl.sogyo.mancala.domain;

import nl.sogyo.mancala.domain.exceptions.OngeldigBordException;
import nl.sogyo.mancala.domain.exceptions.CanNotPlayThisPocket;

public class Pocket extends PocketAbstract {

    public Pocket() {
        super();
        this.nextPocket = createNextPocket(2, this, this.owner, this.turn);
    }

    protected Pocket(int pocketNr, PocketAbstract firstPocket, int owner, Player turn) {
        super(pocketNr, owner, turn);
        this.stones = 4;
        this.nextPocket = createNextPocket(pocketNr + 1, firstPocket, owner, turn);
    }

    @Override
    protected PocketAbstract createNextPocket(int nextNr, PocketAbstract firstPocket, int owner, Player turn){
        return switch (nextNr) {
            case 7, 14 -> new MancalaPocket(nextNr, firstPocket, owner, turn);
            default    -> new Pocket(nextNr, firstPocket, owner, turn);
        };
    }

    @Override
    public void setMoveStones() {
        determineIfGameIsOver();

        boolean canPlay = this.turn.isTurnOfThisPlayer() && this.stones > 0;

        if (!canPlay) {
            throw new CanNotPlayThisPocket();
        }

        int stonesToPass = this.stones;
        setStones(0);
        this.nextPocket.receiveStones(stonesToPass);
    }

    @Override
    protected void passRemainingStones(int remainingStones) {
        if (remainingStones > 0) {
            this.nextPocket.receiveStones(remainingStones);
        } else {
            lastStoneInPocket();
            this.turn.setChangeTurn();
        }
    }

    private void lastStoneInPocket() {
        boolean isMyTurn = this.turn.isTurnOfThisPlayer();
        if (this.stones == 1 && isMyTurn) {
            this.setStones(0);

            PocketAbstract neighborPocket = findNeighborPocket(this.pocketNr);
            PocketAbstract mancalaOwn = findMyMancala();

            int neighborPocketStonesAmount = neighborPocket.getStonesAmount();

            mancalaOwn.setAddStones(neighborPocketStonesAmount);
            mancalaOwn.setAddStones(1);

            neighborPocket.setStones(0);
        }
    }

    @Override
    protected boolean isCurrentTurnSideEmpty() {
        if (this.turn.isTurnOfThisPlayer()) {
            boolean thisPocketIsEmpty = (this.stones == 0);
            return thisPocketIsEmpty && this.nextPocket.isCurrentTurnSideEmpty();
        }

        return this.nextPocket.isCurrentTurnSideEmpty();
    }

    @Override
    protected void clearAllSideStonesToMancalas() {
        if (this.stones > 0) {
            PocketAbstract myMancala = findMyMancala();
            myMancala.setAddStones(this.stones);
            this.stones = 0;
        }
        this.nextPocket.clearAllSideStonesToMancalas();
    }

    protected PocketAbstract findNeighborPocket(int pocketNr) {
        return this.getPocketFinder(14 - pocketNr);
    }

    @Override
    protected int getSideStonesCount() {
        return this.stones + this.nextPocket.getSideStonesCount();
    }

    @Override
    protected int clearSideStones() {
        int count = this.stones;
        this.stones = 0;
        return count + this.nextPocket.clearSideStones();
    }

}