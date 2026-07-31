package nl.sogyo.mancala.domain;

import nl.sogyo.mancala.domain.exceptions.OngeldigBordException;
import nl.sogyo.mancala.domain.exceptions.CanNotPlayThisPocket;
import nl.sogyo.mancala.domain.exceptions.GameOver;

public class Pocket extends PocketAbstract {

    public Pocket() {
        super();
        this.nextPocket = createNextPocket(2, this, this.beurt, this.owner);
    }

    protected Pocket(int pocketNr, PocketAbstract firstPocket, Beurt beurt, int owner) {
        super(pocketNr, beurt, owner);
        this.stones = 4;
        this.nextPocket = createNextPocket(pocketNr + 1, firstPocket, beurt, owner);
    }

    @Override
    protected PocketAbstract createNextPocket(int nextNr, PocketAbstract firstPocket, Beurt beurt, int owner){
//        if (nextNr != 7 && nextNr != 14){
//            return new Pocket(nextNr, firstPocket, beurt);
//        }else{
//            return new MancalaPocket(nextNr, firstPocket, beurt);
//        }
        return switch (nextNr) {
            case 7, 14 -> new MancalaPocket(nextNr, firstPocket, beurt, owner);
            default    -> new Pocket(nextNr, firstPocket, beurt, owner);
        };
    }

    @Override
    public void setMoveStones() {
        boolean isMyTurn = this.beurt.isTurnOf(this.owner);
//        if (isMyTurn){
//            depositStoneAndPass(this.stones);
//        }
        if (this.stones == 0){
            this.determineIfGameIsOver();
        }
        if (!isMyTurn || this.stones == 0) {
            throw new CanNotPlayThisPocket();
        }

        int stonesToPass = this.stones;
        setStones(0);
        this.nextPocket.receiveStones(stonesToPass);
    }

//    @Override
//    protected void passRemainingStones(int remainingStones) {
//        if (remainingStones == 0) {
//            this.beurt.setChangeBeurt();
//        }
//        this.nextPocket.receiveStones(remainingStones);
//    }

    @Override
    protected void passRemainingStones(int remainingStones) {
        if (remainingStones > 0) {
            this.nextPocket.receiveStones(remainingStones);
        } else {
            lastStoneInPocket(); // check voor slaan
            this.beurt.setChangeBeurt(); // beurt wisselt
        }
    }

//    @Override
//    protected void receiveStones(int stonesPassedOn) {
//        stonesPassedOn--;
//        this.stones++;
//
//        if (stonesPassedOn > 0) {
//            this.nextPocket.receiveStones(stonesPassedOn);
//        } else {
//            lastStoneInPocket();
//            this.beurt.setChangeBeurt();
//        }
//    }

    private void lastStoneInPocket() {
        boolean isMyTurn = this.beurt.isTurnOf(this.owner);
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

    private PocketAbstract findNeighborPocket(int pocketNr) {
        if (pocketNr == 7 || pocketNr == 14) {
            throw new OngeldigBordException();
        }
//        int pocketNrNeighbor = 14 - pocketNr + 14 * ((pocketNr - 1) / 7);
        int pocketNrNeighbor = 14 - pocketNr;
        return this.getPocketFinder(pocketNrNeighbor);
    }

//    private PocketAbstract findMyMancala(int currentPlayer) {
//        if (currentPlayer==1){
//            return this.getPocketFinder(7);
//        }
//        else{
//            return this.getPocketFinder(14);
//        }
//    }

    private PocketAbstract findMyMancala() {
        int targetMancalaNr = (this.owner == 1) ? 7 : 14;
        return this.getPocketFinder(targetMancalaNr);
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