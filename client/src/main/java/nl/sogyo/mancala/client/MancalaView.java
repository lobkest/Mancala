package nl.sogyo.mancala.client;

import nl.sogyo.mancala.domain.exceptions.GameOver;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MancalaView {
    private final GameController controller;

    private Frame frame;
    private Label statusLabel;
    private Panel buttonPanel;
    private Label[] pocketLabels;

    public MancalaView(GameController controller) {
        this.controller = controller;
    }

    public void initAndShow() {
        frame = new Frame("Mancala Game");
        frame.setLayout(new BorderLayout());

        statusLabel = new Label("Welkom bij Mancala!", Label.CENTER);
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
        boardPanel.setBackground(Color.darkGray);

        pocketLabels = new Label[14];
        for (int i = 0; i < 14; i++) {
            pocketLabels[i] = new Label("", Label.CENTER);
            pocketLabels[i].setBackground(Color.white);
        }

        pocketLabels[13].setPreferredSize(new Dimension(150, 0));
        boardPanel.add(pocketLabels[13], BorderLayout.WEST);
        pocketLabels[6].setPreferredSize(new Dimension(150, 0));
        boardPanel.add(pocketLabels[6], BorderLayout.EAST);

        Panel centerPockets = new Panel(new GridLayout(2, 6, 5, 5));
        for (int i = 12; i >= 7; i--) {
            centerPockets.add(pocketLabels[i]);
        }
        for (int i = 0; i < 6; i++) {
            centerPockets.add(pocketLabels[i]);
        }

        boardPanel.add(centerPockets, BorderLayout.CENTER);
        frame.add(boardPanel, BorderLayout.CENTER);

        // Knoppen onderaan
        buttonPanel = new Panel(new GridLayout(1, 6, 10, 0));
        buttonPanel.setBackground(Color.darkGray);

        for (int i = 1; i <= 6; i++) {
            int pocketNr = i;
            Button button = new Button("Pocket " + pocketNr);
            button.addActionListener(e -> handleButtonClick(pocketNr));
            buttonPanel.add(button);
        }
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.revalidate();
        frame.repaint();
    }

    private void handleButtonClick(int pocketNr) {
        try {
            String errorMessage = controller.makeMove(pocketNr);
            if (errorMessage != null) {
                statusLabel.setText(errorMessage);
                return;
            }
            updateDisplay();

        } catch (GameOver e) {
            updateDisplay();
            int winner = controller.getWinner();
            statusLabel.setText(e.getMessage() + " The winner is: " + winner);
            buttonPanel.setEnabled(false);
        }
    }

    private void updateDisplay() {
        int[] stones = controller.getBoardStones();

//        for (int i = 0; i < 14; i++) {
//            String text = (i == 6 || i == 13)
//                    ? "<html><center><b>Kalaha " + (i + 1) + "</b><br/><br/>" + stones[i] + " stones</center></html>"
//                    : "<html><center>Pocket " + (i + 1) + "<br/>" + stones[i] + " stones</center></html>";
//            pocketLabels[i].setText(text);
//        }
        for (int i = 0; i < 14; i++) {
            String name = (i == 6 || i == 13) ? "Kalaha " : "Pocket ";
            pocketLabels[i].setText(name + (i + 1) + ": " + stones[i] + " stones");
        }

        statusLabel.setText("Player " + controller.getCurrentTurn() + " is now, which pocket would you like to move?");
    }
}