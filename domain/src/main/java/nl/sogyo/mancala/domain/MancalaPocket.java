
package nl.sogyo.mancala.domain;

import nl.sogyo.mancala.domain.exceptions.OngeldigBordException;
import nl.sogyo.mancala.domain.exceptions.CanNotPlayThisPocket;

public class MancalaPocket extends PocketAbstract  {

    protected MancalaPocket(int pocketNr, PocketAbstract firstPocket, Beurt beurt, int owner) {
        super(pocketNr, beurt, owner);
        this.stones = 0;
        this.nextPocket = createNextPocket(pocketNr + 1, firstPocket, beurt, owner);
    }

    @Override
    protected PocketAbstract createNextPocket(int nextNr, PocketAbstract firstPocket, Beurt beurt, int owner){
        owner = owner+1; // of =2
        return switch (nextNr) {
            case 15 -> firstPocket;
            default -> new Pocket(nextNr, firstPocket, beurt, owner);
        };
    }

    @Override
    public void setMoveStones() {
        throw new CanNotPlayThisPocket();
    }

    @Override
    protected void passRemainingStones(int remainingStones) {
        if (remainingStones > 0) {
            this.nextPocket.receiveStones(remainingStones);
        }
    }

    @Override
    protected void receiveStones(int stonesPassedOn) {
        if (this.beurt.isTurnOf(this.owner)) {
            depositStoneAndPass(stonesPassedOn);
        } else {
            this.nextPocket.receiveStones(stonesPassedOn); // Sla tegenstanders Mancala over
        }
    }

    // In MancalaPocket.java:
    @Override
    protected PocketAbstract findNeighborPocket(int pocketNr) {
        throw new OngeldigBordException();
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