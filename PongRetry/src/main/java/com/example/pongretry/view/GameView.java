package com.example.pongretry.view;

import com.example.pongretry.controllers.GameController;
import com.example.pongretry.controllers.Gamesettings;
import com.example.pongretry.model.Ball;
import com.example.pongretry.model.Player;
import com.example.pongretry.model.Racket;
import javafx.animation.FadeTransition;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.Serializable;
import java.util.Objects;

    /**
     * Represents the view of the Pong game.
     */
    public class GameView implements Serializable {
        private final Scene scene;
        private final Player player1;
        private final Player player2;
        private final Racket racket1;
        private final Racket racket2;
        private final Ball ball;
        private final int amountToWin;
        private final String gamename;

        /**
         * Constructs a GameView object with the specified parameters.
         *
         * @param primaryStage         The primary stage of the game.
         * @param player1              The first player.
         * @param player2              The second player.
         * @param setspeed             The speed setting for the ball.
         * @param ballFrequencyIncrease The frequency increase setting for the ball.
         * @param amountToWin          The number of points required to win the game.
         */
        public GameView(Stage primaryStage, Player player1, Player player2, int setspeed, int ballFrequencyIncrease, int amountToWin , Gamesettings gamesettings ,String gamename) {
            this.player1 = player1;
            this.player2 = player2;
            this.gamename = gamename;
            racket1 = player1.getRacket();
            racket2 = player2.getRacket();
            double racketh = racket2.getHeight();
            double racketw = racket2.getWidth();
            this.amountToWin = amountToWin;


            ball = new Ball();
            double ballspeed = ball.getSpeed(setspeed);
            System.out.println(ballspeed);
            double ballbouncefequenceincrease = ball.getFrequency(ballFrequencyIncrease);
            System.out.println(ballbouncefequenceincrease);

            Button exitButton = new Button("Exit");
            exitButton.getStyleClass().add("exit-button");
            exitButton.setOnAction(event -> primaryStage.close());

            Pane root = new Pane();
            root.getStyleClass().add("root");

            scene = new Scene(root);
            scene.widthProperty().addListener((obs, oldVal, newVal) -> resizeGameComponents(newVal.doubleValue(), scene.getHeight(), racketw, racketh));
            scene.heightProperty().addListener((obs, oldVal, newVal) -> resizeGameComponents(scene.getWidth(), newVal.doubleValue(), racketw, racketh));
            resizeGameComponents(primaryStage.getWidth(), primaryStage.getHeight(), racketw, racketh);

            root.getChildren().addAll(ball, racket1, racket2, player1.getLabel(), player2.getLabel(), exitButton);

            primaryStage.setScene(scene);
            primaryStage.widthProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.doubleValue() / oldValue.doubleValue() != getAspectRatio()) {
                    primaryStage.setWidth(newValue.doubleValue());
                    primaryStage.setHeight(newValue.doubleValue() / getAspectRatio());
                }
            });

            primaryStage.heightProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.doubleValue() / oldValue.doubleValue() != 1 / getAspectRatio()) {
                    primaryStage.setHeight(newValue.doubleValue());
                    primaryStage.setWidth(newValue.doubleValue() * getAspectRatio());
                }
            });
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/stylesheets/game.css")).toExternalForm());

            GameController gameController = new GameController(root, player1.getRacket(), player2.getRacket(), ball, player1, player2, ballspeed, ballFrequencyIncrease, amountToWin, primaryStage , gamename);
            Button pauseButton = gameController.createPauseButton();
            root.getChildren().add(pauseButton);


            FadeTransition fadeTransition = new FadeTransition(Duration.seconds(3), root);
            fadeTransition.setFromValue(0.0);
            fadeTransition.setToValue(1.0);
            fadeTransition.play();
            primaryStage.requestFocus();
        }

            private double getAspectRatio() {
            return 16.0 / 9.0;
        }

        /**
         * gets scene
         *
         *
         * @return The scene of the game view.
         */
        public Scene getScene() {
            return scene;
        }
        /**
         * Resizes game components based on the specified width, height, racket width, and racket height.
         *
         * @param width     The width of the scene.
         * @param height    The height of the scene.
         * @param racketw   The width of the racket.
         * @param racketh   The height of the racket.
         */
    private void resizeGameComponents(double width, double height, double racketw, double racketh) {
        double racketWidth = ((width * .02) + (racketw)) / 2;
        double racketHeight = ((height * .015) * (racketh)) / 20;

        double racket1X = width * 0.05;
        double racket1Y = height * 0.5 - racketHeight * 0.5;

        double racket2X = width * 0.93 - racketWidth;
        double racket2Y = height * 0.5 - racketHeight * 0.5;
        double ballRadius = Math.min(width, height) * 0.02;

        double ballX = width * 0.5;
        double ballY = height * 0.5;

        ball.setRadius(ballRadius);
        ball.setCenterX(ballX);
        ball.setCenterY(ballY);

        player1.getLabel().setLayoutX(width * 0.05);
        player1.getLabel().setLayoutY(height * 0.04);

        player2.getLabel().setLayoutX(width * 0.75);
        player2.getLabel().setLayoutY(height * 0.04);

        double labelFontSize = Math.min(width, height) * 0.08;
        player1.getLabel().setFont(new Font(labelFontSize));
        player2.getLabel().setFont(new Font(labelFontSize));

        racket1.setWidth(racketWidth);
        racket1.setHeight(racketHeight);
        racket1.setLayoutX(racket1X);
        racket1.setLayoutY(racket1Y);

        racket2.setWidth(racketWidth);
        racket2.setHeight(racketHeight);
        racket2.setLayoutX(racket2X);
        racket2.setLayoutY(racket2Y);
    }


}