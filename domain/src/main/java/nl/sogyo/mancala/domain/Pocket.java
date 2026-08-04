package nl.sogyo.mancala.domain;

import nl.sogyo.mancala.domain.exceptions.OngeldigBordException;
import nl.sogyo.mancala.domain.exceptions.CanNotPlayThisPocket;

import java.util.List;


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

    // constructors voor maken van custom board [niet public; alleen voor testen]
    Pocket(List<Integer> initialStones) {
        super(1, new Player(), initialStones.get(0));
        PocketTemplate next = createNextPocket(2, this, getTurn(), initialStones);
        setNextPocket(next);
    }
    Pocket(int pocketNr, PocketTemplate firstPocket, Player turn, List<Integer> initialStones) {
        super(pocketNr, turn, initialStones.get(pocketNr - 1)); // -1 vanwege 0-indexed List

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

    public void setMoveStones(int pocketNr) {
        Pocket targetPocket = findPocket(pocketNr);
        determineIfGameIsOver();
        targetPocket.doMoveStones();
        determineIfGameIsOver();

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
    boolean isSideEmpty() {
        if (this.getStonesAmount() > 0) {
            return false;
        }
        return this.getNextPocket().isSideEmpty();
    }

    private void lastStoneInPocket() {
        boolean isMyTurn = this.getTurn().isTurnOfThisPlayer();
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

    private void removeAllStones(){
        this.stones = 0;
    }

    Pocket findNeighborPocket() {
        return (Pocket) countStepsToMancala(0);
    }

    private PocketTemplate countStepsToMancala(int steps) {
        PocketTemplate next = this.getNextPocket();

        if (next instanceof MancalaPocket) {
            return next.stepForward(steps + 1);
        }

        return ((Pocket) next).countStepsToMancala(steps + 1);
    }

}