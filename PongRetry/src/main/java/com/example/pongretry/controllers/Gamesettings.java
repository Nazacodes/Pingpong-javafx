package com.example.pongretry.controllers;

import java.io.*;

public class Gamesettings implements Serializable {
    private int racketSize;
    private int ballSpeed;
    private int ballFrequencyIncrease;
    private int amountToWin;

    public Gamesettings(int racketSize, int ballSpeed, int ballFrequencyIncrease, int amountToWin) {
        this.racketSize = racketSize;
        this.ballSpeed = ballSpeed;
        this.ballFrequencyIncrease = ballFrequencyIncrease;
        this.amountToWin = amountToWin;
    }

    public int getRacketSize() {
        return racketSize;
    }

    public void setRacketSize(int racketSize) {
        this.racketSize = racketSize;
    }

    public int getBallSpeed() {
        return ballSpeed;
    }

    public void setBallSpeed(int ballSpeed) {
        this.ballSpeed = ballSpeed;
    }

    public int getBallFrequencyIncrease() {
        return ballFrequencyIncrease;
    }

    public void setBallFrequencyIncrease(int ballFrequencyIncrease) {
        this.ballFrequencyIncrease = ballFrequencyIncrease;
    }

    public int getAmountToWin() {
        return amountToWin;
    }

    public void setAmountToWin(int amountToWin) {
        this.amountToWin = amountToWin;
    }

    public void saveSettings(String fileName) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName))) {
            outputStream.writeObject(this);
            System.out.println("Game settings saved successfully.");
        } catch (IOException e) {
            System.out.println("Failed to save game settings: " + e.getMessage());
        }
    }

    public static Gamesettings loadSettings(String fileName) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName))) {
            return (Gamesettings) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Failed to load game settings: " + e.getMessage());
            return null;
        }
    }
}
