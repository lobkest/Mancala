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

//    @Override
//    void setMoveStones() {
//        determineIfGameIsOver();
//
//        boolean canPlay = this.getTurn().isTurnOfThisPlayer() && this.getStonesAmount() > 0;
//
//        if (!canPlay) {
//            throw new CanNotPlayThisPocket();
//        }
//
//        this.getNextPocket().receiveStones(this.getStonesAmount());
//        setStones(0);
//    }

//    public void setMoveStones(int pocketNr) {
//        PocketTemplate pocketFound = getPocketFinder(pocketNr);
//        pocketFound.setMoveStones();
//    }

    public void setMoveStones(int pocketNr) {
        Pocket targetPocket = findPocket(pocketNr);
        determineIfGameIsOver();
        targetPocket.doMoveStones();
    }

    private void doMoveStones() {
        boolean canPlay = this.getTurn().isTurnOfThisPlayer() && this.getStonesAmount() > 0;

        if (!canPlay) {
            throw new CanNotPlayThisPocket();
        }

        this.getNextPocket().receiveStones(this.getStonesAmount());
        setStones(0);
    }


    Pocket findPocket(int targetPocketNr) {
        // Find the requested pocket via the circular chain
        PocketTemplate found = this.getPocketFinder(targetPocketNr);

        // Validate that the target pocket is actually a Pocket and not a MancalaPocket
        if (!(found instanceof Pocket)) {
            throw new CanNotPlayThisPocket();
        }

        return (Pocket) found;
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

//            PocketTemplate neighborPocket = findNeighborPocket(this.getPocketNr());
            Pocket neighborPocket = this.findNeighborPocket();
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

//    PocketTemplate findNeighborPocket(int pocketNr) {
//        return this.getPocketFinder(14 - pocketNr);
//    }

    Pocket findNeighborPocket() {
        return (Pocket) countStepsToMancala(0);
    }

    private PocketTemplate countStepsToMancala(int steps) {
        PocketTemplate next = this.getNextPocket();

        // Als het volgende vakje de Mancala is, stap vanaf de Mancala (steps + 1) keer verder
        if (next instanceof MancalaPocket) {
            return next.stepForward(steps + 1);
        }

        // Anders is het nog een Pocket, tel 1 stap erbij en zoek verder
        return ((Pocket) next).countStepsToMancala(steps + 1);
    }

}