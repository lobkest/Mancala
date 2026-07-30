
package nl.sogyo.mancala.domain;

public class MancalaPocket extends PocketAbstract  {

    protected MancalaPocket(int pocketNr, PocketAbstract firstPocket, Beurt beurt) {
        super(pocketNr, firstPocket, beurt);
        this.stones = 0;
        this.nextPocket = createNextPocket(pocketNr + 1, firstPocket, beurt);
    }

    @Override
    public boolean canIDoMove() {
        return false;
    }

    @Override
    public void setMoveStones() {
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
                // Regel: Laatste steen in EIGEN Mancala? Extra beurt! (beurt NIET wisselen)
            }
        } else {
            // Niet jouw Mancala? Sla deze over en geef direct door aan het volgende vakje
            this.nextPocket.receiveStones(stonesPassedOn);
        }
    }
}