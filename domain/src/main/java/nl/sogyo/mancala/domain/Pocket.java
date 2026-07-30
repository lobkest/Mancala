package nl.sogyo.mancala.domain;

import nl.sogyo.mancala.domain.exceptions.OngeldigBordException;
import nl.sogyo.mancala.domain.exceptions.CanNotPlayThisPocket;

public class Pocket extends PocketAbstract {

    public Pocket() {
        super();
    }

    protected Pocket(int pocketNr, PocketAbstract firstPocket, Beurt beurt) {
        super(pocketNr, beurt);
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
            throw new CanNotPlayThisPocket();
        }
        int stonesToPass = this.stones;
        setStones(0);
        this.nextPocket.receiveStones(stonesToPass);
    }

    @Override
    protected void receiveStones(int stonesPassedOn) {
        stonesPassedOn--;
        this.stones++;

        if (stonesPassedOn > 0) {
            this.nextPocket.receiveStones(stonesPassedOn);
        } else if (this.stones == 1){
            // get all stones to own mancala and that of neighbor
            this.setStones(0);

            PocketAbstract neighborPocket = findNeighborPocket(this.pocketNr);
            PocketAbstract mancalaOwn = findMyMancala(this.beurt.getWhichPlayerIsNow());

            int neighborPocketStonesAmount = neighborPocket.getStonesAmount();

            mancalaOwn.setAddStones(neighborPocketStonesAmount);
            mancalaOwn.setAddStones(1);

            neighborPocket.setStones(0);

            this.beurt.setChangeBeurt();
        }
        else{
            this.beurt.setChangeBeurt();
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

    private PocketAbstract findMyMancala(int currentPlayer) {
        if (currentPlayer==1){
            return this.getPocketFinder(7);
        }
        else{
            return this.getPocketFinder(14);
        }
    }

}