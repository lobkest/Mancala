
package nl.sogyo.mancala.domain;

import nl.sogyo.mancala.domain.exceptions.CanNotPlayThisPocket;

public class MancalaPocket extends PocketAbstract  {

    protected MancalaPocket(int pocketNr, PocketAbstract firstPocket, Beurt beurt) {
        super(pocketNr, beurt);
        this.stones = 0;
        this.nextPocket = createNextPocket(pocketNr + 1, firstPocket, beurt);
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
        int whoseTurnNow = this.beurt.getWhichPlayerIsNow();

        boolean isMyMancala = (this.pocketNr == 7 && whoseTurnNow == 1) ||
                (this.pocketNr == 14 && whoseTurnNow == 2);

        if (isMyMancala) {
            stonesPassedOn--;
            this.stones++;

            if (stonesPassedOn > 0) {
                this.nextPocket.receiveStones(stonesPassedOn);
            } else {
                // jij bent nog een keer
            }
        } else {
            // sla andermans mancala over
            this.nextPocket.receiveStones(stonesPassedOn);
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