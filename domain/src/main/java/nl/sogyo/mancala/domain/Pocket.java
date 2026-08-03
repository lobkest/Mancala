package nl.sogyo.mancala.domain;

import nl.sogyo.mancala.domain.exceptions.OngeldigBordException;
import nl.sogyo.mancala.domain.exceptions.CanNotPlayThisPocket;


public class Pocket extends PocketTemplate {

    public Pocket() {
        super();
        PocketTemplate next = createNextPocket(2, this, getTurn());
        setNextPocket(next);
    }

    Pocket(int pocketNr, PocketTemplate firstPocket, Player turn) {
        super(pocketNr, turn, 4);

        PocketTemplate next = createNextPocket(pocketNr + 1, firstPocket, turn);
        setNextPocket(next);
    }

    @Override
    PocketTemplate createNextPocket(int nextNr, PocketTemplate firstPocket, Player turn) {
        return switch (nextNr) {
            case 7, 14 -> new MancalaPocket(nextNr, firstPocket, turn);
            default    -> new Pocket(nextNr, firstPocket, turn);
        };
    }

    @Override
    public void setMoveStones() {
        determineIfGameIsOver();

        boolean canPlay = this.getTurn().isTurnOfThisPlayer() && this.getStonesAmount() > 0;

        if (!canPlay) {
            throw new CanNotPlayThisPocket();
        }

        this.getNextPocket().receiveStones(this.getStonesAmount());
        setStones(0);
    }

    public void setMoveStones(int pocketNr) {
        PocketTemplate pocketFound = getPocketFinder(pocketNr);
        pocketFound.setMoveStones();
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

            PocketTemplate neighborPocket = findNeighborPocket(this.getPocketNr());
            PocketTemplate mancalaOwn = findMyMancala();

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
            PocketTemplate myMancala = findMyMancala();
            myMancala.setAddStones(this.getStonesAmount());
            this.setStones(0);
        }
        this.getNextPocket().clearAllSideStonesToMancalas();
    }

    PocketTemplate findNeighborPocket(int pocketNr) {
        return this.getPocketFinder(14 - pocketNr);
    }

}