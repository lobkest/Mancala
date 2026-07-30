package nl.sogyo.mancala.domain.exceptions;

public class OngeldigBordException extends RuntimeException {
    public OngeldigBordException() {

        super("Het bord heeft geen 15 vakjes!");
    }
}