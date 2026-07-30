package nl.sogyo.mancala.domain.exceptions;

public class CanNotPlayThisPocket extends RuntimeException {
    public CanNotPlayThisPocket() {
        super("Deze pocket is niet van jouw of dit is een mancala, deze kan je niet spelen!");
    }
}