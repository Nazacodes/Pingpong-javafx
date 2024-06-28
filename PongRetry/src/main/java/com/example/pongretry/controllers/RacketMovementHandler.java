package com.example.pongretry.controllers;

import com.example.pongretry.model.Racket;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Handles the movement of rackets in the Pong game.
 */
public class RacketMovementHandler implements Runnable , Serializable {
    private final Pane root;
    private final Racket racket1;
    private final Racket racket2;
    private final double movementSpeed = 2;
    private final Set<KeyCode> pressedKeys = new HashSet<>();
    private final GameController gameController;

    /**
     * Constructs a RacketMovementHandler with the specified parameters.
     *
     * @param root            The root pane of the Pong game.
     * @param racket1         The first racket in the game.
     * @param racket2         The second racket in the game.
     * @param gameController  The GameController associated with the game.
     */
    public RacketMovementHandler(Pane root, Racket racket1, Racket racket2, GameController gameController) {
        this.root = root;
        this.racket1 = racket1;
        this.racket2 = racket2;
        this.gameController = gameController;

        root.setOnKeyPressed(new KeyPressedListener());
        root.setOnKeyReleased(new KeyReleasedListener());
    }

    /**
     * Runs the racket movement logic in a separate thread.
     */
    @Override
    public void run() {
        while (true) {
            gameController.waitIfNeeded(); // Wait if the game is paused
            moveRackets();
            try {
                Thread.sleep(2); // Adjust sleep duration for smoother movement
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Moves the rackets based on the pressed keys.
     */
    private void moveRackets() {
        double maxHeightPercentage = 0.2;
        if (pressedKeys.contains(KeyCode.W) && racket1.getLayoutY() > root.getHeight() * maxHeightPercentage) {
            Platform.runLater(() -> racket1.setLayoutY(racket1.getLayoutY() - movementSpeed));
        }
        if (pressedKeys.contains(KeyCode.S) && racket1.getLayoutY() + racket1.getHeight() < root.getHeight()) {
            Platform.runLater(() -> racket1.setLayoutY(racket1.getLayoutY() + movementSpeed));
        }
        if (pressedKeys.contains(KeyCode.I) && racket2.getLayoutY() > root.getHeight() * maxHeightPercentage) {
            Platform.runLater(() -> racket2.setLayoutY(racket2.getLayoutY() - movementSpeed));
        }
        if (pressedKeys.contains(KeyCode.K) && racket2.getLayoutY() + racket2.getHeight() < root.getHeight()) {
            Platform.runLater(() -> racket2.setLayoutY(racket2.getLayoutY() + movementSpeed));
        }
    }

    /**
     * Handles the key pressed events.
     */
    private class KeyPressedListener implements javafx.event.EventHandler<KeyEvent> {
        @Override
        public void handle(KeyEvent event) {
            KeyCode keyCode = event.getCode();
            pressedKeys.add(keyCode);
        }
    }

    /**
     * Handles the key released events.
     */
    private class KeyReleasedListener implements javafx.event.EventHandler<KeyEvent> {
        @Override
        public void handle(KeyEvent event) {
            KeyCode keyCode = event.getCode();
            pressedKeys.remove(keyCode);
        }
    }
}
