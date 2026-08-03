package nl.sogyo.mancala.domain;

class Player {
    private boolean turnOfPlayer;
    private Player otherTurn;

    Player() {
        this.turnOfPlayer = true;
    }

    Player(Player turn) {
        this.turnOfPlayer = !turn.isTurnOfThisPlayer();
        this.otherTurn = turn;
    }

    protected boolean isTurnOfThisPlayer() {
        return this.turnOfPlayer;
    }

    protected void setChangeTurn() {
        boolean currentState = this.turnOfPlayer;
        this.turnOfPlayer = !currentState;
        this.otherTurn.turnOfPlayer = currentState;
    }

    protected void giveTurnTwo(Player turnTwo) {
        this.otherTurn = turnTwo;
    }

    protected void setGameOver() {
        this.turnOfPlayer = false;
        if (this.otherTurn.turnOfPlayer) {
            this.otherTurn.setGameOver();
        }
    }
}