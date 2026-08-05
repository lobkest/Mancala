package nl.sogyo.mancala.domain;

import nl.sogyo.mancala.domain.exceptions.CanNotPlayThisPocket;

public class Facade {
    private Pocket pocket;

    public void startGame() {
        this.pocket = new Pocket();
    }

    public void setMoveStones(int pocketNr) throws CanNotPlayThisPocket {
        if (this.pocket == null) return;
        this.pocket.setMoveStones(pocketNr);
    }

    public boolean[] getPlayablePockets() {
        boolean[] playable = new boolean[14];
        return (this.pocket == null) ? playable : collectPlayability(this.pocket, 0, playable);
    }

    private boolean[] collectPlayability(PocketTemplate current, int index, boolean[] playable) {
        playable[index] = current.isPlayable();
        return (index == 13) ? playable : collectPlayability(current.getNextPocket(), index + 1, playable);
    }

    public boolean isGameOver() {
        return (this.pocket != null) && this.pocket.isGameOver();
    }

    public int[] getBoardStones() {
        int[] stones = new int[14];
        return (this.pocket == null) ? stones : collectStones(this.pocket, 0, stones);
    }

    private int[] collectStones(PocketTemplate current, int index, int[] stones) {
        stones[index] = current.getStonesAmount();
        return (index == 13) ? stones : collectStones(current.getNextPocket(), index + 1, stones);
    }

    public int getCurrentTurn() {
        if (this.pocket == null) return 1;
        return this.pocket.getWhoseTurnIsIt();
    }

    public int getWinner() {
        if (this.pocket == null) return 0;
        return this.pocket.getWhoIsTheWinner();
    }
}