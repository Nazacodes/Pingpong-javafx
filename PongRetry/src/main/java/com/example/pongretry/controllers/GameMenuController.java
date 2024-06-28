package com.example.pongretry.controllers;

import com.example.pongretry.DAO.Gamebuilder;
import com.example.pongretry.DAO.PostgreSQLManager;
import com.example.pongretry.model.Player;
import com.example.pongretry.model.Racket;
import com.example.pongretry.view.GameMenuView;
import com.example.pongretry.view.GameView;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Controller class for the game menu view.
 */
public class GameMenuController {
    private final GameMenuView view;
    private final Stage primaryStage;
    private final Scene menuScene;
    private final Racket racket1;
    private final Racket racket2;
    private final Player player1;
    private final Player player2;

    private static final double ASPECT_RATIO = 16.0 / 9.0;

    /**
     * Constructor for the GameMenuController class.
     *
     * @param view         The game menu view.
     * @param primaryStage The primary stage of the JavaFX application.
     * @param menuScene    The scene of the game menu.
     */
    public GameMenuController(GameMenuView view, Stage primaryStage, Scene menuScene) {
        this.view = view;
        this.primaryStage = primaryStage;
        this.menuScene = menuScene;
        racket1 = new Racket(0);
        racket2 = new Racket(0);
        player1 = new Player("Player 1", racket1);
        player2 = new Player("Player 2", racket2);

        view.getStartButton().setOnAction(this::handleStartButton);
        view.getLoadButton().setOnAction(this::handleLoadButton);
    }

    /**
     * Event handler for the "Load Game" button.
     *
     * @param event The ActionEvent representing the button click.
     */
    private void handleLoadButton(ActionEvent event) {
        try {
            // Get all game names from the database
            List<String> gameNames = PostgreSQLManager.getInstance().getAllGameNames();

            // If there are no games in the database, show an error message
            if (gameNames.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Load Game Error");
                alert.setHeaderText("No games found in the database.");
                alert.setContentText("Please create a new game first.");
                alert.showAndWait();
                return;
            }

            // Create a choice dialog for the user to select the game to load
            ChoiceDialog<String> dialog = new ChoiceDialog<>(gameNames.get(0), gameNames);
            dialog.setTitle("Load Game");
            dialog.setHeaderText("Please select the game you want to load:");
            dialog.setContentText("Game:");

            // Show the dialog and wait for the user's response
            Optional<String> result = dialog.showAndWait();

            // If the user selected a game, load its information
            if (result.isPresent()) {
                String selectedGameName = result.get();

                // Load game information from the database
                Gamebuilder loadedGameInfo = PostgreSQLManager.getInstance().loadGameInfoFromDatabase(selectedGameName);
                if (loadedGameInfo != null) {
                    // Set player names and scores
                    player1.setName(loadedGameInfo.getPlayer1Name());
                    player2.setName(loadedGameInfo.getPlayer2Name());
                    player1.setScore(loadedGameInfo.getPlayer1Score());
                    player2.setScore(loadedGameInfo.getPlayer2Score());

                    // Set racket size directly from loaded game info
                    racket1.setSize(loadedGameInfo.getRacketSize());
                    racket2.setSize(loadedGameInfo.getRacketSize());

                    // Create the game view with loaded information
                    GameView game = new GameView(primaryStage, player1, player2, loadedGameInfo.getBallSpeed(),
                            loadedGameInfo.getBallFrequencyIncrease(), loadedGameInfo.getAmountToWin(),
                            new Gamesettings(loadedGameInfo.getRacketSize(), loadedGameInfo.getBallSpeed(),
                                    loadedGameInfo.getBallFrequencyIncrease(), loadedGameInfo.getAmountToWin()),
                            loadedGameInfo.getGameName());
                    Scene gameScene = game.getScene();
                    gameScene.getRoot().getStyleClass().add("root");

                    // Adjust window size and show the game scene
                    double gameWidth = 900;
                    double gameHeight = gameWidth / ASPECT_RATIO;
                    primaryStage.setWidth(gameWidth);
                    primaryStage.setHeight(gameHeight);
                    primaryStage.setX(0);
                    primaryStage.setY(0);
                    primaryStage.setScene(gameScene);
                    primaryStage.setTitle("NAZAPONG!");
                    primaryStage.show();
                } else {
                    // Handle case where loading game information failed
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Load Game Error");
                    alert.setHeaderText("Failed to load game information.");
                    alert.setContentText("Please try again.");
                    alert.showAndWait();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Event handler for the "Start Game" button.
     *
     * @param event The ActionEvent representing the button click.
     */
    private void handleStartButton(ActionEvent event) {
        try {
            // Create a TextInputDialog for the user to input the game name
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Enter Game Name");
            dialog.setHeaderText("Please enter the name of the game:");
            dialog.setContentText("Game Name:");

            // Show the dialog and wait for the user's response
            Optional<String> result = dialog.showAndWait();

            // If the user entered a game name, proceed with starting the game
            if (result.isPresent()) {
                String gameName = result.get();
                String player1Name = view.getPlayer1Name();
                String player2Name = view.getPlayer2Name();
                int racketSize = view.getRacketSize();
                int amountToWin = view.getAmountToWin();
                int ballSpeed = view.getBallSpeed();
                int ballFrequencyIncrease = view.getBallFrequencyIncrease();

                // Create game settings object
                Gamesettings gamesettings = new Gamesettings(racketSize, ballSpeed, ballFrequencyIncrease, amountToWin);

                // Save game information to the database
                PostgreSQLManager.getInstance().saveGameInfoToDatabase(gameName, player1Name, player2Name, 0, 0,
                        gamesettings);

                // Set player names and racket sizes
                player1.setName(player1Name);
                player2.setName(player2Name);
                racket1.setSize(racketSize);
                racket2.setSize(racketSize);

                // Create and show the game view
                GameView game = new GameView(primaryStage, player1, player2, ballSpeed, ballFrequencyIncrease,
                        amountToWin, gamesettings, gameName);
                Scene gameScene = game.getScene();
                gameScene.getRoot().getStyleClass().add("root");

                double gameWidth = 900;
                double gameHeight = gameWidth / ASPECT_RATIO;

                primaryStage.setWidth(gameWidth);
                primaryStage.setHeight(gameHeight);
                primaryStage.setX(0);
                primaryStage.setY(0);
                primaryStage.setScene(gameScene);
                primaryStage.setTitle("NAZAPONG!");
                primaryStage.show();
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Start Error");
            alert.setHeaderText("An error occurred while starting the game.");
            alert.setContentText("Please try again.");
            alert.showAndWait();
        }
    }

    /**
     * Method to show the game menu scene.
     */
    public void showMenuScene() {
        primaryStage.setScene(menuScene);
    }
}
