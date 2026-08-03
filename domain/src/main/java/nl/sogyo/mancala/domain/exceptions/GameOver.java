package nl.sogyo.mancala.domain.exceptions;

public class GameOver extends RuntimeException {
    public GameOver() {
        super("Game over! No more moves can be made.");
    }
}