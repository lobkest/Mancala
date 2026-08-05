package nl.sogyo.mancala.client;

import nl.sogyo.mancala.domain.Facade;
import nl.sogyo.mancala.domain.exceptions.CanNotPlayThisPocket;

public class GameController {
    private final Facade facade;

    public GameController() {
        this.facade = new Facade();
    }

    public void startNewGame() {
        this.facade.startGame();
    }

    public int getCurrentTurn() {
        return this.facade.getCurrentTurn();
    }

    public String makeMove(int playerPocketChoice) {
        int actualPocket = playerPocketChoice;
        if (getCurrentTurn() == 2) {
            actualPocket += 7;
        }

        try {
            this.facade.setMoveStones(actualPocket);
            return null;
        } catch (CanNotPlayThisPocket e) {
            return "Invalid move! Player " + getCurrentTurn() + ", please choose a different pocket.";
        }
    }

    public boolean[] getPlayablePockets() {
        return this.facade.getPlayablePockets();
    }

    public boolean isGameOver() {
        return this.facade.isGameOver();
    }

    public int getWinner() {
        return this.facade.getWinner();
    }

    public int[] getBoardStones() {
        return this.facade.getBoardStones();
    }
}