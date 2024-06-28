package com.example.pongretry.controllers;

import com.example.pongretry.DAO.PostgreSQLManager;
import com.example.pongretry.model.Ball;
import com.example.pongretry.model.Player;
import com.example.pongretry.model.Racket;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class GameController implements Serializable {
    private final String gamename;
    private transient Pane root;
    private transient RacketMovementHandler racketHandler;
    private transient BallMovementHandler ballHandler;
    private transient Thread racketThread;
    private transient Thread ballThread;
    private Racket racket1;
    private Racket racket2;
    private Ball ball;
    private Player player1;
    private Player player2;
    private boolean isPaused;
    private transient Button pauseButton;
    private final int amountToWin;
    private transient Stage primaryStage;
    private GameSaveLoadManager saveLoadManager;

    public GameController(Pane root, Racket racket1, Racket racket2, Ball ball, Player player1, Player player2,
                          double ballSpeed, double ballFrequency, int amountToWin, Stage primaryStage , String gamename) {
        this.root = root;
        this.racket1 = racket1;
        this.racket2 = racket2;
        this.gamename = gamename;
        this.ball = ball;
        this.player1 = player1;
        this.player2 = player2;
        this.amountToWin = amountToWin;
        this.primaryStage = primaryStage;
        this.saveLoadManager = GameSaveLoadManager.getInstance();

        racketHandler = new RacketMovementHandler(root, racket1, racket2, this);
        ballHandler = new BallMovementHandler(root, ball, racket1, racket2, player1, player2, ballSpeed, ballFrequency, this ,gamename );

        racketThread = new Thread(racketHandler);
        ballThread = new Thread(ballHandler);

        racketThread.start();
        ballThread.start();
        this.pauseButton = createPauseButton();
        gameWinner();

        primaryStage.getScene().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.P) {
                if (isPaused()) {
                } else {
                    pauseGame();
                }
            }
        });
    }

    public void gameWinner() {
        Player winnerPlayer = null;
        if (player1.getScore() >= amountToWin) {
            winnerPlayer = player1;
        } else if (player2.getScore() >= amountToWin) {
            winnerPlayer = player2;
        }

        if (winnerPlayer != null) {
            primaryStage.close();
            Stage winnerStage = new Stage();
            StackPane winnerPane = new StackPane();

            Label winnerLabel = new Label("Winner: " + winnerPlayer.getName());
            winnerLabel.setStyle("-fx-font-size: 30px; -fx-text-fill: red;");

            Button exitButton = new Button("Exit");
            exitButton.setOnAction(event -> {
                winnerStage.close();
            });

            VBox vbox = new VBox(10, winnerLabel, exitButton);
            vbox.setAlignment(Pos.CENTER);

            winnerPane.getChildren().add(vbox);

            Scene winnerScene = new Scene(winnerPane, 300, 200);
            winnerStage.setScene(winnerScene);
            winnerStage.setTitle("Winner");
            winnerStage.show();
        }
    }

    public synchronized void pauseGame() {
        isPaused = true;
        pauseButton.setText("Resume");
        showPauseMenu();
    }

    public synchronized void resumeGame() {
        isPaused = false;
        pauseButton.setText("Pause");
        notifyAll();
    }

    public synchronized void resetGame(String gamename) throws InterruptedException {
        ball.setCenterX(root.getWidth() / 2);
        ball.setCenterY(root.getHeight() / 2);

        player1.setScore(0);
        player2.setScore(0);
        try {
            PostgreSQLManager.getInstance().updateScoreInDatabase(gamename, player2.getScore(), player2.getScore());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        isPaused = false;
        pauseButton.setText("Pause");

        notifyAll();
    }

    public synchronized boolean isPaused() {
        return isPaused;
    }

    public synchronized void waitIfNeeded() {
        while (isPaused) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Button createPauseButton() {
        Button pauseButton = new Button("Pause");

        pauseButton.setOnAction(e -> {
            if (isPaused()) {
                showPauseMenu();
            } else {
                pauseGame();
            }
        });

        return pauseButton;
    }

    private void showPauseMenu() {
        Alert pauseAlert = new Alert(Alert.AlertType.NONE);
        pauseAlert.setTitle("Paused");
        pauseAlert.setHeaderText(null);
        pauseAlert.setContentText("Game Paused");

        ButtonType resumeButtonType = new ButtonType("Resume");
        ButtonType saveButtonType = new ButtonType("Save Game");
        ButtonType saveToDatabaseButtonType = new ButtonType("Save to Database"); // New button
        ButtonType loadButtonType = new ButtonType("Load Game");
        ButtonType resetButtonType = new ButtonType("Reset");

        pauseAlert.getButtonTypes().setAll(resumeButtonType, saveButtonType, saveToDatabaseButtonType, loadButtonType, resetButtonType); // Added saveToDatabaseButtonType

        Stage stage = (Stage) pauseAlert.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);

        pauseAlert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == resumeButtonType) {
                resumeGame();
            } else if (buttonType == saveButtonType) {
                saveGame();
            } else if (buttonType == saveToDatabaseButtonType) { // Handle Save to Database button click
                saveToDatabase();
            } else if (buttonType == loadButtonType) {
                loadGame();
                resumeGame();
            } else if (buttonType == resetButtonType) {
                try {
                    resetGame(gamename);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void saveToDatabase() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Save Game to Database");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter game name:");
        dialog.setGraphic(null);

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String gameName = result.get();
            saveDatabaseGame(gameName, player1.getName(), player2.getName(), player1.getScore(), player2.getScore(), amountToWin);
        }
    }

    public void saveGame() {
        File directory = new File("saved_games");
        if (!directory.exists()) {
            directory.mkdir();
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Save Game");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter file name:");
        dialog.setGraphic(null);

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String fileName = result.get();
            String filePath = "saved_games/" + fileName + ".ser";
            saveLoadManager.saveGame(player1, player2, ball, filePath);
        }
    }

    public void loadGame() {
        File directory = new File("saved_games");
        if (!directory.exists()) {
            directory.mkdir();
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Game");
        fileChooser.setInitialDirectory(directory);

        File selectedFile = fileChooser.showOpenDialog(primaryStage);

        if (selectedFile != null) {
            saveLoadManager.loadGame(player1, player2, ball, selectedFile.getAbsolutePath());
        }
    }
    public void saveDatabaseGame(String gameName, String player1Name, String player2Name, int player1Score, int player2Score, int gameLimit) {
        String sql = "INSERT INTO games (game_name, player1_name, player2_name, player1_score, player2_score, game_limit) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = PostgreSQLManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, gameName);
            statement.setString(2, player1Name);
            statement.setString(3, player2Name);
            statement.setInt(4, player1Score);
            statement.setInt(5, player2Score);
            statement.setInt(6, gameLimit);
            statement.executeUpdate();
            System.out.println("Game saved to PostgreSQL.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
