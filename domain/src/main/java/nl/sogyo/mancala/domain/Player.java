package nl.sogyo.mancala.domain;

class Player {
    private boolean isTurnOfPlayer;
    private Player otherTurn;

    Player() {
        this.isTurnOfPlayer = true;
    }

    Player(Player turn) {
        this.isTurnOfPlayer = !turn.isTurnOfThisPlayer();
        this.otherTurn = turn;
    }

    boolean isTurnOfThisPlayer() {
        return this.isTurnOfPlayer;
    }

    void setChangeTurn() {
        boolean currentState = this.isTurnOfPlayer;
        this.isTurnOfPlayer = !currentState;
        this.otherTurn.isTurnOfPlayer = currentState;
    }

    void giveTurnTwo(Player turnTwo) {
        this.otherTurn = turnTwo;
    }

    void setGameOver() {
        this.isTurnOfPlayer = false;
        if (this.otherTurn.isTurnOfPlayer) {
            this.otherTurn.setGameOver();
        }
    }
}