
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

//    @Override
//    protected void receiveStones(int stonesPassedOn) {
//        boolean isMyTurn = this.beurt.isTurnOf(this.owner);
//        if (isMyTurn) {
//            depositStoneAndPass(stonesPassedOn);
//        } else {
//            this.nextPocket.receiveStones(stonesPassedOn);
//        }
//    }

//    @Override
//    protected void passRemainingStones(int remainingStones) {
//        if (remainingStones == 0) {
//            return; // this player can go again
//        }
//        this.nextPocket.receiveStones(remainingStones);
//    }

//    @Override
//    protected boolean canReceiveStones() {
//        return this.beurt.isTurnOf(this.owner); // Alleen als het mijn turn is
//    }

    @Override
    protected void passRemainingStones(int remainingStones) {
        if (remainingStones > 0) {
            this.nextPocket.receiveStones(remainingStones);
        }
//        else {
//            // 0 stenen over in eigen Mancala = nog een keer aan de beurt (beurt niet wisselen!)
//        }
    }

    @Override
    protected void receiveStones(int stonesPassedOn) {
        if (this.beurt.isTurnOf(this.owner)) {
            depositStoneAndPass(stonesPassedOn);
        } else {
            this.nextPocket.receiveStones(stonesPassedOn); // Sla tegenstanders Mancala over
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