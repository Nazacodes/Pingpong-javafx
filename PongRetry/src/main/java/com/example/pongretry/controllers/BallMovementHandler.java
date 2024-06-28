package com.example.pongretry.controllers;

import com.example.pongretry.model.Ball;
import com.example.pongretry.model.Player;
import com.example.pongretry.model.Racket;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Random;
import com.example.pongretry.DAO.PostgreSQLManager;


public class BallMovementHandler implements Runnable, Serializable {
    private final Pane root;
    private final Ball ball;
    private transient Label goalPopup;
    private double savedDX;
    private double savedDY;
    private final Racket racket1;
    private final Racket racket2;
    private final Player player1;
    private final Player player2;
    private final double ballSpeed;
    private final double ballfeq;
    private double dx;
    private double dy;
    private boolean startMoving;
    private int bounceCounter;
    private final GameController gameController;
    private final String gameName;

    public BallMovementHandler(Pane root, Ball ball, Racket racket1, Racket racket2, Player player1, Player player2, double ballSpeed, double ballfeq, GameController gameController, String gameName) {
        this.root = root;
        this.ball = ball;
        this.racket1 = racket1;
        this.racket2 = racket2;
        this.player1 = player1;
        this.player2 = player2;
        this.ballSpeed = ballSpeed;
        this.gameController = gameController;
        this.ballfeq = ballfeq;
        this.gameName = gameName;
        bounceCounter = 0;
        dx = .8;
        dy = 1;
        startMoving = false;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        startMoving = true;

        Random random = new Random();
        dx = random.nextBoolean() ? .8 : -.8;
        dy = random.nextBoolean() ? 1 : -1;

        double speedMultiplier = ballSpeed * 0.5;
        dx *= speedMultiplier;
        dy *= speedMultiplier;

        while (true) {
            gameController.waitIfNeeded();

            if (startMoving) {
                Platform.runLater(() -> {
                    ball.setCenterX(ball.getCenterX() + dx);
                    ball.setCenterY(ball.getCenterY() + dy);

                    handleCollisions();
                    increase();
                    if (ball.getCenterX() >= root.getWidth() - ball.getRadius()) {
                        int score = player1.getScore()+1;
                        player1.setScore(score);
                        try {
                            PostgreSQLManager.getInstance().updateScoreInDatabase(gameName, score, player2.getScore());
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        handleGoal();
                        gameController.gameWinner();
                    } else if (ball.getCenterX() <= ball.getRadius()) {
                        int score = player2.getScore()+1;
                        player2.setScore(score);
                        try {
                            PostgreSQLManager.getInstance().updateScoreInDatabase(gameName, player1.getScore(),score);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        handleGoal();
                    }


                });

                try {
                    // Adjust sleep time based on ball frequency
                    Thread.sleep((long) (3));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void handleCollisions() {
        if (racket1.getBoundsInParent().intersects(ball.getBoundsInParent())) {
            dx = Math.abs(dx);
        }

        if (racket2.getBoundsInParent().intersects(ball.getBoundsInParent())) {
            dx = -Math.abs(dx);
        }

        if (ball.getCenterY() <= (root.getHeight() - root.getHeight() * .80) - ball.getRadius() || ball.getCenterY() >= root.getHeight() - ball.getRadius()) {
            dy = -dy;
        }
    }

    public void increase() {
        if (ball.getCenterY() <= (root.getHeight() - root.getHeight() * .80) - ball.getRadius() || ball.getCenterY() >= root.getHeight() - ball.getRadius()) {
            incrementBounceCounter();
        }
    }

    private void handleGoal() {
        savedDX = dx;
        savedDY = dy;

        ball.setCenterX(root.getWidth() / 2);
        ball.setCenterY(root.getHeight() / 2);
        dx = 0;
        dy = 0;
        bounceCounter = 0;

        goalPopup = new Label("Goal!");
        goalPopup.setStyle("-fx-font-size: 70px; -fx-background-color: transparent; -fx-text-fill: white;");

        double popupX = (root.getWidth()) / 2;
        double popupY = (root.getHeight()) / 2;
        goalPopup.setLayoutX(popupX);
        goalPopup.setLayoutY(popupY);

        root.getChildren().add(goalPopup);

        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event -> {
            Random random = new Random();
            dx = random.nextBoolean() ? .8 : -.8;
            dy = random.nextBoolean() ? 1 : -1;

            double speedMultiplier = ballSpeed * 0.5;
            dx *= speedMultiplier;
            dy *= speedMultiplier;
            root.getChildren().remove(goalPopup);
        });
        pause.play();
    }

    private void incrementBounceCounter() {
        bounceCounter++;
        if (bounceCounter >= ballfeq) {
            dx *= 1.5;
            dy *= 1.5;
            bounceCounter = 0;
            System.out.println(dx);
            System.out.println(dy);
            System.out.println(bounceCounter);
        }
    }

    public double getDx() {
        return dx;
    }

    public double getDy() {
        return dy;
    }
}
