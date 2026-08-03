package nl.sogyo.mancala.client;

import nl.sogyo.mancala.domain.Pocket;
import nl.sogyo.mancala.domain.PocketTemplate;
import nl.sogyo.mancala.domain.exceptions.CanNotPlayThisPocket;
import nl.sogyo.mancala.domain.exceptions.GameOver;

public class GameController {
    private Pocket pocket;

    public void startNewGame() {
        this.pocket = new Pocket();
    }

    public int getCurrentTurn() {
        return this.pocket.getWhoseTurnIsIt();
    }

    /**
     * Voert een zet uit.
     * Geeft null terug als de zet gelukt is, of de foutmelding als string als er iets misgaat.
     */
    public String makeMove(int playerPocketChoice) throws GameOver {
        int actualPocket = playerPocketChoice;
        if (getCurrentTurn() == 2) {
            actualPocket += 7;
        }

        try {
            this.pocket.setMoveStones(actualPocket);
            return null; // Geen foutmelding, zet is gelukt!
        } catch (CanNotPlayThisPocket e) {
            return "Invalid move! Player " + getCurrentTurn() + ", please choose a different pocket.";
        }
    }

    public int getWinner() {
        return this.pocket.getWhoIsTheWinner();
    }

    /**
     * Haalt het aantal stenen per pocket op in een simpele int-array van 14 lang.
     */
    public int[] getBoardStones() {
        int[] stones = new int[14];
        if (this.pocket == null) return stones;

        PocketTemplate current = this.pocket;
        for (int i = 0; i < 14; i++) {
            if (current != null) {
                stones[i] = current.getStonesAmount();
                current = current.getNextPocket();
            }
        }
        return stones;
    }
}