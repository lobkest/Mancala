package nl.sogyo.mancala.domain.exceptions;

public class GameOver extends RuntimeException {
    public GameOver() {
        super("Game Over :) Someone won, I know who, but I don't know how to tell you yet :( ");
    }
}