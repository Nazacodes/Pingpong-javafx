package com.example.pongretry.view;

import com.example.pongretry.controllers.Gamesettings;
import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Objects;


/**
 * Represents view of menu of the game
 */
public class GameMenuView extends StackPane {
    private final TextField player1NameField;
    private final TextField player2NameField;
    private final Spinner<Integer> racketSizeSpinner;
    private final Spinner<Integer> amountToWinSpinner;
    private final Button startButton;
    private final Button loadButton;
    private final Spinner<Integer> ballSpeedSpinner;
    private final Spinner<Integer> ballFrequencySpinner; // Added spinner for ball frequency increase

    /**
     * Creating menu Items
     */
    public GameMenuView() {

        ImageView titleImageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/stylesheets/image/gamelogo.png")).toExternalForm()));
        titleImageView.setFitWidth(300);
        titleImageView.setPreserveRatio(true);

        player1NameField = new TextField("Player 1");
        player1NameField.getStyleClass().add("player-text-field");
        player1NameField.setMaxWidth(200);

        player2NameField = new TextField("Player 2");
        player2NameField.getStyleClass().add("player-text-field");
        player2NameField.setMaxWidth(200);

        racketSizeSpinner = new Spinner<>(1, 3, 2);
        racketSizeSpinner.setEditable(true);

        Label racketThicknessLabel = new Label("Racket Thickness:");
        Spinner<Integer> racketThicknessSpinner = new Spinner<>(1, 5, 2);
        racketThicknessSpinner.setEditable(true);

        ballSpeedSpinner = new Spinner<>(1, 3, 2);
        ballSpeedSpinner.setEditable(false);

        ballFrequencySpinner = new Spinner<>(1, 10, 5); // Initialize spinner for ball frequency increase
        ballFrequencySpinner.setEditable(false);

        amountToWinSpinner = new Spinner<>(1, 10, 5);
        amountToWinSpinner.setEditable(false);

        Label racketSizeLabel = new Label("Racket Size:");
        racketSizeLabel.getStyleClass().add("racket-size-label");

        Label ballSpeedLabel = new Label("Ball Speed:");
        ballSpeedLabel.getStyleClass().add("ball-speed-label");

        Label ballFrequencyIncreaseLabel = new Label("Ball Frequency Increase:");
        ballFrequencyIncreaseLabel.getStyleClass().add("ball-frequency-increase-label");

        Label amountToWinLabel = new Label("Amount to Win:");
        amountToWinLabel.getStyleClass().add("amount-to-win-label");

        startButton = new Button("Start");
        startButton.getStyleClass().add("start-button");


        loadButton = new Button("Load Game");
        loadButton.getStyleClass().add("load-button");

        VBox menuContent = new VBox(10, titleImageView, player1NameField, player2NameField, ballSpeedLabel, ballSpeedSpinner,
                ballFrequencyIncreaseLabel, ballFrequencySpinner,
                racketSizeLabel, racketSizeSpinner, racketThicknessLabel, racketThicknessSpinner,
                amountToWinLabel, amountToWinSpinner, startButton,  loadButton); // Add load button to the menu
        menuContent.setAlignment(Pos.CENTER);
        menuContent.setMaxWidth(600);

        StackPane.setAlignment(menuContent, Pos.CENTER);
        this.getChildren().add(menuContent);

        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(3), menuContent);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.play();
    }

    /**
     * @return Start button
     */
    public Button getStartButton() {
        return startButton;
    }

    /**
     * @return Load button
     */
    public Button getLoadButton() {
        return loadButton;
    }

    /**
     * @return player name for first player
     */
    public String getPlayer1Name() {
        return player1NameField.getText();
    }

    /**
     * @return player name for second player
     */
    public String getPlayer2Name() {
        return player2NameField.getText();
    }

    /**
     * @return racket size for player
     */
    public int getRacketSize() {
        return racketSizeSpinner.getValue();
    }

    /**
     * @return sets the amount of goals needed to win
     */
    public int getAmountToWin() {
        return amountToWinSpinner.getValue();
    }

    /**
     * @return ball speed
     */
    public int getBallSpeed() {
        return ballSpeedSpinner.getValue();
    }

    /**
     * @return the amount of bounces needed before a speed increase
     */
    public int getBallFrequencyIncrease() {
        return ballFrequencySpinner.getValue();
    }

    // Method to initialize fields with loaded settings
    public void initializeWithSettings(Gamesettings gameSettings) {
        player1NameField.setText("Player 1");
        player2NameField.setText("Player 2");
        racketSizeSpinner.getValueFactory().setValue(gameSettings.getRacketSize());
        amountToWinSpinner.getValueFactory().setValue(gameSettings.getAmountToWin());
    }
}
