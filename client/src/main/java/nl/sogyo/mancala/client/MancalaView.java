package nl.sogyo.mancala.client;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MancalaView {
    private final GameController controller;

    private Frame frame;
    private Label statusLabel;
    private Component[] boardComponents;

    public MancalaView(GameController controller) {
        this.controller = controller;
    }

    public void initializeAndShow() {
        frame = new Frame("Mancala Game");
        frame.setLayout(new BorderLayout());

        statusLabel = new Label("Welcome to Mancala!", Label.CENTER);
        Button buttonStart = new Button("Start Game");
        buttonStart.addActionListener(e -> startGame());

        Panel startPanel = new Panel();
        startPanel.add(buttonStart);

        frame.add(statusLabel, BorderLayout.NORTH);
        frame.add(startPanel, BorderLayout.CENTER);

        frame.setSize(1200, 350);
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    private void startGame() {
        controller.startNewGame();
        buildGameWindow();
        updateDisplay();
    }

    private void buildGameWindow() {
        frame.removeAll();

        // Status bovenaan
        statusLabel = new Label("", Label.CENTER);
        frame.add(statusLabel, BorderLayout.NORTH);

        // Bord in het midden
        Panel boardPanel = new Panel(new BorderLayout(10, 10));
        boardPanel.setBackground(Color.DARK_GRAY);

        boardComponents = new Component[14];

        // Kalaha's blijven vaste labels
        Label kalaha14 = new Label("", Label.CENTER);
        kalaha14.setBackground(Color.WHITE);
        kalaha14.setPreferredSize(new Dimension(150, 0));
        boardComponents[13] = kalaha14;
        boardPanel.add(kalaha14, BorderLayout.WEST);

        Label kalaha7 = new Label("", Label.CENTER);
        kalaha7.setBackground(Color.WHITE);
        kalaha7.setPreferredSize(new Dimension(150, 0));
        boardComponents[6] = kalaha7;
        boardPanel.add(kalaha7, BorderLayout.EAST);

        // Speelbare vakjes worden knoppen op de rasterweergave
        Panel centerPockets = new Panel(new GridLayout(2, 6, 5, 5));

        // Bovenste rij (Speler 2: Pockets 13 t/m 8)
        for (int i = 12; i >= 7; i--) {
            int pocketNr = i + 1;
            Button pocketButton = new Button();
            pocketButton.addActionListener(e -> handlePocketClick(pocketNr));
            boardComponents[i] = pocketButton;
            centerPockets.add(pocketButton);
        }

        // Onderste rij (Speler 1: Pockets 1 t/m 6)
        for (int i = 0; i < 6; i++) {
            int pocketNr = i + 1;
            Button pocketButton = new Button();
            pocketButton.addActionListener(e -> handlePocketClick(pocketNr));
            boardComponents[i] = pocketButton;
            centerPockets.add(pocketButton);
        }

        boardPanel.add(centerPockets, BorderLayout.CENTER);
        frame.add(boardPanel, BorderLayout.CENTER);

        frame.revalidate();
        frame.repaint();
    }

    private void handlePocketClick(int absolutePocketNr) {
        // Converteer de absolute pocketindex (1-14) naar de relatieve keuze per speler (1-6)
        int playerChoice = (absolutePocketNr > 7) ? absolutePocketNr - 7 : absolutePocketNr;

        String errorMessage = controller.makeMove(playerChoice);

        updateDisplay();

        if (errorMessage != null) {
            statusLabel.setText(errorMessage);
            return;
        }

        if (controller.isGameOver()) {
            int winner = controller.getWinner();
            statusLabel.setText("Game Over! The winner is: Player " + winner);
            disableAllButtons();
        }
    }

    private void updateDisplay() {
        int[] stones = controller.getBoardStones();
        boolean[] playable = controller.getPlayablePockets();
        int currentTurn = controller.getCurrentTurn();

        ((Label) boardComponents[6]).setText("Kalaha: " + stones[6] + " stones");
        ((Label) boardComponents[13]).setText("Kalaha: " + stones[13] + " stones");

        // Update speelbare pockets
        updatePocketView(0, stones, playable);

        statusLabel.setText("Player " + currentTurn + "'s turn. Click a green pocket to make a move.");
    }

    private void updatePocketView(int index, int[] stones, boolean[] playable) {
        if (index == 14) {
            return;
        }

        if (index != 6 && index != 13) {
            Button btn = (Button) boardComponents[index];
//            btn.setLabel("Pocket " + (index + 1) + ": " + stones[index] + " stones");
            btn.setLabel(stones[index] + " stones");

            boolean isPlayable = playable[index];
            btn.setEnabled(isPlayable);

            Color lightGreen = new Color(180, 240, 180);
            Color lightRed = new Color(255, 180, 180);
            btn.setBackground(isPlayable ? lightGreen : lightRed);
        }

        updatePocketView(index + 1, stones, playable);
    }

    private void disableAllButtons() {
        disableButtonsRecursively(0);
    }

    private void disableButtonsRecursively(int index) {
        if (index == 14) {
            return;
        }

        if (index != 6 && index != 13) {
            boardComponents[index].setEnabled(false);
        }

        disableButtonsRecursively(index + 1);
    }
}