
package nl.sogyo.mancala.domain;

import nl.sogyo.mancala.domain.exceptions.CanNotPlayThisPocket;

public class MancalaPocket extends PocketAbstract  {

    protected MancalaPocket(int pocketNr, PocketAbstract firstPocket, Beurt beurt, int owner) {
        super(pocketNr, beurt, owner);
        this.stones = 0;
        this.nextPocket = createNextPocket(pocketNr + 1, firstPocket, beurt, owner);
    }

    @Override
    protected PocketAbstract createNextPocket(int nextNr, PocketAbstract firstPocket, Beurt beurt, int owner){
        owner = owner+1;
        return switch (nextNr) {
            case 15 -> firstPocket;
            default -> new Pocket(nextNr, firstPocket, beurt, owner);
        };
    }

    @Override
    public boolean canIDoMove() {
        return false;
    }

    @Override
    public void setMoveStones() {
        throw new CanNotPlayThisPocket();
    }

    @Override
    protected void receiveStones(int stonesPassedOn) {
        boolean isMyTurn = this.beurt.isTurnOf(this.owner);
        if (isMyTurn) {
            depositStoneAndPass(stonesPassedOn); // wat is verschil tussen this.methodeInAbstract en zonder this.?
        } else {
            this.nextPocket.receiveStones(stonesPassedOn);
        }
    }

    @Override
    protected void passRemainingStones(int remainingStones) {
        if (remainingStones == 0) {
            return; // this player can go again
        }
        else{
            this.nextPocket.receiveStones(remainingStones);
        }

    }

    @Override
    protected int getSideStonesCount() {
        return 0; // Mancala pockets do not count as standard side pockets
    }

    @Override
    protected int clearSideStones() {
        return 0; // Mancala pocket stores points; it is not cleared
    }
}