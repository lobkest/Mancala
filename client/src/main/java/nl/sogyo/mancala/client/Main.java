package nl.sogyo.mancala.client;

public class Main {
    public static void main(String[] args) {
        GameController controller = new GameController();
        MancalaView view = new MancalaView(controller);

        view.initAndShow();
    }
}