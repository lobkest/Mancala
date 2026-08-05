package nl.sogyo.mancala.domain;

import nl.sogyo.mancala.domain.exceptions.CanNotPlayThisPocket;

import java.util.List;

class Pocket extends PocketTemplate {

    Pocket() {
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

    // constructors voor maken van custom board [niet public; alleen voor testen]
    Pocket(List<Integer> initialStones) {
        super(1, new Player(), initialStones.get(0));
        PocketTemplate next = createNextPocket(2, this, getTurn(), initialStones);
        setNextPocket(next);
    }
    Pocket(int pocketNr, PocketTemplate firstPocket, Player turn, List<Integer> initialStones) {
        super(pocketNr, turn, initialStones.get(pocketNr - 1));

        PocketTemplate next = createNextPocket(pocketNr + 1, firstPocket, turn, initialStones);
        setNextPocket(next);
    }
    private PocketTemplate createNextPocket(int nextNr, PocketTemplate firstPocket, Player turn, List<Integer> initialStones) {
        return switch (nextNr) {
            case 7, 14 -> new MancalaPocket(nextNr, firstPocket, turn, initialStones);
            default    -> new Pocket(nextNr, firstPocket, turn, initialStones);
        };
    }

    //

    void setMoveStones(int pocketNr) {
        Pocket targetPocket = findPocket(pocketNr);
        determineIfGameIsOver();
        targetPocket.doMoveStones();
        determineIfGameIsOver();
    }

    private void doMoveStones() {
        boolean canPlay = this.isTurnOfThisPlayer() && this.getStonesAmount() > 0;

        if (!canPlay) {
            throw new CanNotPlayThisPocket();
        }

        this.getNextPocket().receiveStones(this.getStonesAmount());
        setStones(0);
    }


    Pocket findPocket(int targetPocketNr) {
        PocketTemplate foundPocket = this.getPocketFinder(targetPocketNr);

        if (!(foundPocket instanceof Pocket)) {
            throw new CanNotPlayThisPocket();
        }

        return (Pocket) foundPocket;
    }

    @Override
    void passRemainingStones(int remainingStones) {
        if (remainingStones > 0) {
            this.getNextPocket().receiveStones(remainingStones);
        } else {
            lastStoneInPocket();
            this.setChangeTurn();
            determineIfGameIsOver();
        }
    }

    @Override
    boolean isEmptyForGameEnd() {
        return this.getStonesAmount() == 0;
    }

    private void lastStoneInPocket() {
        boolean isMyTurn = this.isTurnOfThisPlayer();
        if (this.getStonesAmount() == 1 && isMyTurn) {
            this.setStones(0);

            Pocket neighborPocket = this.findNeighborPocket();
            PocketTemplate mancalaOwn = findMyMancala();

            int neighborPocketStonesAmount = neighborPocket.getStonesAmount();

            mancalaOwn.setAddStones(neighborPocketStonesAmount);
            mancalaOwn.setAddStones(1);

            neighborPocket.setStones(0);
        }
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

    Pocket findNeighborPocket() {
        return (Pocket) countStepsToMancala(0);
    }

    @Override
    protected PocketTemplate countStepsToMancala(int steps) {
        PocketTemplate next = this.getNextPocket();
        return next.countStepsToMancala(steps + 1);
    }

    @Override
    PocketTemplate findMyMancala() {
        return getNextPocket().findMyMancala();
    }

}