package nl.sogyo.mancala.domain;

import nl.sogyo.mancala.domain.exceptions.CanNotPlayThisPocket;

import java.util.HashMap;
import java.util.Map;

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

    private boolean[] collectTurn(PocketTemplate current, int index, boolean[] isTurn) {
        // Zorg dat de methode isTurnOfThisPlayer() bestaat in je PocketTemplate/Pocket!
        isTurn[index] = current.isTurnOfThisPlayer();

        // Roep recursief collectTurn aan, in plaats van collectStones
        return (index == 13) ? isTurn : collectTurn(current.getNextPocket(), index + 1, isTurn);
    }

    public int getWinner() {
//        if (this.pocket == null) return 0;
        return this.pocket.getWhoIsTheWinner();
    }

    public int getWhosePlayersTurn(){
        return this.pocket.getWhosePlayersTurn();
    }

    public boolean isPocketOfCurrentPlayer(int pocketNr) {
        if (this.pocket == null) return false;
        return this.pocket.isPocketOfCurrentPlayer(pocketNr);
    }

    public boolean[] isTurnArray() {
        boolean[] isTurn = new boolean[14];
        return (this.pocket == null) ? isTurn : collectTurn(this.pocket, 0, isTurn);
    }

    public Map<String, Object> getGameStatus() {
        Map<String, Object> status = new HashMap<>();

        status.put("stonesArray", this.getBoardStones());
        status.put("currentPlayer", this.getWhosePlayersTurn());
        status.put("winner", this.getWinner());

        return status;
    }

    public Map<String, Object> getPocketsStatus() {
        Map<String, Object> status = new HashMap<>();

        status.put("stonesArray", this.getBoardStones());
        status.put("isTurnPlayer", this.isTurnArray());
        status.put("winner", this.getWinner());

        return status;
    }

}