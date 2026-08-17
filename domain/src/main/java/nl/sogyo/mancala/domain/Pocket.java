package nl.sogyo.mancala.domain;

import nl.sogyo.mancala.domain.exceptions.CanNotPlayThisPocket;

import java.util.List;

class Pocket extends PocketTemplate {

    Pocket() {
        this(List.of(
                4, 4, 4, 4, 4, 4, 0,
                4, 4, 4, 4, 4, 4, 0
        ));
    }

    Pocket(List<Integer> initialStones) {
        this(new Player(), initialStones);
    }

    private Pocket(Player firstPlayer, List<Integer> initialStones) {
        super(1, firstPlayer, initialStones.get(0));
        PocketTemplate next = createNextPocket(2, this, firstPlayer, initialStones);
        setNextPocket(next);
    }

    Pocket(int pocketNr, PocketTemplate firstPocket, Player turn, List<Integer> initialStones) {
        super(pocketNr, turn, initialStones.get(pocketNr - 1));

        PocketTemplate next = createNextPocket(pocketNr + 1, firstPocket, turn, initialStones);
        setNextPocket(next);
    }

    @Override
    PocketTemplate createNextPocket(int nextNr, PocketTemplate firstPocket, Player turn, List<Integer> initialStones) {
        return switch (nextNr) {
            case 7, 14 -> new MancalaPocket(nextNr, firstPocket, turn, initialStones);
            default    -> new Pocket(nextNr, firstPocket, turn, initialStones);
        };
    }

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


    private Pocket findPocket(int targetPocketNr) {
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

            Pocket oppositePocket = this.findOppositePocket();
            PocketTemplate mancalaOwn = findMyMancala();

            int oppositePocketStonesAmount = oppositePocket.getStonesAmount();

            mancalaOwn.setAddStones(oppositePocketStonesAmount);
            mancalaOwn.setAddStones(1);

            oppositePocket.setStones(0);
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

    private Pocket findOppositePocket() {
        return (Pocket) countStepsToMancala(0);
    }

    @Override
    PocketTemplate countStepsToMancala(int steps) {
        PocketTemplate next = this.getNextPocket();
        return next.countStepsToMancala(steps + 1);
    }

    @Override
    PocketTemplate findMyMancala() {
        return getNextPocket().findMyMancala();
    }

}