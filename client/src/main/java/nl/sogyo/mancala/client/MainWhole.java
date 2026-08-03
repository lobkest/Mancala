package nl.sogyo.mancala.client;

import nl.sogyo.mancala.domain.Pocket;
import nl.sogyo.mancala.domain.PocketTemplate;

import nl.sogyo.mancala.domain.exceptions.GameOver;
import nl.sogyo.mancala.domain.exceptions.OngeldigBordException;
import nl.sogyo.mancala.domain.exceptions.CanNotPlayThisPocket;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainWhole {
    private Pocket pocket;
    private int turn;

    private Frame frame;
    private Label label;
    private Panel buttonPanel;

    private Panel boardPanel;
    private Label[] pocketLabels;

    private void startGame() {
        this.pocket = new Pocket();
        this.turn = this.pocket.getWhoseTurnIsIt();

        makeWindow();
        updateBoardDisplay();
    }

    private void makeWindow() {
        frame.removeAll();

        // bovenaan
        label = new Label("Player " + turn + " is now, which pocket would you like to move?", Label.CENTER);
        frame.add(label, BorderLayout.NORTH);

        // midden
        boardPanel = new Panel();
        boardPanel.setLayout(new BorderLayout(10, 10)); // Hoofd-layout voor het bord
        boardPanel.setBackground(Color.darkGray);

        pocketLabels = new Label[14];
        for (int i = 0; i < 14; i++) {
            pocketLabels[i] = new Label("", Label.CENTER);
            pocketLabels[i].setBackground(Color.white);
        }

        // Mancala's
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

        // onderaan
        buttonPanel = new Panel();
        buttonPanel.setLayout(new GridLayout(1, 6, 10, 0)); // 1 rij, 6 kolommen, 10px tussenruimte
        buttonPanel.setBackground(Color.darkGray);

        for (int i = 1; i <= 6; i++) {
            buttonMaker(i);
        }
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setSize(1200, 350);
        frame.revalidate();
        frame.repaint();
    }

    private void buttonMaker(int pocketNr) {
        Button button = new Button("Pocket " + pocketNr);

        button.addActionListener(e -> {
            moveStones(pocketNr);
        });

        buttonPanel.add(button);
    }

    private void moveStones(int pocketNr) {
        if (turn == 2) {
            pocketNr += 7;
        }
        try {
            pocket.setMoveStones(pocketNr);
        } catch (CanNotPlayThisPocket e) {
            label.setText("Invalid move! Player " + turn + ", please choose a different pocket.");

            return;
        } catch (GameOver e) {
            int winner = pocket.getWhoIsTheWinner();
            label.setText(e.getMessage() + " The winner is: "+ winner);
            buttonPanel.setEnabled(false);
            return;
        }

        updateBoardDisplay();
        updateTurn();
    }

    private void updateBoardDisplay() {
        if (pocket == null || pocketLabels == null) return;

        // Gebruik PocketTemplate in plaats van Pocket!
        PocketTemplate current = this.pocket;

        for (int i = 0; i < 14; i++) {
            if (current != null) {
                int stones = current.getStonesAmount();
                pocketLabels[i].setText("Pocket " + (i + 1) + ": " + stones + " stenen");

                // Cast naar PocketTemplate in plaats van Pocket
                current = current.getNextPocket();
            }
        }
    }

    private void updateTurn() {
        this.turn = this.pocket.getWhoseTurnIsIt();

        if (label != null) {
            label.setText("Player " + turn + " is now, which pocket would you like to move?");
        }
    }

    public void run() {
        frame = new Frame("Mancala Game");
        frame.setLayout(new BorderLayout());

        label = new Label("Welkom bij Mancala!", Label.CENTER);
        Button buttonStart = new Button("Start Game");

        buttonStart.addActionListener(e -> {
            startGame();
        });

        Panel startPanel = new Panel();
        startPanel.add(buttonStart);

        frame.add(label, BorderLayout.NORTH);
        frame.add(startPanel, BorderLayout.CENTER);

        frame.setSize(1200, 350);
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        MainWhole app = new MainWhole();
        app.run();
    }
}