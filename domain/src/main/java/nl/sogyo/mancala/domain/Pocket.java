package nl.sogyo.mancala.domain;

import nl.sogyo.mancala.domain.exceptions.OngeldigBordException;

public class Pocket extends PocketAbstract {

    public Pocket() {
        super();
    }

    protected Pocket(int pocketNr, PocketAbstract firstPocket, Beurt beurt) {
        super(pocketNr, firstPocket, beurt);
        this.stones = 4;
        this.nextPocket = createNextPocket(pocketNr + 1, firstPocket, beurt);
    }

    @Override
    public boolean canIDoMove() {
        int whoseTurnNow = this.beurt.getWhichPlayerIsNow();
        return (this.pocketNr < 7 && whoseTurnNow == 1) ||
                (this.pocketNr > 7 && this.pocketNr < 14 && whoseTurnNow == 2);
    }

    @Override
    public void setMoveStones() {
        if (!canIDoMove() || this.stones == 0) {
            return; // Mag niet spelen of vakje is leeg
        }
        int stonesToPass = this.stones;
        this.stones = 0;
        this.nextPocket.receiveStones(stonesToPass);
    }

    @Override
    protected void receiveStones(int stonesPassedOn) {
        stonesPassedOn--;
        this.stones++;

        if (stonesPassedOn > 0) {
            this.nextPocket.receiveStones(stonesPassedOn);
        } else {
            this.beurt.setChangeBeurt();
        }
    }
}