package com.example.pongretry.controllers;

import com.example.pongretry.model.Ball;
import com.example.pongretry.model.Player;

import java.io.*;

public class GameSaveLoadManager {
    private static GameSaveLoadManager instance;

    private GameSaveLoadManager() {}

    public static synchronized GameSaveLoadManager getInstance() {
        if (instance == null) {
            instance = new GameSaveLoadManager();
        }
        return instance;
    }

    public void saveGame(Player player1, Player player2, Ball ball, String fileName) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName))) {
            outputStream.writeObject(player1);
            outputStream.writeObject(player2);
            outputStream.writeObject(ball);
            System.out.println("Game saved successfully.");
        } catch (IOException e) {
            System.out.println("Failed to save game: " + e.getMessage());
        }
    }

    public void loadGame(Player player1, Player player2, Ball ball, String fileName) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName))) {
            Player loadedPlayer1 = (Player) inputStream.readObject();
            Player loadedPlayer2 = (Player) inputStream.readObject();
            Ball loadedBall = (Ball) inputStream.readObject();

            player1.setName(loadedPlayer1.getName());
            player1.setScore(loadedPlayer1.getScore());
            player2.setName(loadedPlayer2.getName());
            player2.setScore(loadedPlayer2.getScore());
            ball.setCenterX(loadedBall.getCenterX());
            ball.setCenterY(loadedBall.getCenterY());

            System.out.println("Game loaded successfully.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Failed to load game: " + e.getMessage());
        }
    }
}
