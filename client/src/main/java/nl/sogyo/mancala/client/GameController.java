package nl.sogyo.mancala.client;

import nl.sogyo.mancala.domain.Facade;
import nl.sogyo.mancala.domain.exceptions.CanNotPlayThisPocket;

public class GameController {
    private final Facade facade;

    // Definieer de representatieve vakjes van beide spelers in de controller
    private static final int PLAYER_1_CHECK_POCKET = 1;
    private static final int PLAYER_2_CHECK_POCKET = 8;


    public GameController() {
        this.facade = new Facade();
    }

    public int getCurrentTurn() {
        if (this.facade.isPocketOfCurrentPlayer(PLAYER_1_CHECK_POCKET)) {
            return 1;
        }
        if (this.facade.isPocketOfCurrentPlayer(PLAYER_2_CHECK_POCKET)) {
            return 2;
        }
        return 0; // Game over of geen spel gestart
    }


    public void startNewGame() {
        this.facade.startGame();
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