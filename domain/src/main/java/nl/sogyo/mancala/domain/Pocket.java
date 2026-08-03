package nl.sogyo.mancala.domain;

import nl.sogyo.mancala.domain.exceptions.OngeldigBordException;
import nl.sogyo.mancala.domain.exceptions.CanNotPlayThisPocket;

public class Pocket extends PocketAbstract {

    public Pocket() {
        super();

        PocketAbstract next = createNextPocket(2, this, 1, getTurn());
        setNextPocket(next);
    }


    Pocket(int pocketNr, PocketAbstract firstPocket, int owner, Player turn) {
        super(pocketNr, owner, turn, 4);

        PocketAbstract next = createNextPocket(pocketNr + 1, firstPocket, owner, turn);
        setNextPocket(next);
    }

    @Override
    PocketAbstract createNextPocket(int nextNr, PocketAbstract firstPocket, int owner, Player turn) {
        return switch (nextNr) {
            case 7, 14 -> new MancalaPocket(nextNr, firstPocket, owner, turn);
            default    -> new Pocket(nextNr, firstPocket, owner, turn);
        };
    }

    @Override
    public void setMoveStones() {
        determineIfGameIsOver();

        boolean canPlay = this.getTurn().isTurnOfThisPlayer() && this.getStonesAmount() > 0;

        if (!canPlay) {
            throw new CanNotPlayThisPocket();
        }

        int stonesToPass = this.getStonesAmount();
        setStones(0);
        this.getNextPocket().receiveStones(stonesToPass);
    }

    @Override
    void passRemainingStones(int remainingStones) {
        if (remainingStones > 0) {
            this.getNextPocket().receiveStones(remainingStones);
        } else {
            lastStoneInPocket();
            this.setChangeTurn();
        }
    }

    private void lastStoneInPocket() {
        boolean isMyTurn = this.getTurn().isTurnOfThisPlayer();
        if (this.getStonesAmount() == 1 && isMyTurn) {
            this.setStones(0);

            PocketAbstract neighborPocket = findNeighborPocket(this.getPocketNr());
            PocketAbstract mancalaOwn = findMyMancala();

            int neighborPocketStonesAmount = neighborPocket.getStonesAmount();

            mancalaOwn.setAddStones(neighborPocketStonesAmount);
            mancalaOwn.setAddStones(1);

            neighborPocket.setStones(0);
        }
    }

    @Override
    boolean isCurrentTurnSideEmpty() {
        if (this.getTurn().isTurnOfThisPlayer()) {
            boolean thisPocketIsEmpty = (this.getStonesAmount() == 0);
            return thisPocketIsEmpty && this.getNextPocket().isCurrentTurnSideEmpty();
        }

        return this.getNextPocket().isCurrentTurnSideEmpty();
    }

    @Override
    void clearAllSideStonesToMancalas() {
        if (this.getStonesAmount() > 0) {
            PocketAbstract myMancala = findMyMancala();
            myMancala.setAddStones(this.getStonesAmount());
            this.setStones(0);
        }
        this.getNextPocket().clearAllSideStonesToMancalas();
    }

    PocketAbstract findNeighborPocket(int pocketNr) {
        return this.getPocketFinder(14 - pocketNr);
    }

    @Override
    int getSideStonesCount() {
        return this.getStonesAmount() + this.getNextPocket().getSideStonesCount();
    }

    @Override
    int clearSideStones() {
        int count = this.getStonesAmount();
        this.setStones(0);
        return count + this.getNextPocket().clearSideStones();
    }

}